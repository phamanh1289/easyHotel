package com.example.phamanh.easyhotel.other.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.DialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmDialog extends Dialog {

    @BindView(R.id.dialogConfirm_tvOK)
    TextView tvOK;
    @BindView(R.id.dialogConfirm_tvTitle)
    TextView tvTitle;

    private DialogListener clickListener;
    private String title = "";

    public void setOnItemClickListener(DialogListener listener) {
        this.clickListener = listener;
    }

    public ConfirmDialog(Context context) {
        super(context, android.R.style.Theme_Holo_Dialog);
    }

    public ConfirmDialog(Context context, String title) {
        super(context, android.R.style.Theme_Holo_Dialog);
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getWindow() != null) {
            getWindow().setDimAmount(0.3f);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        setContentView(R.layout.dialog_confirm);
        ButterKnife.bind(this);
        if (!title.isEmpty()) {
            tvTitle.setText(getSpannedText(title));
        }
        setCanceledOnTouchOutside(false);
    }
    private Spanned getSpannedText(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }

    @OnClick({R.id.dialogConfirm_tvOK})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialogConfirm_tvOK:
                if (clickListener != null)
                    clickListener.onConfirmClicked();
                dismiss();
                break;
        }
    }
}
