package com.challentec.lmss.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
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
import com.challentec.lmss.util.ClinetAPI;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.util.UnitUtil;
import com.challentec.lmss.view.LoadProgressView;

/**
 * 监控activity
 * 
 * @author 泰得利通 wanglu
 * 
 * 
 */
public class MonitorActivity extends TabContentBaseActivity {

	private RelativeLayout monitor_rl_drive_state, monitor_rl_port_state;
	private LoadProgressView monitor_pb;
	private static final int DATA_LEN = 30;
	private TextView mon_tv_control_type, mon_current_speed, mon_tv_give_speed,
			mon_tv_current_floor, mon_tv_current_height, mon_tv_current_dir,
			mon_tv_total_time, mon_tv_total_count;
	private Drawable imgData_Warring;
	private MonitorRecever monitorRecever;
	public static String  UI_NAME="UI_MONITOR";
	@Override
	protected void addListeners() {

		super.addListeners();
		monitor_rl_drive_state.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopMontitor();//停止监控
				Intent intent = new Intent(MonitorActivity.this,
						DriveStateActivity.class);
				MainTabActivity.instance.addView(intent);

			}
		});

		monitor_rl_port_state.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				stopMontitor();//停止监控
				Intent intent = new Intent(MonitorActivity.this,
						PortStateActivity.class);
				MainTabActivity.instance.addView(intent);
			}
		});
	}

	@Override
	protected void initMainView(View mainView) {
		monitor_rl_drive_state = (RelativeLayout) mainView
				.findViewById(R.id.monitor_rl_drive_state);// 驱动状态
		monitor_rl_port_state = (RelativeLayout) mainView
				.findViewById(R.id.monitor_rl_port_state);// 端口状态
		monitor_pb = (LoadProgressView) mainView.findViewById(R.id.monitor_pb);// 进度条

		mon_tv_control_type = (TextView) mainView
				.findViewById(R.id.mon_tv_control_type);
		mon_current_speed = (TextView) mainView
				.findViewById(R.id.mon_tv_current_speed);
		mon_tv_give_speed = (TextView) mainView
				.findViewById(R.id.mon_tv_give_speed);
		mon_tv_current_floor = (TextView) mainView
				.findViewById(R.id.mon_tv_current_floor);
		mon_tv_current_height = (TextView) mainView
				.findViewById(R.id.mon_tv_current_height);
		mon_tv_current_dir = (TextView) mainView
				.findViewById(R.id.mon_tv_current_dir);
		mon_tv_total_time = (TextView) mainView
				.findViewById(R.id.mon_tv_total_time);
		mon_tv_total_count = (TextView) mainView
				.findViewById(R.id.mon_tv_total_count);
		imgData_Warring = getResources().getDrawable(
				R.drawable.item_data_waring);
		imgData_Warring.setBounds(0, 0, imgData_Warring.getMinimumWidth(),
				imgData_Warring.getMinimumHeight());
		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new MonitorMessageListner());

		startMontitor();// 开始监控
	}

	
	/**
	 * 注册监控
	 *@author 泰得利通 wanglu
	 */
	private void registerMonitorRecever(){
		
		monitorRecever=new MonitorRecever();
		monitorRecever.setMonitorListener(new MonListener());
		IntentFilter filter = new IntentFilter();
		filter.addAction(MonitorRecever.ACTION_MONTION);
		this.registerReceiver(monitorRecever, filter);
		
	}
	
	/**
	 * 监控
	 * @author 泰得利通 wanglu
	 *
	 */
	private class MonListener implements MonitorListener{

		@Override
		public void monitor(Intent intent) {
			
			startMontitor();//开始监控
			
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		registerMonitorRecever();//注册监空广播监听
		AppCache.cache(AppCache.CACHE_UI, UI_NAME);//存储当前UI
	}
	private class MonitorMessageListner implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(Protocol.M_START_MON)) {// 开始监听
				if (responseData.isSuccess()) {
					showMonitorData(responseData);

				} else {
					UIHelper.showToask(MonitorActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}

				monitor_pb.setVisibility(View.GONE);

			} else if (responseData.getFunctionCode()
					.equals(Protocol.M_END_MON)) {// 结束监听
				if (responseData.isSuccess()) {
					// UIHelper.showToask(appContext, "结束监控成功");
				}

			}
		}

	}

	/**
	 * 开始监控
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void startMontitor() {
		monitor_pb.setVisibility(View.VISIBLE);
		String apiData = ClinetAPI.getApiStr(Protocol.M_START_MON);
		SynTask synTask = new SynTask(new SynHandler(), appContext);
		synTask.writeData(apiData);
		synTask.uiLog(Protocol.UI_MONITOR_HOME);// 记录操作

	}

	/**
	 * 显示监控数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param responseData
	 */
	protected void showMonitorData(ResponseData responseData) {
		if (responseData.getData().length() != DATA_LEN) {
			UIHelper.showToask(this, AppTipMessage
					.getResouceStringId(AppTipMessage.DATA_FORMAT_ERROR));// 格式有误
			return;
		}

		String hexData = responseData.getData();

		mon_tv_control_type.setText(getControlType(DataPaseUtil.getDataInt(
				hexData, 0, 2)));// 电梯控制模式
		// 电梯当前速度
		String floatCurrentSpeed = DataPaseUtil.getDataFloat(hexData, 2, 6, 2,
				1);

		float currentSpeed = Float.valueOf(floatCurrentSpeed);
		mon_current_speed.setText(floatCurrentSpeed
				+ UnitUtil.getUnit(this, UnitUtil.SPEED));
		if (currentSpeed < 0.3 || currentSpeed > 10
				|| (floatCurrentSpeed.split("\\.")[1].length() > 1)) {// 检查当前速度
			showErro(mon_current_speed);
		}

		// 当前给定速度
		String floatGiveSpeed = DataPaseUtil.getDataFloat(hexData, 6, 10, 2, 1);
		mon_tv_give_speed.setText(floatGiveSpeed
				+ UnitUtil.getUnit(this, UnitUtil.SPEED));// 当前给定速度
		float giveSpeed = Float.valueOf(DataPaseUtil.getDataFloat(hexData, 6,
				10, 2, 1));

		if (giveSpeed < 0.3 || giveSpeed > 10
				|| (floatGiveSpeed.split("\\.")[1].length() > 1)) {// 检查给定速度
			showErro(mon_tv_give_speed);
		}
		// 电梯当前层楼
		int currentFloor = DataPaseUtil.getDataInt(hexData, 10, 12);
		mon_tv_current_floor.setText(currentFloor
				+ UnitUtil.getUnit(this, UnitUtil.FLOOR));

		if (currentFloor < 2 || currentFloor > 40) {
			showErro(mon_tv_current_floor);
		}
		// 电梯当前高度
		int currentHinght = DataPaseUtil.getDataInt(hexData, 12, 14);
		mon_tv_current_height.setText(currentHinght + ""
				+ UnitUtil.getUnit(this, UnitUtil.MILL));
		if (currentHinght < 3 || currentFloor > 200) {
			showErro(mon_tv_current_height);
		}
		// 电梯运行方向
		mon_tv_current_dir.setText(getRunDir(DataPaseUtil.getDataInt(hexData,
				14, 16)));
		// 总计运行时间
		int totalRunTime = DataPaseUtil.getDataInt(hexData, 16, 22);
		mon_tv_total_time.setText(totalRunTime
				+ UnitUtil.getUnit(this, UnitUtil.HOUR));

		if (totalRunTime < 0 || totalRunTime > 150000) {
			showErro(mon_tv_total_time);
		}

		mon_tv_total_count.setText(DataPaseUtil.getDataInt(hexData, 22, 30)
				+ UnitUtil.getUnit(this, UnitUtil.TIME));// 总计运行次数
	}

	private CharSequence getRunDir(int dataInt) {
		int dirData = dataInt & 1;
		if (dirData == 1) {
			return getString(R.string.monitor_tv_run_dir_up);// 上行
		} else if (dirData == 0) {
			return getString(R.string.monitor_tv_run_dir_down);// 下行
		} else {
			showErro(mon_tv_current_dir);
			return "Error";
		}

	}

	/**
	 * 检修/集选/并联 控制模式
	 * 
	 */

	private CharSequence getControlType(int dataInt) {
		String contype = "Error";
		switch (dataInt) {

		case 0:
			contype = getString(R.string.monitor_tv_control_model_reparir);// 检修
			break;
		case 1:
			contype = getString(R.string.monitor_tv_control_model_jx);// 集选
			break;
		case 2:
			contype = getString(R.string.monitor_tv_control_model_bl);// 并联
			break;
		default:
			showErro(mon_tv_control_type);
		}

		return contype;
	}

	@Override
	protected CharSequence getTitleText() {
		return getResources().getString(R.string.ui_title_monitor);
	}

	@Override
	protected int getMainViewLayoutId() {
		return R.layout.activity_monitor_layout;
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		stopMontitor();
		unregisterReceiver(monitorRecever);
	}

	/**
	 * 结束监控
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void stopMontitor() {

		String apiData = ClinetAPI.getApiStr(Protocol.M_END_MON);
		new SynTask(new SynHandler() {

			@Override
			protected void onToast(String code) {

			}

			@Override
			public void onFianly() {

			}

		}, appContext).writeData(apiData);

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
