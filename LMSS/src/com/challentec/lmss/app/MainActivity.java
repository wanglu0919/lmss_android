package com.challentec.lmss.app;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.GPSInfoService;
import com.challentec.lmss.listener.AppConectStateListener;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SocketClient;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.recever.AppConnectStateRecever;
import com.challentec.lmss.recever.AppMessageRecever;
import com.challentec.lmss.service.AutoConnectPollingService;
import com.challentec.lmss.service.LoginPollingService;
import com.challentec.lmss.ui.ChoiceDeviceActivity;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.LogUtil;
import com.challentec.lmss.util.PollingUtils;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.LoadProgressView;

/**
 * 开始activity 登陆界面
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class MainActivity extends Activity {

	private long lastExitTime = 0;

	private Button main_btn_login;// 登录
	private Button main_btn_getvecode;// 获取验证码
	private TextView main_tv_chinese;// 中文
	private TextView main_tv_english;// 英文
	private EditText main_login_et_tel;// 手机号
	private EditText main_et_vecode;

	private static final int LANGUAGE_TYPE_ZH = 0x01;// 中文
	private static final int LANGUAGE_TYPE_EN = 0x02;// 英文
	private AppMessageRecever appMessageRecever;// 消息广播接收者
	private int CURRENT_LANGUAGE_TYPE = LANGUAGE_TYPE_ZH;
	private static final int LOCATION_SETTING_REQUEAST = 01;// 定位界面code
	private AppContext appContext;
	private AppManager appManager;

	private static final int GET_LOACTION_SUCCESS = 0x02;// 获取位置成功

	private String locationStr = "";// 坐标地址
	private GPSInfoService gpsInfoService;
	private SocketClient socketClient;
	private LoadProgressView main_pb_load;// 加载进度条
	private SharedPreferences sp;
	private AppConnectStateRecever appConnectStateRecever;
	private static final int LOCATION_GET_VECODE = 1;// 验证码获取时定位
	private static final int LOCATION_LOGIN = 2;// 登录时定位
	private int locationType = LOCATION_GET_VECODE;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case GET_LOACTION_SUCCESS:
				if (locationType == LOCATION_GET_VECODE) {
					getVecode();// 获取验证码
				} else if (locationType == LOCATION_LOGIN) {
					doLogin();// 登陆
				}

				gpsInfoService.cancleLocationUpdates();// 取消定位服务
				break;

			}

		}

	};

	/**
	 * 注册状态改变监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void registAppConnectStateRecever() {
		appConnectStateRecever = appManager
				.registerAppConnectStateRecever(this);
		appConnectStateRecever
				.setAppConectStateListener(new MainConnectStateMessageListener());
	}

	private class MainConnectStateMessageListener implements
			AppConectStateListener {

		@Override
		public void connectStateChanged() {
			connect();// 连接服务器

		}

	}

	/**
	 * 服务器返回消息监听类
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class MainAppMessageLinstener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			main_pb_load.setVisibility(View.GONE);// 隐藏加载进度控件
			if (responseData.getFunctionCode().equals(Protocol.C_GET_VCODE)) {// 获取验证码数据返回

				if (!responseData.isSuccess()) {

					UIHelper.showToask(MainActivity.this, AppTipMessage
							.getString(MainActivity.this,
									responseData.getErrorCode()));
				} else {

					UIHelper.showToask(MainActivity.this,
							R.string.tip_msg_get_vecode_success);
				}

			} else if (responseData.getFunctionCode().equals(Protocol.C_LOGIN)) {// 登陆消息返回数据

				if (!responseData.isSuccess()) {// 登陆失败

					UIHelper.showToask(MainActivity.this, AppTipMessage
							.getString(MainActivity.this,
									responseData.getErrorCode()));

				} else {// 登陆成功
					saveTelVecode();// 保存手机号，和验证码
					setLanguage(CURRENT_LANGUAGE_TYPE);// 设置语言环境
					Intent intent = new Intent(MainActivity.this,
							ChoiceDeviceActivity.class);
					startActivity(intent);

				}
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setLanguage(LANGUAGE_TYPE_ZH);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);
		findViews();
		setLanguageTextViewStyle();
		addLinsteners();

		appContext = (AppContext) getApplication();
		appManager = AppManager.getManager(appContext);
		appManager.addActivity(this);
		sp = AppConfig.getAppConfig(appContext).getSharedPreferences();
		clearCache();// 清除本地存储信息
		connect();// 连接服务器

		PollingUtils.stopPollingService(appContext, LoginPollingService.class,
				LoginPollingService.ACTION);
		PollingUtils.stopPollingService(appContext,
				AutoConnectPollingService.class,
				AutoConnectPollingService.ACTION);

	}

	@Override
	protected void onStart() {
		super.onStart();
		registerAppMessageRecever();// 注册消息监听
		registAppConnectStateRecever();// 注册网络状态监听

	}

	@Override
	protected void onStop() {
		super.onStop();

		unregisterReceiver(appMessageRecever);// 取消消息监听
		unregisterReceiver(appConnectStateRecever);// 取消网络状态监听
	}

	/**
	 * 注册消息监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void registerAppMessageRecever() {
		appMessageRecever = appManager.registerAppMessageRecever(this);
		appMessageRecever.setAppMessageLinstener(new MainAppMessageLinstener());// 注册消息监听
	}

	/**
	 * 保存手机号和验证码
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void saveTelVecode() {
		Editor editor = sp.edit();
		editor.putString(AppConfig.TELE_PHONE_NUM_KEY, main_login_et_tel
				.getText().toString());// 保存手机号
		editor.putString(AppConfig.VECODE_KEY, main_et_vecode.getText()
				.toString());// 保存验证码
		editor.commit();

	}

	/**
	 * 清楚SharePrefence存储的缓存
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void clearCache() {

		Editor editor = sp.edit();
		editor.clear();
		editor.commit();

	}

	/**
	 * 连接服务器
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void connect() {
		main_pb_load.setVisibility(View.VISIBLE);
		main_pb_load.setProgressText(R.string.tip_msg_pb_connecting_server);
		socketClient = SocketClient.getSocketClient();

		new SynTask(new SynHandler() {

			@Override
			public void onConnectSuccess(String code) {// 连接成功

				super.onConnectSuccess(code);

				new SynTask(appContext).uiLog(Protocol.UI_LOGIN);// 记录操作日志
				appManager.startPolling();// 开始心跳

			}

			@Override
			public void onFianly() {

				main_pb_load.setVisibility(View.GONE);
			}

		}, appContext).connectServer(socketClient);

	}

	/**
	 * 设置语言
	 * 
	 * @author 泰得利通 wanglu
	 * @param languageTypeZh
	 *            app语言类型
	 */
	private void setLanguage(int languageTypeZh) {

		Resources resources = getResources();// 获得res资源对象
		Configuration config = resources.getConfiguration();// 获得设置对象
		DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。

		switch (languageTypeZh) {

		case LANGUAGE_TYPE_ZH:
			CURRENT_LANGUAGE_TYPE = LANGUAGE_TYPE_ZH;
			config.locale = Locale.SIMPLIFIED_CHINESE; // 简体中文

			break;
		case LANGUAGE_TYPE_EN:
			CURRENT_LANGUAGE_TYPE = LANGUAGE_TYPE_EN;
			config.locale = Locale.ENGLISH;

			break;
		}

		resources.updateConfiguration(config, dm);

	}

	/**
	 * 控件事件处理
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void addLinsteners() {
		main_btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!appContext.isGPSOPen()) {// 用户基站定位没有打开
					locationType = LOCATION_LOGIN;
					showOpenGPSDlg();// 显示定位对话框

				} else {// 基站已经定位已经打开
					if (checkInput()) {
						locationType = LOCATION_LOGIN;
						getLoaction();
					}

				}

			}
		});

		main_btn_getvecode.setOnClickListener(new OnClickListener() {// 获取手机验证码

					@Override
					public void onClick(View v) {

						if (main_login_et_tel.getText().toString().trim()
								.equals("")) {
							UIHelper.showToask(MainActivity.this,
									R.string.tip_msg_form_tele_empty);
							main_login_et_tel.requestFocus();
							return;
						}

						if (!appContext.isGPSOPen()) {// 用户基站定位没有打开
							locationType = LOCATION_GET_VECODE;
							showOpenGPSDlg();// 显示定位对话框

						} else {// 基站已经定位已经打开

							locationType = LOCATION_GET_VECODE;
							getLoaction();// 定位

						}

					}
				});

		main_tv_english.setOnClickListener(new OnClickListener() {// 英文

					@Override
					public void onClick(View v) {
						/*
						 * setLanguage(LANGUAGE_TYPE_EN);
						 * setContentView(R.layout.activity_main_layout);
						 * findViews(); addLinsteners();
						 * setLanguageTextViewStyle();
						 * 
						 * String hexStr = ClinetAPI.getHexApiStr(
						 * Protocol.C_SET_LANGUAGE, "01"); new SynTask(new
						 * SynHandler(), appContext) .writeData(hexStr);
						 */
						UIHelper.showToask(appContext, "施工中...");

					}
				});

		main_tv_chinese.setOnClickListener(new OnClickListener() {// 中文

					@Override
					public void onClick(View v) {
						setLanguage(LANGUAGE_TYPE_ZH);
						setContentView(R.layout.activity_main_layout);
						findViews();
						setLanguageTextViewStyle();
						addLinsteners();

						String hexStr = ClientAPI.getHexApiStr(
								Protocol.C_SET_LANGUAGE, "02");
						new SynTask(new SynHandler(), appContext)
								.writeData(hexStr);
					}
				});

	}

	/**
	 * 登陆
	 * 
	 * @author 泰得利通 wanglu
	 */
	protected void doLogin() {

		if (checkInput()) {
			main_pb_load.setVisibility(View.VISIBLE);
			main_pb_load.setProgressText(getString(R.string.main_tab_logining));
			String apiData = ClientAPI.getApiStr(
					Protocol.C_LOGIN,
					main_login_et_tel.getText().toString() + "|"
							+ main_et_vecode.getText().toString() + "|"
							+ appManager.getIMEI() + "|"
							+ locationStr.toString());// 手机号+验证码+IME号+经纬度

			new SynTask(new SynHandler() {

			}, appContext).writeData(apiData);

		}

	}

	/**
	 * 发送并获取验证
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void getVecode() {

		String tele = main_login_et_tel.getText().toString();
		String apiData = ClientAPI.getApiStr(Protocol.C_GET_VCODE, tele + "|"
				+ appManager.getIMEI() + "|" + locationStr);
		LogUtil.i(LogUtil.LOG_TAG_LOCATION, locationStr);
		new SynTask(new SynHandler()

		, appContext).writeData(apiData);

	}

	/**
	 * 获取位置信息
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void getLoaction() {
		main_pb_load.setVisibility(View.VISIBLE);
		main_pb_load.showProgessText(false);
		gpsInfoService = GPSInfoService.getInstance(MainActivity.this);

		gpsInfoService.registerLocationUpdates(new MyGPSLinster());

	}

	/**
	 * 查找初始化控件
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void findViews() {
		main_btn_login = (Button) findViewById(R.id.main_btn_login);
		main_btn_getvecode = (Button) findViewById(R.id.main_btn_getvecode);
		main_tv_chinese = (TextView) findViewById(R.id.main_tv_chinese);
		main_tv_english = (TextView) findViewById(R.id.main_tv_english);
		main_pb_load = (LoadProgressView) findViewById(R.id.main_pb_load);
		main_login_et_tel = (EditText) findViewById(R.id.main_login_et_tel);
		main_et_vecode = (EditText) findViewById(R.id.main_et_vecode);

		// main_login_et_tel.setText("13888888888");
		// main_et_vecode.setText("123456");

	}

	/**
	 * 设置语言颜色高亮显示
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void setLanguageTextViewStyle() {
		if (CURRENT_LANGUAGE_TYPE == LANGUAGE_TYPE_ZH) {// 是中文
			main_tv_english.setTextColor(getResources().getColor(
					R.color.tv_nomal_color));
			main_tv_chinese.setTextColor(getResources().getColor(R.color.red));
		} else if (CURRENT_LANGUAGE_TYPE == LANGUAGE_TYPE_EN) {// 是英文
			main_tv_english.setTextColor(getResources().getColor(R.color.red));
			main_tv_chinese.setTextColor(getResources().getColor(
					R.color.tv_nomal_color));
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出app
	 * 
	 * @author 泰得利通 wanglu
	 */

	public void exit() {

		if ((System.currentTimeMillis() - lastExitTime) > 2000) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.tip_msg_exit),
					Toast.LENGTH_SHORT).show();
			lastExitTime = System.currentTimeMillis();
		} else {

			AppManager.getManager(this).appExit(this);
		}
	}

	private class MyGPSLinster implements LocationListener {

		// 用户位置改变的时候 的回调方法
		public void onLocationChanged(Location location) {
			// 获取到用户的纬度
			double latitude = location.getLatitude();
			// 获取到用户的经度
			double longitude = location.getLongitude();
			// 进行封装写入到文件中
			locationStr = longitude + "," + latitude;

			handler.sendEmptyMessage(GET_LOACTION_SUCCESS);

		}

		// 状态改变
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		// gps ,打开
		public void onProviderEnabled(String provider) {
		}

		// 关闭
		public void onProviderDisabled(String provider) {
		}
	}

	/**
	 * 表单检查输入
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 * @return
	 */

	private boolean checkInput() {

		if (main_login_et_tel.getText().toString().equals("")) {
			UIHelper.showToask(this, R.string.tip_msg_form_tele_empty);
			main_login_et_tel.requestFocus();
			return false;
		} else if (main_et_vecode.getText().toString().equals("")) {
			UIHelper.showToask(this, R.string.tip_msg_form_vecode_empty);
			main_et_vecode.requestFocus();
			return false;
		}

		return true;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == LOCATION_SETTING_REQUEAST) {// 定位设置返回
			if (appContext.isGPSOPen()) {// 基站定位打开
				if(locationType == LOCATION_LOGIN&&checkInput()){
					getLoaction();// 定位
				}else if(locationType == LOCATION_GET_VECODE){
					getLoaction();
				}
				
			} else {
				
				locationStr = "0,0";// 默认地址
				if (locationType == LOCATION_GET_VECODE) {
					main_pb_load.setProgressText("");
					main_pb_load.setVisibility(View.VISIBLE);
					getVecode();// 获取验证码
				} else if (locationType == LOCATION_LOGIN) {
					doLogin();// 登录
				}

			}
		}

	}

	private void showOpenGPSDlg() {

		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(R.drawable.applog);
		builder.setTitle(getString(R.string.main_location_tip_title));
		builder.setMessage(getString(R.string.main_location_message));
		builder.setCancelable(false);

		builder.setPositiveButton(getString(R.string.dlg_msg_ok),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// 打开GPS 设定界面
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivityForResult(intent,
								LOCATION_SETTING_REQUEAST);
					}
				});

		builder.setNegativeButton(getString(R.string.dlg_msg_cancle),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						locationStr = "0,0";// 默认地址
						if (locationType == LOCATION_GET_VECODE) {
							main_pb_load.showProgessText(false);
							main_pb_load.setVisibility(View.VISIBLE);
							getVecode();// 获取验证码
						} else if (locationType == LOCATION_LOGIN) {

							doLogin();// 登录

						}

					}

				});

		builder.create().show();
	}

}
