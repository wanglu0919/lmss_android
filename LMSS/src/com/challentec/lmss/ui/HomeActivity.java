package com.challentec.lmss.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.dlg.RecoverOutSetDlg;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.recever.AppMessageRecever;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.DataTimeUtil;
import com.challentec.lmss.util.LogUtil;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.LoadProgressView;
import com.challentec.lmss.view.SwitchButton;

public class HomeActivity extends TabContentBaseActivity {

	private SynTask recoverSynTask;
	private RecoverOutSetDlg recoverOutSetDlg;// 恢复出厂设置对话框
	private RecoverOutSetDlg recoverlastOutSetDlg;// 恢复上次出厂设置对话框
	private Drawable imgData_Warring;
	private SharedPreferences sp;
	private FloorNumbeUpdateRecever floorNumbeUpdateRecever;
	private FlushDataRecever flushDataRecever;
	public static final String ACTION_UPDATE_FLOOR_NUMBER = "action_update_floor_nubmer";// 更新楼层数量
	public static final String ACTION_FLUSHDATA = "action_flush_data";// 更新首页数据监听
	private TextView home_tv_floor_no, home_tv_control_no,
			home_tv_out_datetime, home_tv_fate_load, home_tv_home_speed,
			home_tv_control_type, home_tv_floor_num, home_tv_door_num;

	private LoadProgressView home_lp_device;
	private SwitchButton home_sw_recover_out, home_sw_recover_last_out;
	private ProgressDialog pd_recoverdlg;

	private AppMessageRecever appMessageRecever;
	private static final int REVOVERTIMEOUT = 0;

	private Handler recoverHandler = new Handler() {// 恢复出厂设置，和恢复设置值handler处理
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REVOVERTIMEOUT:
				reoverHandlerTimeOut();
				break;

			}

		};
	};

	@Override
	protected void addListeners() {
		super.addListeners();

		home_sw_recover_out
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 恢复出厂设置

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {

						if (isChecked) {
							if (recoverOutSetDlg == null) {
								recoverOutSetDlg = new RecoverOutSetDlg(
										HomeActivity.this,
										new RecoverOutEnableListener(),
										new RecoverOutCancleListener(),
										getString(R.string.device_tv_reply_default));
							}

							recoverOutSetDlg.show();// 显示对话框

						} else {
							String hexData = ClientAPI.getHexApiStr(
									Protocol.C_RECOVER_DEVICE, "00");
							recoverSynTask.writeData(hexData,true);

						}
					}
				});

		home_sw_recover_last_out
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 恢复上次出场设置

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {

						if (isChecked) {

							if (recoverlastOutSetDlg == null) {
								recoverlastOutSetDlg = new RecoverOutSetDlg(
										HomeActivity.this,
										new RecoverLastOutEnableListener(),
										new RecoverLastOutCancleListener(),
										getString(R.string.device_tv_reply_last_default));
							}

							recoverlastOutSetDlg.show();

						} else {
							String hexData = ClientAPI.getHexApiStr(
									Protocol.C_RECOVER_LAST_SET, "00");
							recoverSynTask.writeData(hexData,true);

						}
					}
				});
	}

	/**
	 * 恢复出厂设置值和恢复上次设置值超时处理 wanglu 泰得利通
	 */
	protected void reoverHandlerTimeOut() {

		if (pd_recoverdlg.isShowing()) {
			pd_recoverdlg.dismiss();
			UIHelper.showToask(HomeActivity.this, "恢复失败!");
			home_sw_recover_last_out.setChecked(false);// 设置按钮状态为禁止
			home_sw_recover_out.setChecked(false);// 设置按钮状态为禁止
			unRegistAppMessageReceiver();

		}

	}

	/**
	 * 恢复出厂设置使能
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class RecoverOutEnableListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			startRecoverTimeOut();// 超时监听
			registAppMessageReceiver();// 开始消息监听
			String hexData = ClientAPI.getHexApiStr(Protocol.C_RECOVER_DEVICE,
					"01");
			recoverSynTask.writeData(hexData,true);
			recoverOutSetDlg.dismiss();
			pd_recoverdlg.show();

		}

	}

	/**
	 * 取消回复出场设置
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class RecoverOutCancleListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			recoverOutSetDlg.dismiss();
			home_sw_recover_out.setChecked(false);
		}

	}

	/**
	 * 恢复出厂设置，和恢复上次设置值超时监听 wanglu 泰得利通
	 */
	private void startRecoverTimeOut() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				recoverHandler.sendEmptyMessageDelayed(REVOVERTIMEOUT, 10000);
			}
		}).start();
	}

	/**
	 * 恢复上次出场设置值
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class RecoverLastOutEnableListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			startRecoverTimeOut();// 超时监听
			registAppMessageReceiver();// 开始消息监听
			String hexData = ClientAPI.getHexApiStr(
					Protocol.C_RECOVER_LAST_SET, "01");
			recoverSynTask.writeData(hexData,true);
			recoverlastOutSetDlg.dismiss();
			pd_recoverdlg.show();

		}

	}

	private class RecoverLastOutCancleListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {

			recoverlastOutSetDlg.dismiss();
			home_sw_recover_last_out.setChecked(false);

		}

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_UP) {

				onBack();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private class HomeMessagLinstener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {
			String functionCode = responseData.getFunctionCode();
			if (functionCode.equals(Protocol.C_RECOVER_LAST_SET)) {// 恢复上次出场设置使能获关闭
				if (responseData.isSuccess()) {
					UIHelper.showToask(HomeActivity.this, "恢复成功!");

				} else {
					UIHelper.showToask(HomeActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
					unRegistAppMessageReceiver();// 取消监听
				}
				//unRegistAppMessageReceiver();// 取消监听
				home_sw_recover_last_out.setChecked(false);// 设置按钮状态为禁止
				pd_recoverdlg.dismiss();// 销毁恢复进度对话框
			} else if (functionCode.equals(Protocol.C_RECOVER_DEVICE)) {// 恢复出厂设置

				if (responseData.isSuccess()) {
					UIHelper.showToask(HomeActivity.this, "恢复成功!");

				} else {
					UIHelper.showToask(HomeActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
					unRegistAppMessageReceiver();// 取消监听
				}
			//	unRegistAppMessageReceiver();// 取消监听
				home_sw_recover_out.setChecked(false);// 设置按钮状态为禁止

				pd_recoverdlg.dismiss();// 销毁恢复进度对话框

			} else if (functionCode.equals(Protocol.C_GET_DEVICE_INFO)) {//
				if (responseData.isSuccess()) {// 数据获取成功
					showData(responseData.getData());

				} else {
					UIHelper.showToask(HomeActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}
				unRegistAppMessageReceiver();// 取消监听
				home_lp_device.setVisibility(View.GONE);
			}

		}

	}

	@Override
	protected void initMainView(View mainView) {
		registAppMessageReceiver();// 注册消息监听
		home_tv_floor_no = (TextView) mainView
				.findViewById(R.id.home_tv_floor_no);// 电梯编号
		home_tv_control_no = (TextView) mainView
				.findViewById(R.id.home_tv_control_no);// 控制柜信息
		home_tv_out_datetime = (TextView) mainView
				.findViewById(R.id.home_tv_out_datetime);// 出场时间
		home_tv_fate_load = (TextView) mainView
				.findViewById(R.id.home_tv_fate_load);// 额定载重
		home_tv_home_speed = (TextView) mainView
				.findViewById(R.id.home_tv_home_speed);// 额定速度
		home_tv_control_type = (TextView) mainView
				.findViewById(R.id.home_tv_control_type);// 控制方式
		home_tv_floor_num = (TextView) mainView
				.findViewById(R.id.home_tv_floor_num);// 楼层数量
		home_tv_door_num = (TextView) mainView
				.findViewById(R.id.home_tv_door_num);// 门机数量
		home_lp_device = (LoadProgressView) findViewById(R.id.home_lp_device);
		home_sw_recover_out = (SwitchButton) findViewById(R.id.home_sw_recover_out);// 恢复出厂设置
		home_sw_recover_last_out = (SwitchButton) findViewById(R.id.home_sw_recover_last_out);// 恢复上次出场设置
		imgData_Warring = getResources().getDrawable(
				R.drawable.item_data_waring);
		imgData_Warring.setBounds(0, 0, imgData_Warring.getMinimumWidth(),
				imgData_Warring.getMinimumHeight());

		loadData(false);

		initSynTask();// 初始化异步任务

		registerFloorUpdateRecever();//注册楼层更新广播监听
		registerFlushDataRecever();//注册刷新数据广播监听
		pd_recoverdlg = new ProgressDialog(this);
		pd_recoverdlg.setMessage(getString(R.string.tip_msg_pb_revovering));
		pd_recoverdlg.setCancelable(false);

	}

	/**
	 * 注册消息监听 wanglu 泰得利通
	 */
	private void registAppMessageReceiver() {
		if (appMessageRecever != null) {
			unRegistAppMessageReceiver();// 先取消，再重新注册
		}
		appMessageRecever = appManager.registerAppMessageRecever(this);
		appMessageRecever.setAppMessageLinstener(new HomeMessagLinstener());
	}

	/**
	 * 取消消息监听 wanglu 泰得利通
	 */
	private void unRegistAppMessageReceiver() {

		if (appMessageRecever != null) {
			unregisterReceiver(appMessageRecever);
			appMessageRecever = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegistAppMessageReceiver();
	}

	/**
	 * 注册楼层更改广播监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void registerFloorUpdateRecever() {
		floorNumbeUpdateRecever = new FloorNumbeUpdateRecever();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_UPDATE_FLOOR_NUMBER);
		registerReceiver(floorNumbeUpdateRecever, filter);
	}
	
	/**
	 * 注册楼层更改广播监听
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void registerFlushDataRecever() {
		flushDataRecever = new FlushDataRecever();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_FLUSHDATA);
		registerReceiver(flushDataRecever, filter);
	}
	
	

	@Override
	protected void onStop() {

		super.onStop();
		unregisterReceiver(floorNumbeUpdateRecever);// 取消楼层更新监听
	}

	private void initSynTask() {
		recoverSynTask = new SynTask(new SynHandler(), appContext);// 初始化异步任务

	}

	/**
	 * 加载数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void loadData(boolean isFlush) {
		String apiData = ClientAPI.getApiStr(Protocol.C_GET_DEVICE_INFO);
		SynTask synTask = new SynTask(new SynHandler(), appContext);
		synTask.uiLog(Protocol.UI_DEVICE_DISPLAY);// 日志记录
		if(!isFlush){
			home_lp_device.setVisibility(View.GONE);
		}
		
		synTask.writeData(apiData,true);

	}

	/**
	 * 显示数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void showData(String data) {

		if (data.length() != 40) {
			UIHelper.showToask(this, AppTipMessage
					.getResouceStringId(AppTipMessage.DATA_FORMAT_ERROR));// 格式有误
			return;
		}
		String floorNo = data.substring(0, 8);

		int intFloorNo = Integer.parseInt(floorNo);
		if (intFloorNo < 20130001 || intFloorNo > 20639999) {
			showErro(home_tv_floor_no);
		}
		home_tv_floor_no.setText(floorNo);// 电梯编号

		String control_no = data.substring(8, 16);
		int intControl_no = Integer.parseInt(control_no);
		if (intControl_no < 20130001 || intControl_no > 20639999) {
			showErro(home_tv_control_no);
		}

		home_tv_control_no.setText(control_no);// 控制柜编号

		String year = data.substring(16, 20);
		String month = data.substring(20, 22);
		String day = data.substring(22, 24);
		home_tv_out_datetime.setText(year + "." + month + "." + day);// 出场日期

		if (!DataTimeUtil.checkDataTime(Integer.parseInt(year),
				Integer.parseInt(month), Integer.parseInt(day))) {
			showErro(home_tv_out_datetime);
		}

		int fate_load = DataPaseUtil.getDataInt(data, 24, 28);
		home_tv_fate_load.setText(fate_load + getString(R.string.unit_load));// 额定载重
		if (fate_load < 400 || fate_load > 5000) {// 范围有误
			showErro(home_tv_fate_load);// 显示错误图标
		}

		String fspeed = DataPaseUtil.getDataFloat(data, 28, 32, 2, 1);
		float speed = Float.valueOf(fspeed);
		home_tv_home_speed.setText(DataPaseUtil
				.getDataFloat(data, 28, 32, 2, 1)
				+ getString(R.string.unit_speed));// 速度
		if (speed < 0.4 || speed > 10 || (fspeed.split("\\.")[1].length() > 1)) {
			showErro(home_tv_home_speed);// 显示错误图标
		}

		String controlType = data.substring(32, 34);

		home_tv_control_type.setText(getControlType(controlType));// 控制方式
		int floorNum = DataPaseUtil.getDataInt(data, 34, 36);// 楼层数量
		home_tv_floor_num.setText(floorNum + ""
				+ getString(R.string.unit_floor));// 楼层数量

		if (floorNum < 2 || floorNum > 40) {
			showErro(home_tv_floor_num);// 显示错误图标
		}

		int homeDoor = DataPaseUtil.getDataInt(data, 36, 38);
		home_tv_door_num.setText(homeDoor + "" + getString(R.string.unit_desk));
		if (homeDoor < 1 || homeDoor > 3) {
			showErro(home_tv_door_num);// 显示错误图标
		}

		String recover = data.substring(38, 40);
		int recoverInt = DataPaseUtil.hexStrToInt(recover);
		int recoverOut = recoverInt & 1;// 取最低位
		int revoverLast = recoverInt >> 1 & 1;// 取低2位

		setRecoverOUtChecked(recoverOut);

		setLastRecoverOUtChecked(revoverLast);

		// 保存楼层数量
		sp = AppConfig.getAppConfig(appContext).getSharedPreferences();
		Editor ed = sp.edit();

		ed.putInt(AppConfig.FLOOR_NUM_KEY, floorNum);
		ed.commit();

	}

	/**
	 * 设置恢复出厂设置禁止或运行
	 * 
	 * @author 泰得利通 wanglu
	 * @param value
	 */
	private void setRecoverOUtChecked(int value) {
		home_sw_recover_out.setChecked(false);
		/*
		 * if (value == 0) { home_sw_recover_out.setChecked(false); } else if
		 * (value == 1) { home_sw_recover_out.setChecked(true); }
		 */
	}

	/**
	 * 设置恢复上次出厂设置禁止或运行
	 * 
	 * @author 泰得利通 wanglu
	 * @param value
	 */
	private void setLastRecoverOUtChecked(int value) {
		home_sw_recover_last_out.setChecked(false);
		/*
		 * if (value == 0) { home_sw_recover_last_out.setChecked(false); } else
		 * if (value == 1) { home_sw_recover_last_out.setChecked(true); }
		 */
	}

	private String getControlType(String value) {

		if (value.equals("00")) {
			return getString(R.string.basic_check_control_type_jx);// 集选
		} else if (value.equals("01")) {
			return getString(R.string.basic_check_control_type_qc);// 群控
		} else if (value.equals("02")) {
			return getString(R.string.basic_check_control_type_bl);// 并联
		} else {
			showErro(home_tv_control_type);
			return "Error";
		}
	}

	@Override
	protected CharSequence getTitleText() {

		return getResources().getString(R.string.ui_title_device_info);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_home_layout;
	}

	/**
	 * 显示错误图标
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void showErro(TextView textView) {
		textView.setCompoundDrawables(null, null, imgData_Warring, null);// 显示数据错误提示信息
	}

	/**
	 * 楼层更新 广播监听
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class FloorNumbeUpdateRecever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (sp != null) {
				home_tv_floor_num.setText(sp.getInt(AppConfig.FLOOR_NUM_KEY, 0)
						+ getString(R.string.unit_floor));
			}
		}

	}
	
	/**
	 * 刷新首页数据 wanglu 泰得利通
	 */
	private void flushHomeData() {
		Intent intent = new Intent();// 通知楼层数量更新
		intent.setAction(ACTION_FLUSHDATA);

		sendBroadcast(intent);// 通知广播
	}
	
	/**
	 * 刷新数据监听
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class FlushDataRecever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			registAppMessageReceiver();
			loadData(true);//刷新数据
			
			LogUtil.i(LogUtil.LOG_TAG_I, "刷新首页数据");
		}

	}

}
