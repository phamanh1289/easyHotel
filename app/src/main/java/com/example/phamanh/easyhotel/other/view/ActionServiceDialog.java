package com.example.phamanh.easyhotel.other.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.DialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActionServiceDialog extends Dialog {


    @BindView(R.id.dialogActionService_etCode)
    EditText etCode;
    @BindView(R.id.dialogActionService_tvContent)
    TextView tvContent;
    @BindView(R.id.dialogActionService_tvCancel)
    TextView tvCancel;
    @BindView(R.id.dialogActionService_tvSave)
    TextView tvSave;
    private DialogListener clickListener;
    private String content, cancel, save = "";

    public void setOnItemClickListener(DialogListener listener) {
        this.clickListener = listener;
    }

    public ActionServiceDialog(Context context, String content, String cancel,
                                 String save) {
        super(context, android.R.style.Theme_Holo_Dialog);
        this.content = content;
        this.cancel = cancel;
        this.save = save;
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
        if (!content.isEmpty()) {
            tvContent.setText(content);
        }
        if (!save.isEmpty()) {
            tvSave.setText(save);
        }
        if (!cancel.isEmpty()) {
            tvCancel.setText(cancel);
        }
        setCanceledOnTouchOutside(false);
    }

    public String getEditText() {
        if (!etCode.getText().toString().trim().isEmpty()) {
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
                    etCode.setText("");
                }
                dismiss();
                break;
            case R.id.dialogActionService_tvSave:
                if (clickListener != null) {
                    clickListener.onConfirmClicked();
                    etCode.setText("");
                }
                dismiss();
                break;
        }
    }
}
