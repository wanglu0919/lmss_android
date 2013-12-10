package com.challentec.lmss.recever;

import com.challentec.lmss.listener.MonitorListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 监控广播监听
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class MonitorRecever extends BroadcastReceiver {

	public static final String ACTION_MONTION = "LMSS_MONITOR_ACTION";//监控
	public static final String ACTION_DRIVER_STATE="LMSS_DRIVER_ACTION";//驱动状态
	public static final String ACTION_PORT_STATE="LMSS_PORT_ACTION";//端口状态
	public static final String ACTION_COMMAND_CALL="LMSS_COMMD_CALL_ACTION";//召唤指令
	
	public MonitorListener monitorListener;
	
	
	public void setMonitorListener(MonitorListener monitorListener) {
		this.monitorListener = monitorListener;
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		if(monitorListener!=null){
			monitorListener.monitor(intent);
		}
	}

}
