package com.example.phamanh.easyhotel.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.phamanh.easyhotel.R;

import java.util.ArrayList;
import java.util.List;


public class ImageSearchPager extends PagerAdapter {
    private ImageView imgBackground;

    private Context context;
    private List<Bitmap> list = new ArrayList<>();

    public ImageSearchPager(Context context, List<Bitmap> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_image_hotel, container, false);
        imgBackground = view.findViewById(R.id.itemImageHotel_ivBanner);
        setContentIntro(position);
        container.addView(view);
        return view;
    }

    private void setContentIntro(int position) {
        imgBackground.setImageBitmap(list.get(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
