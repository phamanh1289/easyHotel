package com.example.phamanh.easyhotel.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.phamanh.easyhotel.fragment.home.BookingFragment;
import com.example.phamanh.easyhotel.fragment.home.CommentFragment;
import com.example.phamanh.easyhotel.fragment.home.HomeDetailFragment;

import java.util.ArrayList;
import java.util.List;


public class BookingCommentPager extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public BookingCommentPager(FragmentManager fm) {
        super(fm);
        addFragments();
    }

    private void addFragments() {
        fragments.add(new HomeDetailFragment());
        fragments.add(new CommentFragment());
        fragments.add(new BookingFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
