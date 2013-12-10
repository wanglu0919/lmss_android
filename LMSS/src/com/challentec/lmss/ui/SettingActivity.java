package com.challentec.lmss.ui;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.challentec.lmss.app.R;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.Protocol;

/**
 * 设置界面
 * @author 泰得利通 wanglu
 *
 */
public class SettingActivity extends TabContentBaseActivity {
	private RelativeLayout setting_rl_basic_params, setting_rl_auto_study,
			setting_rl_elec_params, setting_rl_floor_display_setting,
			setting_rl_mainboard_input_setting, setting_rl_soft_about,
			setting_rl_floor_door_setting,setting_rl_change_device;

	@Override
	protected void initMainView(View mainView) {
		setting_rl_basic_params = (RelativeLayout) mainView
				.findViewById(R.id.setting_rl_basic_params);// 基本参数
		setting_rl_auto_study = (RelativeLayout) mainView
				.findViewById(R.id.setting_rl_auto_study);// 自学习
		setting_rl_elec_params = (RelativeLayout) mainView
				.findViewById(R.id.setting_rl_elec_params);// 电机参数
		setting_rl_floor_display_setting = (RelativeLayout) mainView
				.findViewById(R.id.setting_rl_floor_display_setting);// 楼层显示设置

		setting_rl_mainboard_input_setting = (RelativeLayout) mainView
				.findViewById(R.id.setting_rl_mainboard_input_setting);// 主板输入设置
		setting_rl_soft_about = (RelativeLayout) mainView
				.findViewById(R.id.setting_rl_soft_about);

		setting_rl_floor_door_setting = (RelativeLayout) mainView
				.findViewById(R.id.setting_rl_floor_door_setting);// 楼层前后门设置
		
		setting_rl_change_device=(RelativeLayout) mainView.findViewById(R.id.setting_rl_change_device);//设备更换
		
		new SynTask(appContext).uiLog(Protocol.UI_SETTING_HOME);//UI日志
	}

	/**
	 * 事件处理
	 */
	@Override
	protected void addListeners() {

		super.addListeners();

		setting_rl_basic_params.setOnClickListener(new OnClickListener() {// 基本参数

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(SettingActivity.this,
								BasicParamActivity.class);
						MainTabActivity.instance.addView(intent);

					}
				});

		setting_rl_auto_study.setOnClickListener(new OnClickListener() {// 自学习

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(SettingActivity.this,
								AutoStudyActivity.class);
						MainTabActivity.instance.addView(intent);

					}
				});

		setting_rl_elec_params.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(SettingActivity.this,
						ElecParamActivity.class);
				MainTabActivity.instance.addView(intent);
			}
		});

		setting_rl_floor_display_setting
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						Intent intent = new Intent(SettingActivity.this,
								FloorDisplayActivity.class);
						MainTabActivity.instance.addView(intent);
					}
				});

		setting_rl_mainboard_input_setting
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(SettingActivity.this,
								MainBoardActivity.class);
						MainTabActivity.instance.addView(intent);
					}
				});

		setting_rl_soft_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(SettingActivity.this,
						SoftwareAboutActivity.class);
				MainTabActivity.instance.addView(intent);
			}
		});

		setting_rl_floor_door_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(SettingActivity.this,
						FloorDoorSetActivity.class);
				MainTabActivity.instance.addView(intent);
			}
		});
		
		
		setting_rl_change_device.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,
						ChangeDeviceActivity.class);
				MainTabActivity.instance.addView(intent);
				
			}
		});
	}

	@Override
	protected CharSequence getTitleText() {

		return getResources().getString(R.string.ui_title_setting);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_setting_layout;
	}

}
