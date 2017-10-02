package com.example.phamanh.easyhotel.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.FAQAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.FAQModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FAQFragment extends BaseFragment {


    @BindView(R.id.fragFAQ_rvMain)
    RecyclerView rvMain;
    Unbinder unbinder;

    private List<FAQModel> mDataFAQ = new ArrayList<>();
    private FAQModel model;
    private FAQAdapter adapterFAQ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        setActionBar(view, getString(R.string.faq));
        setVisibilityTabBottom(View.GONE);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        adapterFAQ = new FAQAdapter(mDataFAQ);
        adapterFAQ.setItemListener(toClickItem);
        rvMain.setAdapter(adapterFAQ);
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        toAddData();
    }

    private void toAddData() {
        for (int i = 0; i < 3; i++) {
            model = new FAQModel(getString(R.string.faq) + i, "");
            mDataFAQ.add(model);
        }
        adapterFAQ.notifyDataSetChanged();
    }

    ItemListener toClickItem = pos -> addFragment(new FAQDetailFragment(), true);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
