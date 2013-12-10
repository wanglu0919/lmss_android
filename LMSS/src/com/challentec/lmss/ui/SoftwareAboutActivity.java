package com.challentec.lmss.ui;

import com.challentec.lmss.app.R;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.Protocol;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author 泰得利通 wanglu 软件相关
 * 
 */
public class SoftwareAboutActivity extends TabContentBaseActivity {

	private TextView soft_tv_version;// 软件版本号

	private RelativeLayout setting_rl_soft_about_us;// 关于我们
	private RelativeLayout setting_rl_phone_ime;// 手机识别码
	private RelativeLayout setting_rl_soft_update;// 软件升级
	private Button soft_btn_login_out;
	private SynTask synTask;

	@Override
	protected void addListeners() {

		super.addListeners();
		soft_btn_login_out.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				synTask.uiLog(Protocol.UI_LOGINOUT);// 退出登录
				appManager.redirtToLogin();// 退出登录

			}
		});

		setting_rl_soft_about_us.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(SoftwareAboutActivity.this,
						AboutUsActivity.class);
				MainTabActivity.instance.addView(intent);

			}
		});

		setting_rl_phone_ime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(SoftwareAboutActivity.this,
						PhoneImeIdActivity.class);
				MainTabActivity.instance.addView(intent);

			}
		});

		setting_rl_soft_update.setOnClickListener(new OnClickListener() {// 软件升级

					@Override
					public void onClick(View v) {
						synTask.uiLog(Protocol.UI_SOFT_UPDATE);// 退出登录

					}
				});
	}

	@Override
	protected void initMainView(View mainView) {
		soft_tv_version = (TextView) mainView
				.findViewById(R.id.soft_tv_version);

		setting_rl_soft_about_us = (RelativeLayout) mainView
				.findViewById(R.id.setting_rl_soft_about_us);
		soft_btn_login_out = (Button) mainView
				.findViewById(R.id.soft_btn_login_out);
		setting_rl_phone_ime = (RelativeLayout) mainView
				.findViewById(R.id.setting_rl_phone_ime);
		setting_rl_soft_update = (RelativeLayout) mainView
				.findViewById(R.id.setting_rl_soft_update);
		initData();
		synTask = new SynTask(appContext);

		synTask.uiLog(Protocol.UI_SOFT_ABOUT);// 软件相关记录

	}

	private void initData() {
		soft_tv_version.setText(getString(R.string.setting_soft_version)
				+ appManager.getAppVersion());

	}

	@Override
	protected CharSequence getTitleText() {

		return getResources().getString(R.string.ui_title_soft_about);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_setting_soft_layout;
	}

}
