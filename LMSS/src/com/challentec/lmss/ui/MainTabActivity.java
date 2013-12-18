package com.challentec.lmss.ui;

import java.util.Stack;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.challentec.lmss.app.AppCache;
import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.AppContext;
import com.challentec.lmss.app.AppManager;
import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.bean.StackElement;
import com.challentec.lmss.listener.AppConectStateListener;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SocketClient;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.recever.AppConnectStateRecever;
import com.challentec.lmss.recever.AppMessageRecever;
import com.challentec.lmss.recever.MonitorRecever;
import com.challentec.lmss.service.AutoConnectPollingService;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.LogUtil;
import com.challentec.lmss.util.PollingUtils;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;

/**
 * tabActivity
 * 
 * @author 泰得利通 wanglu
 * 
 */

public class MainTabActivity extends ActivityGroup implements
		OnCheckedChangeListener, OnClickListener {

	private FrameLayout main_content;
	private static final int REMOVE_VIEW = 0x00;

	public Stack<StackElement> stack;
	private RadioButton main_tab_rb_setting, main_tab_rb_test,
			main_tab_rb_monitor, main_tab_rb_trouble;
	public static MainTabActivity instance;
	private RadioGroup main_tab_radiogroup;
	private ProgressBar pb_head;
	private TextView pb_text;
	private AppManager appManager = AppManager.getManager(this);
	private AppConnectStateRecever appConnectStateRecever;// 连接网络状态监听
	private AppContext appContext;
	private AppMessageRecever appMessageRecever;
	private static final int SEVER_VEFIY_TIME_OUT = 0x01;
	private SocketClient socketClient;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REMOVE_VIEW:
				removeView();
				break;
			case SEVER_VEFIY_TIME_OUT:
				break;

			}

		};
	};

	/**
	 * 服务器验证超时 wanglu 泰得利通
	 */
	protected void serverVieryTimeOut() {

		if (!socketClient.isVerify()) {// 没有通过服务器验证码
			LogUtil.i(LogUtil.LOG_TAG_I, "tab界面验证超时,重新连接");
			connectServer();// 重新连接服务器
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appContext = (AppContext) getApplication();
		instance = this;
		stack = new Stack<StackElement>();
		setContentView(R.layout.main_tab_layout);
		findViews();
		addListeners();
		appManager.addActivity(this);// 加入管理
		init();
		appMessageRecever = appManager.registerAppMessageRecever(this);
		appMessageRecever
				.setAppMessageLinstener(new MainTabAppMessageListener());// 注册消息监听
	}

	private class MainTabAppMessageListener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(Protocol.C_AUTO_CONNECT)) {// 自动重连接口

				if (responseData.isSuccess()) {// 成功

					sendMonitorBroadCast();// 发送监控通知信息

				} else {// 失败

					appManager.redirtToLogin();// 退回失败界面
					UIHelper.showToask(MainTabActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}
				PollingUtils.stopPollingService(MainTabActivity.this,
						AutoConnectPollingService.class,
						AutoConnectPollingService.ACTION);// 停止自动重练
				LogUtil.i(LogUtil.LOG_TAG_AUTO_CONNECT, "重练停止");
				pb_head.setVisibility(View.GONE);
				pb_text.setVisibility(View.GONE);
			} else if (responseData.getFunctionCode().equals(
					Protocol.C_SEVER_VERIFY)) {// 服务器验证返回数据
				socketClient.setVerify(true);// 验证通过
				LogUtil.i(LogUtil.LOG_TAG_I, "tab界面服务器验证成功");
				appManager.startPolling();// 开始心跳
				autoConnect();// 重连

			}

		}

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		unregisterAppConnectStateReceiver();// 取消网络监测
		unregisterReceiver(appMessageRecever);
	}

	/**
	 * 发送监控通知信息
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void sendMonitorBroadCast() {
		String uiname = AppCache.getCache(AppCache.CACHE_UI);

		if (uiname != null) {
			String action = "";
			Intent intent = new Intent();
			if (uiname.equals(MonitorActivity.UI_NAME)) {
				action = MonitorRecever.ACTION_MONTION;
			} else if (uiname.equals(DriveStateActivity.UI_NAME)) {
				action = MonitorRecever.ACTION_DRIVER_STATE;
			} else if (uiname.equals(PortStateActivity.UI_NAME)) {
				action = MonitorRecever.ACTION_PORT_STATE;
			} else if (uiname.equals(CallCommandActivity.UI_NAME)) {
				action = MonitorRecever.ACTION_COMMAND_CALL;
			}

			if (!action.equals("")) {
				intent.setAction(action);
				this.sendBroadcast(intent);// 发送广播通知 监控页面开始监空数据
			}

		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		registAppConnectStateRecever();// 注册网络检测服务器

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
				.setAppConectStateListener(new MainTabConnectStateMessageListener());
	}

	private class MainTabConnectStateMessageListener implements
			AppConectStateListener {

		@Override
		public void connectStateChanged() {
			appManager.stopPolling();// 停止心跳
			pb_head.setVisibility(View.INVISIBLE);
			pb_text.setVisibility(View.VISIBLE);
			pb_text.setText(getString(R.string.title_tip_unconnnect));
			connectServer();// 连接服务器

		}

	}

	/**
	 * 连接服务器
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void connectServer() {

		socketClient = SocketClient.getSocketClient();

		new SynTask(new SynHandler() {

			@Override
			public void onConnectSuccess(String code) {// 连接成功

				// super.onConnectSuccess(code);
				sendSeverVifyData();

			}

			@Override
			public void onFianly() {

			}

		}, appContext).connectServer(socketClient);

	}

	/**
	 * 服务器验证 wanglu 泰得利通
	 */
	private void sendSeverVifyData() {

		LogUtil.i(LogUtil.LOG_TAG_I, "tab界面备界面发送了验证包");
		String apiData = ClientAPI.getApiStr(Protocol.C_SEVER_VERIFY);

		new SynTask(appContext).writeData(apiData, false);

		new Thread(new Runnable() {// 超时处理

					@Override
					public void run() {

						handler.sendEmptyMessageDelayed(SEVER_VEFIY_TIME_OUT,
								2000);

					}
				}).start();

	}

	/**
	 * 重连
	 * 
	 * @author 泰得利通 wanglu
	 */
	protected void autoConnect() {

		pb_head.setVisibility(View.VISIBLE);
		pb_text.setText(getString(R.string.title_tip_auto_connecting));
		PollingUtils.startPollingService(this, AppConfig.AUTO_CONNECT_TIME,
				AutoConnectPollingService.class,
				AutoConnectPollingService.ACTION);
	}

	/**
	 * 停止网络状态监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void unregisterAppConnectStateReceiver() {
		this.unregisterReceiver(appConnectStateRecever);
	}

	/**
	 * 添加事件监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void addListeners() {

		main_tab_rb_setting.setOnClickListener(this);//

		main_tab_rb_test.setOnClickListener(this);

		main_tab_rb_monitor.setOnClickListener(this);// 监视

		main_tab_rb_trouble.setOnClickListener(this);

		main_tab_radiogroup.setOnCheckedChangeListener(this);

	}

	private void init() {

		Intent intent = new Intent(MainTabActivity.this, HomeActivity.class);

		addView(intent);
	}

	/**
	 * 
	 * @author 泰得利通 wanglu
	 * @param intent
	 */
	public void addView(Intent intent) {
		String str = UUID.randomUUID().toString();

		View view = getLocalActivityManager().startActivity(str,
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();

		main_content.addView(view, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		main_content.requestFocus();

		StackElement component = new StackElement(str, view);
		stack.add(component);

		if (main_content.getChildCount() > 1) {
			handler.sendEmptyMessageDelayed(REMOVE_VIEW, 200);// 移除下面的view
		}

	}

	/**
	 * 回退
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	public int back() {
		int size = stack.size();
		StackElement element = null;
		if (size > 1) {
			element = stack.pop();
			getLocalActivityManager().destroyActivity(element.getTag(), true);
			element = stack.peek();

			main_content.removeAllViews();
			main_content.addView(element.getView());
			main_content.requestFocus();

			if (size == 2) {
				flushHomeData();// 刷新首页数据
			}
		} else if (size == 1) {
			element = stack.peek();
			appManager.finishActivity(this);
		}

		return stack.size();
	}

	/**
	 * 刷新首页数据 wanglu 泰得利通
	 */
	private void flushHomeData() {
		Intent intent = new Intent();// 通知楼层数量更新
		intent.setAction(HomeActivity.ACTION_FLUSHDATA);

		sendBroadcast(intent);// 通知广播
	}

	/**
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void clearStack() {
		int size = stack.size();
		if (size > 1) {
			for (int i = 0; i < size - 1; i++) {
				stack.pop();

			}
		}
	}

	/**
	 * 清除栈中所有activity 只保留homeActivity
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void clearAll() {
		int size = stack.size();
		if (size > 1) {
			for (int i = 1; i < size; i++) {
				StackElement element = stack.pop();
				getLocalActivityManager().destroyActivity(element.getTag(),
						true);
			}
		}
	}

	private void removeView() {
		for (int index = 0; index < main_content.getChildCount() - 1; index++) {
			main_content.removeViewAt(index);
		}
	}

	/**
	 * 查找组件
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void findViews() {
		main_content = (FrameLayout) findViewById(R.id.main_content);
		main_tab_radiogroup = (RadioGroup) findViewById(R.id.main_tab_radiogroup);

		main_tab_rb_setting = (RadioButton) findViewById(R.id.main_tab_rb_setting);
		main_tab_rb_test = (RadioButton) findViewById(R.id.main_tab_rb_test);
		main_tab_rb_monitor = (RadioButton) findViewById(R.id.main_tab_rb_monitor);
		main_tab_rb_trouble = (RadioButton) findViewById(R.id.main_tab_rb_trouble);

		pb_head = (ProgressBar) findViewById(R.id.pb_head);
		pb_text = (TextView) findViewById(R.id.pb_text);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		int size = stack.size();
		if (size > 0) {
			StackElement element = stack.peek();
			Activity activity = getLocalActivityManager().getActivity(
					element.getTag());
			return activity.dispatchKeyEvent(event);
		} else {
			return super.dispatchKeyEvent(event);
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int id) {

		// 改变选中的颜色
		for (int index = 0; index < radioGroup.getChildCount(); index++) {

			RadioButton rb = (RadioButton) radioGroup.getChildAt(index);
			if (rb.isChecked()) {
				rb.setTextColor(Color.WHITE);

			} else {
				rb.setTextColor(Color.parseColor("#A0D4FE"));

			}

		}

	}

	@Override
	public void onClick(View view) {

		Intent intent = null;
		switch (view.getId()) {

		case R.id.main_tab_rb_setting:
			intent = new Intent(this, SettingActivity.class);// 设置
			break;
		case R.id.main_tab_rb_test:
			intent = new Intent(this, DeviceTestActivity.class);// 测试
			break;
		case R.id.main_tab_rb_monitor:
			intent = new Intent(this, MonitorActivity.class);// 监控f
			break;
		case R.id.main_tab_rb_trouble:// 故障
			intent = new Intent(this, TroubleActivity.class);
			break;

		}

		if (intent != null) {
			clearAll();
			addView(intent);
		}

	}

}
