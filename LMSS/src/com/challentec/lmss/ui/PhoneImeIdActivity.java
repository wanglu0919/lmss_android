package com.challentec.lmss.ui;

import android.view.View;
import android.widget.TextView;

import com.challentec.lmss.app.R;

/**
 * 
 * @author 泰得利通 wanglu 手机识别码
 * 
 */
public class PhoneImeIdActivity extends TabContentBaseActivity {

	private TextView soft_tv_tele_imei;// imei号

	@Override
	protected void initMainView(View mainView) {

		soft_tv_tele_imei = (TextView) mainView
				.findViewById(R.id.soft_tv_tele_imei);

		initData();

	}

	private void initData() {

		soft_tv_tele_imei.setText(appManager.getIMEI());

	}

	@Override
	protected CharSequence getTitleText() {

		return getResources().getString(R.string.ui_title_phone_ime);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_phone_id_layout;
	}

}
