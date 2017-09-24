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
import com.example.phamanh.easyhotel.model.ListComment;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.google.gson.Gson;

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
        mDataComment.addAll(((BookingCommentParrent) getParentFragment()).mCommentModel.comment);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fragComment_tvEnter)
    public void onViewClicked() {
        if (toValidate()) {
            mDataComment.add(new CommentModel(System.currentTimeMillis(), getUser().getEmail(), etContent.getText().toString(), getUser().getAvatar()));
            adapter.notifyDataSetChanged();
            ListComment item = new ListComment(mDataComment);
            refHotel_comment.child(mKey).setValue(new Gson().toJson(item));
            etContent.setText("");
        }
    }
}
