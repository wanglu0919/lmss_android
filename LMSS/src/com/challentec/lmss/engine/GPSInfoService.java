package com.challentec.lmss.engine;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;

public class GPSInfoService {

	private LocationManager manager;

	private LocationListener locationListener;

	// 私有化构造方法
	private GPSInfoService(Context context) {

		manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

	}

	private static GPSInfoService mGPSService;

	public synchronized static GPSInfoService getInstance(Context context) {
		if (mGPSService == null) {
			mGPSService = new GPSInfoService(context);
		}

		return mGPSService;
	}

	/*
	 * 当前你的手机 所支持的定位方式获取出来 有多种定位方式 gps network ,基站, passive 可以根据定位的条件 ,获取
	 * 一个最好的定位方式
	 */
	public void registerLocationUpdates(LocationListener locationListener) {

		this.locationListener = locationListener;
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 精准定位
		criteria.setAltitudeRequired(false);// 不需要获取海拔数据
		criteria.setCostAllowed(true);// 设置允许城市资费
		criteria.setSpeedRequired(true);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

		// 获取一个最符合查询条件的位置提供者
		String provider = manager.getBestProvider(criteria, true);

		// 位置改变就会调用Linster的监听器 获取经度纬度
		if (provider != null) {
			manager.requestLocationUpdates(provider, 1000, 50, locationListener);
		}

	}

	public void cancleLocationUpdates() {
		manager.removeUpdates(locationListener);
		locationListener = null;
	}

}
