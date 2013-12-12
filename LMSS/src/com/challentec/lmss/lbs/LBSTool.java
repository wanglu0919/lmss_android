package com.challentec.lmss.lbs;

import com.challentec.lmss.util.LogUtil;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

/**
 * 定位
 * @author wanglu 泰得利通
 *
 */
public class LBSTool {
	private Context mContext;  
    private LocationManager mLocationManager;   
    private LocationData mLocation;  
    private LBSThread mLBSThread;  
    private MyLocationListner mNetworkListner;  
    private MyLocationListner mGPSListener;  
    private Looper mLooper; 
    
    public LBSTool(Context context) {  
        mContext = context;  
        //获取Location manager  
        mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);  
    }  
    
    
    /**  
     * 开始定位   
     * @param timeout 超时设置  
     * @return LocationData位置数据，如果超时则为null  
     */  
    public LocationData getLocation(long timeout) {  
        mLocation = null;  
        mLBSThread = new LBSThread();  
        mLBSThread.start();//启动LBSThread  
        timeout = timeout > 0 ? timeout : 0;  
          
        synchronized (mLBSThread) {  
            try {  
                LogUtil.i(Thread.currentThread().getName(), "Waiting for LocationThread to complete...");  
                mLBSThread.wait(timeout);//主线程进入等待，等待时长timeout ms  
                LogUtil.i(Thread.currentThread().getName(), "Completed.Now back to main thread");  
            }  
            catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
        mLBSThread = null;  
        return mLocation;  
    } 
    
    
    
    private class LBSThread extends Thread {  
        @Override  
        public void run() {  
            setName("location thread");   
            LogUtil.i(Thread.currentThread().getName(), "--start--");   
            Looper.prepare();//给LBSThread加上Looper  
            mLooper = Looper.myLooper();  
            registerLocationListener();  
            Looper.loop();  
            LogUtil.e(Thread.currentThread().getName(),  "--end--");  
              
        }  
    }  
    
    
    
    private void registerLocationListener () {  
    	LogUtil.i(Thread.currentThread().getName(), "registerLocationListener"); 
    	/*
        if (isGPSEnabled()) {  
            mGPSListener=new MyLocationListner();    
      
            //五个参数分别为位置服务的提供者，最短通知时间间隔，最小位置变化，listener，listener所在消息队列的looper  
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mGPSListener, mLooper);    
        }  */
     //   if (isNetworkEnabled()) {  
            mNetworkListner=new MyLocationListner();    
     
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, mNetworkListner, mLooper);    
       // }  
    } 
    
    
    
    private class MyLocationListner implements LocationListener{    
    	  
        @Override  
        public void onLocationChanged(Location location) {    
            // 当LocationManager检测到最小位置变化时，就会回调到这里  
            LogUtil.i(Thread.currentThread().getName(), "Got New Location of provider:"+location.getProvider());  
            unRegisterLocationListener();//停止LocationManager的工作  
            try {  
                synchronized (mLBSThread) {   
                	mLocation=new LocationData();
                	mLocation.lat=location.getLatitude()+"";
                	mLocation.lon=location.getLongitude()+"";
                   // parseLatLon(location.getLatitude()+"", location.getLongitude()+"");//解析地理位置  
                    mLooper.quit();//解除LBSThread的Looper，LBSThread结束  
                    mLBSThread.notify();//通知主线程继续  
                }  
            }  
            catch (Exception e) {  
                e.printStackTrace();  
            }  
        }    
     
        //后3个方法此处不做处理 
        @Override  
        public void onStatusChanged(String provider, int status, Bundle extras) {}    
     
        @Override  
        public void onProviderEnabled(String provider) {}    
     
        @Override  
        public void onProviderDisabled(String provider) {}  
      
    };  
    
    
    /**  
     * 注销监听器   
     */  
    private void unRegisterLocationListener () {  
        if(mGPSListener!=null){    
            mLocationManager.removeUpdates(mGPSListener);    
            mGPSListener=null;    
        }   
        if(mNetworkListner!=null){    
            mLocationManager.removeUpdates(mNetworkListner);    
            mNetworkListner=null;    
        }   
    } 
      
}
