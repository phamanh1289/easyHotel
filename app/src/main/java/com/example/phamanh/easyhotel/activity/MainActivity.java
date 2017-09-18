package com.example.phamanh.easyhotel.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.base.BaseActivity;
import com.example.phamanh.easyhotel.fragment.home.HomeFragment;
import com.example.phamanh.easyhotel.fragment.nearby.NearbyFragment;
import com.example.phamanh.easyhotel.fragment.news.NewsFragment;
import com.example.phamanh.easyhotel.fragment.settings.SettingFragment;
import com.example.phamanh.easyhotel.other.enums.TabsEnum;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.phamanh.easyhotel.other.enums.TabsEnum.NEARBY;
import static com.example.phamanh.easyhotel.other.enums.TabsEnum.HOME;
import static com.example.phamanh.easyhotel.other.enums.TabsEnum.SETTING;
import static com.example.phamanh.easyhotel.other.enums.TabsEnum.NEWS;

public class MainActivity extends BaseActivity implements View.OnTouchListener {

    @BindView(android.R.id.tabhost)
    public FragmentTabHost tabHost;
    @BindView(R.id.activityMain_llTabBottom)
    public LinearLayout llTabBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KeyboardUtils.hideSoftKeyboard(this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(createTabSpec(HOME), HomeFragment.class, null);
        tabHost.addTab(createTabSpec(NEARBY), NearbyFragment.class, null);
        tabHost.addTab(createTabSpec(NEWS), NewsFragment.class, null);
        tabHost.addTab(createTabSpec(SETTING), SettingFragment.class, null);
        setTabsColor();
        tabHost.setOnTabChangedListener(s -> {
            clearAllBackStack();
            setTabsColor();
            saveCurrentTab(tabHost.getCurrentTab());
        });
    }

    private void setTabsColor() {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            setColorForView(i, false);
        }
        setColorForView(tabHost.getCurrentTab(), true);
    }

    private void setColorForView(int i, boolean select) {
        View view = tabHost.getTabWidget().getChildAt(i);
        TextView tvTitle = view.findViewById(R.id.viewTabMain_tvTitle);
        ImageView imgIcon = view.findViewById(R.id.viewTabMain_imgIcon);
        imgIcon.setSelected(select);
        tvTitle.setTextColor(ContextCompat.getColor(this, select ? R.color.denimBlueTwo : R.color.denimBlue));
    }

    public TabHost.TabSpec createTabSpec(TabsEnum tab) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tab.toString());
        Typeface font = Typeface.createFromAsset(
                getAssets(), "fonts/Helvetica.ttf");
        View view = LayoutInflater.from(this).inflate(R.layout.view_tab_main, null, false);
        ((ImageView) view.findViewById(R.id.viewTabMain_imgIcon)).setImageResource(tab.getIdIcon());
        ((TextView) view.findViewById(R.id.viewTabMain_tvTitle)).setText(tab.getTitle());
        ((TextView) view.findViewById(R.id.viewTabMain_tvTitle)).setTypeface(font);

        tabSpec.setIndicator(view);
        return tabSpec;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KeyboardUtils.hideSoftKeyboard(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        int stackCount = fragmentManager.getBackStackEntryCount();
        if (stackCount <= 0) {
            llTabBottom.setVisibility(View.VISIBLE);
        }
    }
}
