package com.challentec.lmss.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.challentec.lmss.app.AppContext;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClinetAPI;
import com.challentec.lmss.util.LogUtil;
import com.challentec.lmss.util.Protocol;

/**
 * 心跳服务
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class PollingService extends Service {

	public static final String ACTION = "lmss.service.PollingService";

	private AppContext appContext;
	private SynTask synTask;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		appContext = (AppContext) getApplication();
		synTask = new SynTask(appContext);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		polling();

	}

	/**
	 * 发送心跳数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void polling() {
		LogUtil.i(LogUtil.LOG_TAG_BEAT, "心跳数据");
		String apiData = ClinetAPI.getApiStr(Protocol.C_BEAT);
		synTask.writeData(apiData);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.i(LogUtil.LOG_TAG_BEAT, "POLL destroy");

	}

}