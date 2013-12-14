package com.challentec.lmss.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.challentec.lmss.app.AppContext;
import com.challentec.lmss.app.AppManager;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.HandlerMessage;
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
	private AppManager appManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		appContext = (AppContext) getApplication();
		appManager = AppManager.getManager(appContext);
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

		if (appManager.getPollCount() >= 3) {// 发送了三次心跳并没有返回情况
			LogUtil.i(LogUtil.LOG_TAG_BEAT, "心跳超过3次没有返回数据");
			HandlerMessage.handlerUNConnectMessage(appContext);// 发送断开连接重连广播
			
		} else {
			LogUtil.i(LogUtil.LOG_TAG_BEAT, "心跳数据");
			String apiData = ClientAPI.getApiStr(Protocol.C_BEAT);
			synTask.writeData(apiData,true);
			LogUtil.i(LogUtil.LOG_TAG_BEAT_COUNT, "发送心跳前次数为:"+appManager.getPollCount());
			appManager.addPollCount();// 记录心跳次数
			LogUtil.i(LogUtil.LOG_TAG_BEAT_COUNT, "发送心跳后次数为:"+appManager.getPollCount());
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.i(LogUtil.LOG_TAG_BEAT, "POLL destroy");

	}

}