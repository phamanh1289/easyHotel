package com.example.phamanh.easyhotel.other.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.DialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActionServiceDialog extends Dialog {


    @BindView(R.id.dialogActionService_etCode)
    EditText etCode;
    private DialogListener clickListener;

    public void setOnItemClickListener(DialogListener listener) {
        this.clickListener = listener;
    }

    public ActionServiceDialog(Context context) {
        super(context, android.R.style.Theme_Holo_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            getWindow().setDimAmount(0.3f);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        setContentView(R.layout.dialog_action_service);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
    }

    public String getEditText() {
        if (TextUtils.isEmpty(etCode.getText().toString())) {
            return etCode.getText().toString();
        }
        return "";
    }

    @OnClick({R.id.dialogActionService_tvCancel, R.id.dialogActionService_tvSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialogActionService_tvCancel:
                if (clickListener != null) {
                    clickListener.onCancelClicked();
                }
                dismiss();
                break;
            case R.id.dialogActionService_tvSave:
                if (clickListener != null) {
                    clickListener.onConfirmClicked();
                }
                dismiss();
                break;
        }
    }
}
