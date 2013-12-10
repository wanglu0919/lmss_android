package com.challentec.lmss.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.AppContext;
import com.challentec.lmss.app.AppManager;
import com.challentec.lmss.app.R;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClinetAPI;
import com.challentec.lmss.util.LogUtil;
import com.challentec.lmss.util.PollingUtils;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;

/**
 * 自动重连服务
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class AutoConnectPollingService extends Service {

	public static final String ACTION = "lmss.service.auton_connnect";

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
		autoConnect();

	}

	/**
	 * 重练数据跳数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void autoConnect() {
		LogUtil.i(LogUtil.LOG_TAG_AUTO_CONNECT, "发送一次重练");
		
		SharedPreferences sp = AppConfig.getAppConfig(appContext)
				.getSharedPreferences();
		long serverHandTime=sp.getLong(AppConfig.SERVER__HANG_TIME_KEY, 0);
		
		if(serverHandTime!=0){
			long nowTime=System.currentTimeMillis();
			
			long betweenMinutes=(nowTime-serverHandTime)/(1000*60);//计算相差分钟
			
			if(betweenMinutes<30){//30分钟内
				String tel = sp.getString(AppConfig.TELE_PHONE_NUM_KEY, "");
				String deviceNo = sp.getString(AppConfig.DEVICE_NUMBER_KEY, "");// 设备授权码
				String apiData = ClinetAPI.getApiStr(Protocol.C_AUTO_CONNECT, tel + "|"
						+ deviceNo+"|"+AppManager.getManager(appContext).getIMEI());//手机号+设备授权码+IME号
				synTask.writeData(apiData);
			}else{
				
				PollingUtils.stopPollingService(appContext, AutoConnectPollingService.class, ACTION);//停止服务
				UIHelper.showToask(appContext, getString(R.string.tip_msg_auto_connect_time_out));//重练超时
				AppManager.getManager(appContext).redirtToLogin();//退回登陆
			}
		}else{
			
			PollingUtils.stopPollingService(appContext, AutoConnectPollingService.class, ACTION);//停止服务
		}
		
	

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.i(LogUtil.LOG_TAG_AUTO_CONNECT, "重练服务注销");

	}
	
	


}