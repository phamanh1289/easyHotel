package com.example.phamanh.easyhotel.other.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.SelectionSingleAdapter;
import com.example.phamanh.easyhotel.interfaces.ItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * *******************************************
 * * Created by Simon on 14/09/2017.           **
 * * Copyright (c) 2015 by AppsCyclone      **
 * * All rights reserved                    **
 * * http://appscyclone.com/                **
 * *******************************************
 */

public class SelectSinglePopup extends PopupWindow {

    private List<String> data = new ArrayList<>();
    private ItemListener listener;
    private boolean fullBorder = false;

    public void setOnItemListener(ItemListener listener) {
        this.listener = listener;
    }

    public SelectSinglePopup(Context context, List<String> data, boolean fullBorder) {
        super(context);
        this.data = data;
        this.fullBorder = fullBorder;
        onCreateView(context);
    }

    private void onCreateView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_text_view, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(true);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dialogEditText_RvList);
        SelectionSingleAdapter adapter = new SelectionSingleAdapter(data);
        adapter.setOnItemListener(listenerSelection);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        setWidth(anchor.getWidth() != 0 ? anchor.getWidth() : LinearLayout.LayoutParams.WRAP_CONTENT);
        super.showAsDropDown(anchor, xoff, yoff);
    }


    private ItemListener listenerSelection = new ItemListener() {
        @Override
        public void onItemClicked(int pos) {
            if (listener != null) {
                listener.onItemClicked(pos);
            }
        }
    };


}
