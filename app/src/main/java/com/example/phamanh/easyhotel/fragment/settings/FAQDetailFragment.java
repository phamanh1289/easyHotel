package com.example.phamanh.easyhotel.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.base.BaseModel;
import com.example.phamanh.easyhotel.model.FAQModel;
import com.example.phamanh.easyhotel.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FAQDetailFragment extends BaseFragment {

    @BindView(R.id.fragFAQDetail_tvTitle)
    TextView tvTitle;
    @BindView(R.id.fragFAQDetail_tvDescription)
    TextView tvDescription;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq_detail, container, false);
        setActionBar(view, getString(R.string.faq_1));
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public static FAQDetailFragment newInstance(BaseModel item) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.BASE_MODEL, item);
        FAQDetailFragment fragment = new FAQDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Constant.BASE_MODEL)) {
            FAQModel item = (FAQModel) bundle.getSerializable(Constant.BASE_MODEL);
            tvTitle.setText(item.getQuestions());
            tvDescription.setText(item.getAnswers());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
