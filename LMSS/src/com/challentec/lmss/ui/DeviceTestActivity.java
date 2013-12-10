package com.challentec.lmss.ui;

import com.challentec.lmss.app.R;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.Protocol;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * 
 * @author 泰得利通 wanglu 测试 Activity
 * 
 */
public class DeviceTestActivity extends TabContentBaseActivity {
	private RelativeLayout device_test_rl_vector_control_params,
			device_test_rl_run_control_params,
			device_test_rl_time_control_params,
			device_test_rl_test_function_control_params,
			device_fast_rl_fast_debug;

	@Override
	protected void addListeners() {

		super.addListeners();
		device_test_rl_vector_control_params
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(DeviceTestActivity.this,
								VectorControlParamsActivity.class);
						MainTabActivity.instance.addView(intent);
					}
				});

		device_test_rl_run_control_params
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(DeviceTestActivity.this,
								RunControlParamsActivity.class);
						MainTabActivity.instance.addView(intent);
					}
				});

		device_test_rl_time_control_params
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(DeviceTestActivity.this,
								TimeControlParamsActivity.class);
						MainTabActivity.instance.addView(intent);
					}
				});

		device_test_rl_test_function_control_params
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(DeviceTestActivity.this,
								DeviceTestFunctionParamsActivity.class);
						MainTabActivity.instance.addView(intent);
					}
				});

		device_fast_rl_fast_debug.setOnClickListener(new OnClickListener() {// 快速调试

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(DeviceTestActivity.this,
								FastDebug2Activity.class);
						MainTabActivity.instance.addView(intent);
					}
				});
	}

	@Override
	protected void initMainView(View mainView) {

		device_test_rl_vector_control_params = (RelativeLayout) mainView
				.findViewById(R.id.device_test_rl_vector_control_params);// 矢量控制参数
		device_test_rl_run_control_params = (RelativeLayout) mainView
				.findViewById(R.id.device_test_rl_run_control_params);
		device_test_rl_time_control_params = (RelativeLayout) mainView
				.findViewById(R.id.device_test_rl_time_control_params);
		device_test_rl_test_function_control_params = (RelativeLayout) mainView
				.findViewById(R.id.device_test_rl_test_function_control_params);
		device_fast_rl_fast_debug = (RelativeLayout) mainView
				.findViewById(R.id.device_fast_rl_fast_debug);// 快速调试
		new SynTask(appContext).uiLog(Protocol.UI_TEST_HOME);// 提交操作日志

	}

	@Override
	protected CharSequence getTitleText() {

		return getResources().getString(R.string.ui_title_device_test);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_device_test_layout;
	}

}
