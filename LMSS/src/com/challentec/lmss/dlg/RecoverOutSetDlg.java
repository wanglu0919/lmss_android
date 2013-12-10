package com.challentec.lmss.dlg;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.challentec.lmss.app.R;

/**
 * 恢复出厂设置自定义对话框
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class RecoverOutSetDlg extends Dialog {

	private Button dlg_btn_ok;
	private Button dlg_btn_cancle;
	private View.OnClickListener buttonOKClickListener;
	private View.OnClickListener buttonCancleClickListener;// 取消
	private TextView dlg_tv_title;

	public RecoverOutSetDlg(Context context,
			View.OnClickListener buttonOKClickListener,
			View.OnClickListener buttonCancleClickListener, String title) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recover_out_set_dialog);
		dlg_btn_ok = (Button) findViewById(R.id.dlg_btn_ok);
		dlg_btn_cancle = (Button) findViewById(R.id.dlg_btn_cancle);
		dlg_tv_title = (TextView) findViewById(R.id.dlg_tv_title);
		dlg_tv_title.setText(context.getString(R.string.dlg_title_ok_cancle)+title);
		this.buttonOKClickListener = buttonOKClickListener;
		this.buttonCancleClickListener = buttonCancleClickListener;

		addListeners();

	}

	/**
	 * @author 泰得利通 wanglu 添加事件监听
	 * 
	 */
	private void addListeners() {
		if (buttonCancleClickListener != null) {
			dlg_btn_cancle.setOnClickListener(buttonCancleClickListener);
		}

		if (buttonOKClickListener != null) {
			dlg_btn_ok.setOnClickListener(buttonOKClickListener);
		}
	}

	public RecoverOutSetDlg(Context context, int theme) {
		super(context, theme);

	}

}
