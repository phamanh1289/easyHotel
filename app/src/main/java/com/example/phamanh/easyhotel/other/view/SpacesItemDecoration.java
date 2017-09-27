package com.example.phamanh.easyhotel.other.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;
    private int spanCount = 2;

    public SpacesItemDecoration(int space) {
        this.spacing = space;
    }

    public SpacesItemDecoration(int column, int space) {
        this.spacing = space;
        this.spanCount = column;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        outRect.left = spacing - column * spacing / spanCount;
        outRect.right = (column + 1) * spacing / spanCount;

        if (position < spanCount) {
            outRect.top = spacing;
        }
        outRect.bottom = spacing;

    }
}
