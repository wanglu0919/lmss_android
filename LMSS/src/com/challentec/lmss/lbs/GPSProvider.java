package com.challentec.lmss.lbs;

import com.challentec.lmss.ui.MainTabActivity.MyLoactionListener;
import com.challentec.lmss.util.LogUtil;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * GPS����
 * 
 * @author wanglu
 * 
 */
public class GPSProvider {

	private static GPSProvider gpsProvider;
	private static Context context;
	private LocationManager locationManager;
	private static LocationListener locationListener;

	private GPSProvider() {

	}

	public static synchronized GPSProvider getInstance(Context context,
			MyLoactionListener locationListener) {
		if (gpsProvider == null) {
			gpsProvider = new GPSProvider();
			GPSProvider.context = context;
			GPSProvider.locationListener = locationListener;
			locationListener.gpsProvider = gpsProvider;
		}
		return gpsProvider;
	}

	public void getLocation() {
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		String provider = getProvider();
		LogUtil.i(LogUtil.LOG_TAG_I, provider+"");
		if (provider != null) {
			locationManager.requestLocationUpdates(getProvider(), 5000, 50,
					locationListener);

		}

	}

	/**
	 * 停止定位信息获取 wanglu 泰得利通
	 */
	public void removeLocation() {
		if (locationListener != null&&getProvider()!=null) {
			LogUtil.i(LogUtil.LOG_TAG_I, "取消了自动定位");
			locationManager.removeUpdates(locationListener);
		}

	}

	private String getProvider() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 精确定位
		criteria.setAltitudeRequired(false);// ����Ҫ��ȡ�������
		criteria.setCostAllowed(true);// ������������ʷ�
		criteria.setSpeedRequired(true);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		return locationManager.getBestProvider(criteria, true);

	}
}
