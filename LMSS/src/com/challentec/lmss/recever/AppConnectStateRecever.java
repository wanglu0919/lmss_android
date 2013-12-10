package com.challentec.lmss.recever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.listener.AppConectStateListener;

/**
 * app Sokect连接状态广播接收着
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class AppConnectStateRecever extends BroadcastReceiver {

	private AppConectStateListener appConectStateListener;// 状态监听
	public static final String ACTION_STRING = "LMSS_CONNECT_STATE_MESSAGE";

	public void setAppConectStateListener(
			AppConectStateListener appConectStateListener) {
		this.appConectStateListener = appConectStateListener;
	}

	public AppConnectStateRecever() {
	};

	public AppConnectStateRecever(AppConectStateListener appConectStateListener) {
		this.appConectStateListener = appConectStateListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		saveServerHandTime(context);
		appConectStateListener.connectStateChanged();
	}

	/**
	 * 存储服务器挂的时间点
	 * 
	 * @author 泰得利通 wanglu
	 * @param context
	 */
	private void saveServerHandTime(Context context) {
		SharedPreferences sp = AppConfig.getAppConfig(context)
				.getSharedPreferences();
		Editor ed = sp.edit();
		ed.putLong(AppConfig.SERVER__HANG_TIME_KEY, System.currentTimeMillis());
		ed.commit();
	}

}
