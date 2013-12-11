package com.challentec.lmss.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.AppContext;
import com.challentec.lmss.app.AppManager;
import com.challentec.lmss.app.R;
import com.challentec.lmss.engine.GPSInfoService;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.LogUtil;
import com.challentec.lmss.util.PollingUtils;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;

/**
 * 轮询登陆服务,在，选择设备界面，如果网络断开时，再建立socket连接后，重新发送登录数据包
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class LoginPollingService extends Service {

	public static final String ACTION = "lmss.service.auton_login";

	private AppContext appContext;
	private SynTask synTask;
	private SharedPreferences sp;

	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		appContext = (AppContext) getApplication();
		synTask = new SynTask(appContext);
		sp = AppConfig.getAppConfig(appContext).getSharedPreferences();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		autoConnect();

	}

	/**
	 * 发送重练数据 wanglu 泰得利通
	 * 
	 * @param location
	 */
	private void sendConnectData(String location) {
		String gpsflag = "1";

		if (appContext.isGPSOPen()) {
			gpsflag = "0";
		}
		String tele = sp.getString(AppConfig.TELE_PHONE_NUM_KEY, "");
		String vecode = sp.getString(AppConfig.VECODE_KEY, "");
		String apiData = ClientAPI.getApiStr(Protocol.C_LOGIN, tele + "|"
				+ vecode + "|" + AppManager.getManager(appContext).getIMEI()
				+ "|" + location + "|" + gpsflag);// 手机号+验证码+IME号+定位信息+定位服务是否开启

		synTask.writeData(apiData);
	}

	/**
	 * 重练数据跳数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void autoConnect() {
		LogUtil.i(LogUtil.LOG_TAG_AUTO_CONNECT, "发送登陆请求一次");

		long serverHandTime = sp.getLong(AppConfig.SERVER__HANG_TIME_KEY, 0);

		if (serverHandTime != 0) {
			long nowTime = System.currentTimeMillis();

			long betweenMinutes = (nowTime - serverHandTime) / (1000 * 60);// 计算相差分钟

			if (betweenMinutes < 30) {

				String locaton = sp.getString(AppConfig.LOCATION_KEY, "0,0");

				sendConnectData(locaton);

			} else {
				PollingUtils.stopPollingService(appContext,
						LoginPollingService.class, ACTION);// 停止服务
				AppManager.getManager(appContext).redirtToLogin();// 退回登陆
				UIHelper.showToask(appContext,
						getString(R.string.tip_msg_auto_connect_time_out));// 重练超时
			}
		} else {

			PollingUtils.stopPollingService(appContext,
					LoginPollingService.class, ACTION);// 停止服务
		}

	}

	

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.i(LogUtil.LOG_TAG_AUTO_CONNECT, "自动登录服务注销");

	}

}