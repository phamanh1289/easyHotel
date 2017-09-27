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
import com.example.phamanh.easyhotel.utils.KeyboardUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
    private ListComment mListComment;
    private CommentModel modelComment;
    private List<CommentModel> mDataComment = new ArrayList<>();
    private String mKey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        unbinder = ButterKnife.bind(this, view);
        KeyboardUtils.setupUI(view, getActivity());
        init();
        return view;
    }
    private void init() {
        mKey = ((BookingCommentParrent) getParentFragment()).mKey;
        adapter = new CommentAdapter(mDataComment);
        rvComment.setAdapter(adapter);
        rvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mDataComment.size() != 0)
            mDataComment.clear();
        refHotel_comment.child(mKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null){
                    try {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                        if (jsonObject != null) {
                            modelComment = gson.fromJson(jsonObject.toString(), CommentModel.class);
//                            mDataComment.add(toChangeAvatarComment(dataSnapshot.getKey(), modelComment));
                            mDataComment.add(modelComment);
                        }
                        adapter.notifyItemInserted(mDataComment.size() - 1);
                        rvComment.scrollToPosition(mDataComment.size());
                        tvNoData.setVisibility(mDataComment.size() != 0 ? View.GONE : View.VISIBLE);
                        rvComment.setVisibility(mDataComment.size() != 0 ? View.VISIBLE : View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dismissLoading();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean toValidate() {
        if (etContent.getText().toString().isEmpty()) {
            AppUtils.showAlert(getContext(), getString(R.string.warning), "Please enter content", null);
            return false;
        }
        return true;
    }

    private CommentModel toChangeAvatarComment(String key, CommentModel item) {
        if (item.getEmail().equals(getUser().getEmail())) {
            if (!item.getImage().equals(getUser().getAvatar())) {
                item.setImage(getUser().getAvatar());
                refHotel_comment.child(mKey).child(key).setValue(new Gson().toJson(item));
            }
        }
        return item;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.fragComment_tvEnter)
    public void onViewClicked() {
        if (toValidate()) {
            refHotel_comment.child(mKey).push().setValue(new Gson().toJson(new CommentModel(System.currentTimeMillis(), getUser().getEmail(), etContent.getText().toString(), getUser().getAvatar())));
            etContent.setText("");
        }
    }
}
