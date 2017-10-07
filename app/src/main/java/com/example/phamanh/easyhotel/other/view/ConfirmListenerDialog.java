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
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.DialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmListenerDialog extends Dialog {


  @BindView(R.id.dialogConfirmDiary_tvCancel)
  TextView tvCancel;
  @BindView(R.id.dialogConfirmDiary_tvSave)
  TextView tvSave;
  @BindView(R.id.dialogConfirmListener_tvContent)
  TextView tvContent;
  @BindView(R.id.dialogConfirmListener_etCode)
  EditText etCode;
  private DialogListener clickListener;
  private String title, content, cancel, save = "";

  public void setOnItemClickListener(DialogListener listener) {
    this.clickListener = listener;
  }

  public ConfirmListenerDialog(Context context, String title, String content, String cancel,
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
    setContentView(R.layout.dialog_confirm_listener);
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
    if (TextUtils.isEmpty(etCode.getText().toString())) {
      return etCode.getText().toString();
    }
    return "";
  }

  @OnClick({R.id.dialogConfirmDiary_tvCancel, R.id.dialogConfirmDiary_tvSave})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.dialogConfirmDiary_tvCancel:
        if (clickListener != null) {
          clickListener.onCancelClicked();
        }
        dismiss();
        break;
      case R.id.dialogConfirmDiary_tvSave:
        if (clickListener != null) {
          clickListener.onConfirmClicked();
        }
        dismiss();
        break;
    }
  }
}
