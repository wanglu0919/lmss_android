package com.challentec.lmss.recever;

import com.challentec.lmss.listener.NetWorkStateLinstener;
import com.challentec.lmss.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态监听接收者
 * @author wanglu 泰得利通
 *
 */
public class NetWorkStateRecever extends BroadcastReceiver {
	private ConnectivityManager connectivityManager;
	private NetWorkStateLinstener netWorkStateLinstener;//网络状态监听器
	 public void setNetWorkStateLinstener(NetWorkStateLinstener netWorkStateLinstener) {
		this.netWorkStateLinstener = netWorkStateLinstener;
	}




	private NetworkInfo info;
	
	public NetWorkStateRecever(NetWorkStateLinstener netWorkStateLinstener){
		this.netWorkStateLinstener=netWorkStateLinstener;
		
	}
	
	public NetWorkStateRecever(){
		
		
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
           LogUtil.i(LogUtil.LOG__TAG_NETWORK, "网络状态发生了变化");
            connectivityManager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();  
            if(info != null && info.isAvailable()) {
                String name = info.getTypeName();
                LogUtil.i(LogUtil.LOG__TAG_NETWORK, name+"网络可用了...");
                
                netWorkStateLinstener.onNetOpen();//通知监听器
                
            } else {
            	LogUtil.i(LogUtil.LOG__TAG_NETWORK, "网络关闭了....");
            	  netWorkStateLinstener.onNetClose();//通知监听器网络关闭了
            }
        }
		
		
	}

	

}
