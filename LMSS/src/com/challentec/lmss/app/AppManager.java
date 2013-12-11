package com.challentec.lmss.app;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

import com.challentec.lmss.net.SocketClient;
import com.challentec.lmss.recever.AppConnectStateRecever;
import com.challentec.lmss.recever.AppMessageRecever;
import com.challentec.lmss.service.AutoConnectPollingService;
import com.challentec.lmss.service.LoginPollingService;
import com.challentec.lmss.service.PollingService;
import com.challentec.lmss.util.LogUtil;
import com.challentec.lmss.util.PollingUtils;

/**
 * 
 * @author 泰得利通 wanglu app管理类 对activity进行管理，并且开始心跳，停止心跳，广播接收着管理
 */
public class AppManager {

	public static AppManager instance;
	private Context context;
	public static Stack<Activity> activityStack;// activity栈

	private int pollCount;//心跳数量
	
	public void setPollCount(int pollCount) {
		this.pollCount = pollCount;
	}
	public void addPollCount(){
		pollCount++;
	}
	public int getPollCount(){
		return pollCount;
	}
	
	public void reduceCount(){
		pollCount--;
	}
	private AppManager() {
	};

	public synchronized static AppManager getManager(Context context) {

		if (instance == null) {
			instance = new AppManager();
			instance.context = context;
		}
		return instance;
	}

	/**
	 * 
	 * @author 泰得利通 wanglu
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获得当前activity
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	public Activity getCurrentActivity() {
		return activityStack.lastElement();
	}

	/**
	 * finish堆栈最上面的activty
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 
	 * @author 泰得利通 wanglu
	 * @param cls
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有activity
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 跳转到登陆
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void redirtToLogin() {
		for (int i = 1, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}

	}

	/**
	 * 结束activity
	 * 
	 * @author 泰得利通 wanglu
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 退出APP
	 * 
	 * @author 泰得利通 wanglu
	 * @param context
	 */
	public void appExit(Context context) {
		try {
			stopPolling();// 停止心跳
			PollingUtils.stopPollingService(context, LoginPollingService.class, LoginPollingService.ACTION);
			PollingUtils.stopPollingService(context, AutoConnectPollingService.class, AutoConnectPollingService.ACTION);
			SocketClient.getSocketClient().dispose();//释放连接资源
			
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取软件版本号
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	public String getAppVersion() {

		PackageManager pm = context.getPackageManager();

		PackageInfo packageInfo;
		try {
			packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return context.getString(R.string.tip_msg_unknow_version);

	}

	/**
	 * 获取手机IMEI
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	public String getIMEI() {

		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

	}

	/**
	 * 注册消息接收类
	 * 
	 * @author 泰得利通 wanglu
	 * @param functionCode
	 *            功能代码
	 * @return
	 */
	public AppMessageRecever registerAppMessageRecever(Context context) {

		AppMessageRecever appMessageRecever = new AppMessageRecever();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppMessageRecever.ACTION_STRING);
		context.registerReceiver(appMessageRecever, filter);
		return appMessageRecever;

	}

	/**
	 * 注册APP连接状态监听
	 * 
	 * @author 泰得利通 wanglu
	 * @param context
	 * @return
	 */
	public AppConnectStateRecever registerAppConnectStateRecever(Context context) {

		AppConnectStateRecever appConnectStateRecever = new AppConnectStateRecever();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConnectStateRecever.ACTION_STRING);
		context.registerReceiver(appConnectStateRecever, filter);
		return appConnectStateRecever;

	}

	/**
	 * 启动心跳
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void startPolling() {
		LogUtil.i(LogUtil.LOG_TAG_BEAT, "开始心跳");
		PollingUtils.startPollingService(context, AppConfig.POLL_INTERVAL_TIME,
				PollingService.class, PollingService.ACTION);
	}

	/**
	 * 停止心跳
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void stopPolling() {
		LogUtil.i(LogUtil.LOG_TAG_BEAT, "停止心跳");
		PollingUtils.stopPollingService(context, PollingService.class,
				PollingService.ACTION);

	}

}
