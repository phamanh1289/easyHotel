package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.CommentAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.model.CommentModel;
import com.example.phamanh.easyhotel.model.EventBusBooking;
import com.example.phamanh.easyhotel.model.ListComment;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.Constant;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class CommentFragment extends BaseFragment {

    @BindView(R.id.fragComment_rvComment)
    RecyclerView rvComment;
    @BindView(R.id.fragComment_etContentComment)
    EditText etContent;
    @BindView(R.id.fragComment_tvEnter)
    TextView tvEnter;
    @BindView(R.id.fragComment_tvNoData)
    TextView tvNoData;
    Unbinder unbinder;

    private CommentAdapter adapter;
    private List<CommentModel> mDataComment = new ArrayList<>();
    private String mKey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mKey = ((BookingCommentParrent) getParentFragment()).mKey;
        if (mDataComment.size() != 0)
            mDataComment.clear();
        adapter = new CommentAdapter(mDataComment);
        rvComment.setAdapter(adapter);
        rvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private boolean toValidate() {
        if (etContent.getText().toString().isEmpty()) {
            AppUtils.showAlert(getContext(), getString(R.string.warning), "Please enter content", null);
            return false;
        }
        return true;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusBooking mData) {
        if (mData.action.equals(Constant.ACTION_COMMENT)) {
            if (mDataComment.size() == 0) {
                mDataComment.addAll(((BookingCommentParrent) getParentFragment()).mCommentModel.comment);
                rvComment.setVisibility(mDataComment.size() != 0 ? View.VISIBLE : View.GONE);
                tvNoData.setVisibility(mDataComment.size() != 0 ? View.GONE : View.VISIBLE);
                for (CommentModel model : mDataComment) {
                    if (model.getEmail().equals(mUser.getEmail())) {
                        model.setImage(getUser().getAvatar());
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
        EventBus.getDefault().removeStickyEvent(mData);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fragComment_tvEnter)
    public void onViewClicked() {
        if (toValidate()) {
            mDataComment.add(0, new CommentModel(System.currentTimeMillis(), getUser().getEmail(), etContent.getText().toString(), getUser().getAvatar()));
            tvNoData.setVisibility(View.GONE);
            rvComment.setVisibility(View.VISIBLE);
            adapter.notifyItemInserted(0);
            rvComment.scrollToPosition(0);
            ListComment item = new ListComment(mDataComment);
            refHotel_comment.child(mKey).setValue(new Gson().toJson(item));
            etContent.setText("");
        }
    }
}
