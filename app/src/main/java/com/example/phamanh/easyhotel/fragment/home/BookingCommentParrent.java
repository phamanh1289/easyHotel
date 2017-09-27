package com.example.phamanh.easyhotel.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.BookingCommentPager;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.base.BaseModel;
import com.example.phamanh.easyhotel.model.InfomationModel;
import com.example.phamanh.easyhotel.model.ListComment;
import com.example.phamanh.easyhotel.model.ListRating;
import com.example.phamanh.easyhotel.utils.Constant;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BookingCommentParrent extends BaseFragment {

    @BindView(R.id.fragBookingComment_tvTitle)
    TextView tvTitle;
    @BindView(R.id.fragBookingComment_tlBookComment)
    TabLayout tabBookComment;
    @BindView(R.id.fragBookingComment_viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    private BookingCommentPager adapter;
    public InfomationModel mInfomationModel;
    public ListRating mDataRating = new ListRating();
    public ListComment mCommentModel = new ListComment();
    public String mKey;

    public static BookingCommentParrent newInstance(BaseModel item) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.BASE_MODEL, item);
        BookingCommentParrent fragment = new BookingCommentParrent();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_booking_comment, container, false);
        setActionBar(view, getString(R.string.page_home_detail));
        setVisibilityTabBottom(View.GONE);
        KeyboardUtils.setupUI(view, getActivity());
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mInfomationModel = (InfomationModel) bundle.getSerializable(Constant.BASE_MODEL);
            mKey = mInfomationModel.getId();
            adapter = new BookingCommentPager(getChildFragmentManager());
            viewpager.setAdapter(adapter);
            tabBookComment.setupWithViewPager(viewpager);
            tabBookComment.getTabAt(0).setText("Detail");
            tabBookComment.getTabAt(1).setText("Comment");
            tabBookComment.getTabAt(2).setText("Booking");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
