package com.hereafter.wxarticle.util;

import com.hereafter.wxarticle.R;

import android.app.Dialog;
import android.content.Context;

public class MyDialog extends Dialog {
	private Context context;
	private Dialog dialog;

	public MyDialog(Context context) {
		super(context);
		this.context = context;
		dialog = new Dialog(context, R.style.DialogLight);
	}

	public Dialog showLoadingDialog() {
		dialog.setContentView(R.layout.dialog_progress);
		dialog.setCancelable(true);// 设置按返回键是否关闭dialog
		dialog.show();
		return dialog;
	}

}