package com.example.phamanh.easyhotel.other.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.DialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ChooseImageDialog extends Dialog {
    @BindView(R.id.viewDialogChooseImage_tvTake)
    TextView tvTake;
    @BindView(R.id.viewDialogChooseImage_tvIamge)
    TextView tvImage;
    Unbinder unbinder;

    private DialogListener mDialogListener;

    public ChooseImageDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Holo_Dialog);
    }

    public void setDialogListener(DialogListener dialogListener) {
        mDialogListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            getWindow().setDimAmount(0.3f);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        setContentView(R.layout.view_dialog_choose_image_demo);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.viewDialogChooseImage_tvTake, R.id.viewDialogChooseImage_tvIamge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.viewDialogChooseImage_tvTake:
                mDialogListener.onConfirmClicked();
                break;
            case R.id.viewDialogChooseImage_tvIamge:
                mDialogListener.onConfirmClicked();
                break;
        }
    }

}
