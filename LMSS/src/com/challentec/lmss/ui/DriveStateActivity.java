package com.challentec.lmss.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.challentec.lmss.app.AppCache;
import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.listener.MonitorListener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.recever.MonitorRecever;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.util.UnitUtil;
import com.challentec.lmss.view.LoadProgressView;

/**
 * 驱动状态
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class DriveStateActivity extends TabContentBaseActivity {
	private LoadProgressView driver_state_lp;
	private static int DATA_LEN = 22;
	private TextView driver_tv_nomal, driver_tv_run, driver_tv_out_voltage,
			driver_tv_out_ele, driver_tv_rate, driver_tv_commad,
			driver_tv_mother_voltage, driver_tv_model_tp;
	private Drawable imgData_Warring;
	private MonitorRecever monitorRecever;
	public static final String  UI_NAME="DRIVER";
	@Override
	protected void onDestroy() {

		super.onDestroy();
		unregisterReceiver(monitorRecever);// 取消驱动状态广播监听
		
	}

	/**
	 * 停止驱动状态
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void stopDriveMonitor() {

		String apiData = ClientAPI.getApiStr(Protocol.M_END_DRIVE);

		SynTask synTask = new SynTask(new SynHandler(), appContext);

		synTask.writeData(apiData);// 停止监控
	}

	

	@Override
	protected void initMainView(View mainView) {

		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new DriverStateListener());
		driver_state_lp = (LoadProgressView) mainView
				.findViewById(R.id.driver_state_lp);
		driver_tv_nomal = (TextView) mainView
				.findViewById(R.id.driver_tv_nomal);
		driver_tv_run = (TextView) mainView.findViewById(R.id.driver_tv_run);
		driver_tv_out_voltage = (TextView) mainView
				.findViewById(R.id.driver_tv_out_voltage);
		driver_tv_out_ele = (TextView) mainView
				.findViewById(R.id.driver_tv_out_ele);
		driver_tv_rate = (TextView) mainView.findViewById(R.id.driver_tv_rate);
		driver_tv_commad = (TextView) mainView
				.findViewById(R.id.driver_tv_commad);
		driver_tv_mother_voltage = (TextView) mainView
				.findViewById(R.id.driver_tv_mother_voltage);
		driver_tv_model_tp = (TextView) mainView
				.findViewById(R.id.driver_tv_model_tp);
		imgData_Warring = getResources().getDrawable(
				R.drawable.item_data_waring);
		imgData_Warring.setBounds(0, 0, imgData_Warring.getMinimumWidth(),
				imgData_Warring.getMinimumHeight());
		startDriverState();
	}

	@Override
	protected void onBack() {
		stopDriveMonitor();
		Intent intent = new Intent();
		intent.setAction(MonitorRecever.ACTION_MONTION);
		this.sendBroadcast(intent);// 发送广播通知 监控页面开始监空数据
		AppCache.cache(AppCache.CACHE_UI, MonitorActivity.UI_NAME);
		super.onBack();
	}

	/**
	 * 注册监控
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void registerDriverMonitorRecever() {

		monitorRecever = new MonitorRecever();
		monitorRecever.setMonitorListener(new DriverMonitorListener());
		IntentFilter filter = new IntentFilter();
		filter.addAction(MonitorRecever.ACTION_DRIVER_STATE);
		this.registerReceiver(monitorRecever, filter);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		AppCache.cache(AppCache.CACHE_UI, UI_NAME);//存储当前UI
		registerDriverMonitorRecever();
	}

	private class DriverMonitorListener implements MonitorListener {

		@Override
		public void monitor(Intent intent) {
			startDriverState();// 开始驱动状态

		}

	}

	private class DriverStateListener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(Protocol.M_START_DRIVE)) {// 获取驱动状态命令

				if (responseData.isSuccess()) {

					showDriverData(responseData);

				} else {
					UIHelper.showToask(DriveStateActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}

				driver_state_lp.setVisibility(View.GONE);
			} else if (responseData.getFunctionCode().equals(
					Protocol.M_END_DRIVE)) {// 停止驱动状态监控
				if (responseData.isSuccess()) {//

				}
			}
		}

	}

	/**
	 * 开始驱动状态
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void startDriverState() {
		driver_state_lp.setVisibility(View.VISIBLE);
		String apiData = ClientAPI.getApiStr(Protocol.M_START_DRIVE);

		SynTask synTask = new SynTask(new SynHandler(), appContext);

		synTask.writeData(apiData);// 开始监控
		synTask.uiLog(Protocol.UI_DRIVER_STATE);// 驱动状态

	}

	/**
	 * 显示驱动状态数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void showDriverData(ResponseData responseData) {
		String driverData = responseData.getData();
		if (driverData.length() != DATA_LEN) {
			UIHelper.showToask(this, AppTipMessage
					.getResouceStringId(AppTipMessage.DATA_FORMAT_ERROR));// 格式有误
			return;
		}

		driver_tv_nomal.setText((DataPaseUtil.getDataInt(driverData, 0, 2) & 1)
				+ "");// 取第一位 驱动正常

		driver_tv_run
				.setText((DataPaseUtil.getDataInt(driverData, 0, 2) >> 1 & 1)
						+ "");// 驱动运行

		int out_voltage = DataPaseUtil.getDataInt(driverData, 2, 6);
		driver_tv_out_voltage.setText(out_voltage
				+ UnitUtil.getUnit(this, UnitUtil.FT));// 输出电压
		if (out_voltage < 0 || out_voltage > 500) {
			showErro(driver_tv_out_voltage);
		}

		int out_ele = DataPaseUtil.getDataInt(driverData, 6, 10);
		driver_tv_out_ele
				.setText(out_ele + UnitUtil.getUnit(this, UnitUtil.AP));// 电流
		if (out_ele < 0 || out_ele > 500) {
			showErro(driver_tv_out_ele);
		}

		int tv_rate = DataPaseUtil.getDataInt(driverData, 10, 12);
		driver_tv_rate.setText(tv_rate + UnitUtil.getUnit(this, UnitUtil.HZ));// 输出频率
		if (tv_rate < 0 || tv_rate > 99) {
			showErro(driver_tv_rate);
		}

		int command = DataPaseUtil.getDataInt(driverData, 12, 16);
		driver_tv_commad.setText(command + "");
		if (command < 0 || command > 5000) {
			showErro(driver_tv_commad);
		}
		int mother_voltage = DataPaseUtil.getDataInt(driverData, 16, 20);
		driver_tv_mother_voltage.setText(mother_voltage
				+ UnitUtil.getUnit(this, UnitUtil.FT));

		if (mother_voltage < 0 || mother_voltage > 1000) {
			showErro(driver_tv_mother_voltage);
		}

		int model_tp = DataPaseUtil.getDataInt(driverData, 20, 22);
		driver_tv_model_tp.setText(model_tp
				+ UnitUtil.getUnit(this, UnitUtil.WD));// 模块温度

		if (model_tp < 0 || model_tp > 120) {
			showErro(driver_tv_model_tp);
		}

	}

	@Override
	protected CharSequence getTitleText() {
		return getResources().getString(R.string.ui_title_monitor_drive_state);
	}

	@Override
	protected int getMainViewLayoutId() {
		return R.layout.activity_monitor_drive_state_layout;
	}

	/**
	 * 显示错误图标
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void showErro(TextView textView) {
		textView.setCompoundDrawables(null, null, imgData_Warring, null);// 显示数据错误提示信息
	}

}
