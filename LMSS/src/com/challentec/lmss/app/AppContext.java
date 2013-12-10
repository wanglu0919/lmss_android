package com.challentec.lmss.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.challentec.lmss.util.StringUtils;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据 app上下文对象
 * 
 * @author wanglu
 */
@SuppressLint("DefaultLocale")
public class AppContext extends Application {

	public static final int NETTYPE_WIFI = 0x01;// 无线网
	public static final int NETTYPE_CMWAP = 0x02;// CMWap网
	public static final int NETTYPE_CMNET = 0x03;

	@Override
	public void onCreate() {
		super.onCreate();

	}

	/**
	 * 检查当前网络状态
	 * 
	 * @return true 已连接false未连接
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/**
	 * 判断基站定位开否
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public boolean isGPSOPen() {

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）

		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		/*
		 * 通过WLAN或移动网络(3G/2G
		 * )确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		 */

		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!network) {
			return false;
		}
		if (gps && network) {
			return false;
		} else if (!gps && network) {
			return true;
		}

		return false;

	}

	

}
