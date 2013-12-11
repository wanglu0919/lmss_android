package com.challentec.lmss.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.listener.AppConectStateListener;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SocketClient;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.recever.AppConnectStateRecever;
import com.challentec.lmss.recever.AppMessageRecever;
import com.challentec.lmss.service.LoginPollingService;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.PollingUtils;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.LoadProgressView;

/**
 * 设备选择
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ChoiceDeviceActivity extends BaseActivity {

	private final static int SCANNIN_GREQUEST_CODE = 0x01;
	private LoadProgressView loadProgess;
	private EditText choice_et_no;// 设备授权码输入框
	private Button choice_btn_codescan;// 二维码扫描
	private Button choice_btn_submit;// 提交
	private AppMessageRecever appMessageRecever;
	private AppConnectStateRecever appConnectStateRecever;

	@Override
	protected void addListeners() {
		super.addListeners();
		choice_btn_codescan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChoiceDeviceActivity.this,
						CodeScanActivity.class);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

			}
		});

		choice_btn_submit.setOnClickListener(new OnClickListener() {// 提交

					@Override
					public void onClick(View v) {

						doSubmitDeviceNo();

					}
				});

	}

	/**
	 * 提交设备授权码
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void doSubmitDeviceNo() {
		if (checkInput()) {
			loadProgess.setVisibility(View.VISIBLE);
			String deviceNo = choice_et_no.getText().toString();

			String apiData = ClientAPI.getApiStr(
					Protocol.C_SEND_AUTHORIZATION_CODE, deviceNo);

			new SynTask(new SynHandler(), appContext).writeData(apiData);

		}

	}

	/**
	 * 检查设备授权码
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	private boolean checkInput() {
		if (choice_et_no.getText().toString().equals("")) {
			UIHelper.showToask(this, R.string.tip_msg_form_device_no_empty);
			return false;
		}

		return true;
	}

	/**
	 * 注册状态改变监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void registAppConnectStateRecever() {
		appConnectStateRecever = appManager
				.registerAppConnectStateRecever(this);
		appConnectStateRecever
				.setAppConectStateListener(new ChoiceConnectStateMessageListener());
	}

	/**
	 * 停止网络状态监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void unregisterAppConnectStateReceiver() {
		this.unregisterReceiver(appConnectStateRecever);
	}

	private class ChoiceConnectStateMessageListener implements
			AppConectStateListener {

		@Override
		public void connectStateChanged() {
			appManager.stopPolling();// 停止心跳
			pb_head.setVisibility(View.INVISIBLE);
			pb_text.setVisibility(View.VISIBLE);
			pb_text.setText(getString(R.string.title_tip_unconnnect));
			connectServer();

		}

	}

	/**
	 * 连接服务器
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void connectServer() {

		SocketClient socketClient = SocketClient.getSocketClient();

		new SynTask(new SynHandler() {

			@Override
			public void onConnectSuccess(String code) {// 连接成功

				super.onConnectSuccess(code);

				appManager.startPolling();// 开始心跳

				aginLogin();// 重新登陆
			}

			@Override
			public void onFianly() {

			}

		}, appContext).connectServer(socketClient);
	}

	/**
	 * 登录，重新登陆
	 * 
	 * @author 泰得利通 wanglu
	 */

	private void aginLogin() {
		pb_head.setVisibility(View.VISIBLE);
		pb_text.setText(getString(R.string.title_tip_auto_connecting));
		PollingUtils.startPollingService(appContext,
				AppConfig.AUTO_CONNECT_TIME, LoginPollingService.class,
				LoginPollingService.ACTION);// 开始自动登录
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onStart() {
		super.onStart();

		registAppConnectStateRecever();// 注册网络状态监听
		registerAppMessageRecever();// 注册消息监听
	}

	@Override
	protected void onStop() {

		unregisterAppConnectStateReceiver();// 取消网络监听
		unregisterAppMessageRecever();// 取消消息监听
		super.onStop();
	}

	private class ChoiceDeviceMessageLinstener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(
					Protocol.C_SEND_AUTHORIZATION_CODE)) {
				if (responseData.isSuccess()) {// 成功
					SharedPreferences sp = appConfig.getSharedPreferences();
					Editor editor = sp.edit();
					editor.putString(AppConfig.DEVICE_NUMBER_KEY, choice_et_no
							.getText().toString());// 保存设备编码
					editor.commit();
					Intent intent = new Intent(ChoiceDeviceActivity.this,
							MainTabActivity.class);// 跳转至tab
					startActivity(intent);

				} else {
					UIHelper.showToask(ChoiceDeviceActivity.this, AppTipMessage
							.getString(ChoiceDeviceActivity.this,
									responseData.getErrorCode()));
				}
				loadProgess.setVisibility(View.GONE);
			} else if (responseData.getFunctionCode().equals(Protocol.C_LOGIN)) {// 登陆消息返回数据

				if (!responseData.isSuccess()) {// 登陆失败

					UIHelper.showToask(ChoiceDeviceActivity.this, AppTipMessage
							.getString(ChoiceDeviceActivity.this,
									responseData.getErrorCode()));

				} else {// 登陆成功
					/*
					UIHelper.showToask(appContext,
							R.string.tip_msg_auto_connect_success);// 重连成功
							*/

				}
				PollingUtils.stopPollingService(appContext,
						LoginPollingService.class, LoginPollingService.ACTION);// 停止自动登录
				pb_head.setVisibility(View.GONE);
				pb_text.setVisibility(View.GONE);

			}

		}

	}

	@Override
	protected void initMainView(View mainView) {

		choice_et_no = (EditText) mainView.findViewById(R.id.choice_et_no);
		choice_btn_codescan = (Button) mainView
				.findViewById(R.id.choice_btn_codescan);
		choice_btn_submit = (Button) mainView
				.findViewById(R.id.choice_btn_submit);
		loadProgess = (LoadProgressView) findViewById(R.id.device_pb_load);

		new SynTask(appContext).uiLog(Protocol.UI_LOGIN_SUCCESS);// 操作日志

		//choice_et_no.setText("20130101");

	}

	/**
	 * 注册消息监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void registerAppMessageRecever() {
		appMessageRecever = appManager.registerAppMessageRecever(this);
		appMessageRecever
				.setAppMessageLinstener(new ChoiceDeviceMessageLinstener());// 注册消息监听

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}

	/**
	 * 取消消息监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void unregisterAppMessageRecever() {
		unregisterReceiver(appMessageRecever);

	}

	@Override
	protected CharSequence getTitleText() {
		return getResources().getString(R.string.ui_login_success);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_choice_device_layout;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();

				choice_et_no.setText(bundle.getString("result"));

			}
			break;
		}
	}

}
