package com.challentec.lmss.util;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * 轮询服务类
 * @author 泰得利通 wanglu
 *
 */
public class PollingUtils {
	/**
	 * 开始心跳
	 *@author 泰得利通 wanglu
	 * @param context
	 * @param seconds
	 * @param cls
	 * @param action
	 */
    public static void startPollingService(Context context, int seconds, Class<?> cls,String action) {
    	//获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
         
      //包装需要执行Service的Intent
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
         
      //触发服务的起始时间
        long triggerAtTime = SystemClock.elapsedRealtime();
         
      //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
                seconds * 1000, pendingIntent);
    }
 
   /**
    * 停止轮询服务
    *@author 泰得利通 wanglu
    * @param context
    * @param cls
    * @param action
    */
    public static void stopPollingService(Context context, Class<?> cls,String action) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //取消执行任务
        manager.cancel(pendingIntent);
      
    }
}
