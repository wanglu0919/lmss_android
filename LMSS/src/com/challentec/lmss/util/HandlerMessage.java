package com.challentec.lmss.util;

import android.content.Context;
import android.content.Intent;

import com.challentec.lmss.app.AppManager;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.recever.AppConnectStateRecever;
import com.challentec.lmss.recever.AppMessageRecever;

/**
 * 分发消息数据类
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class HandlerMessage {

	/**
	 * 分发数据,将消息发送给注册AppMessageRecever 的广播接收者处理
	 * 
	 * @author 泰得利通 wanglu
	 * @param context
	 * @param responseData
	 */
	public static void handlerMessage(Context context, ResponseData responseData) {
		if (responseData.getFunctionCode().equals(Protocol.C_EXIT_LOGIN)) {
			AppManager.getManager(context).redirtToLogin();// 退出道登录界面

			return;
		}
		
		if(responseData.getFunctionCode().equals(Protocol.C_BEAT)){//返回心跳
			LogUtil.i(LogUtil.LOG_TAG_BEAT, "心跳返回数据");
			LogUtil.i(LogUtil.LOG_TAG_BEAT_COUNT, "收到心跳前心跳次数:"+AppManager.getManager(context).getPollCount());
			AppManager.getManager(context).reduceCount();//减少心跳次数
			
			LogUtil.i(LogUtil.LOG_TAG_BEAT_COUNT, "收到心跳后心跳次数:"+AppManager.getManager(context).getPollCount());
			return ;
		}
		Intent intent = new Intent();
		intent.setAction(AppMessageRecever.ACTION_STRING);
		intent.putExtra(AppMessageRecever.DATA_KEY, responseData);
		context.sendBroadcast(intent);// 通知广播

	}

	/**
	 * 分发网络连接断掉时的通信 将消息发给注册，AppConnectStateRecever 广播接收者处理网路断开消息
	 * 
	 * @author 泰得利通 wanglu
	 * @param context
	 */
	public static void handlerUNConnectMessage(Context context) {
		Intent intent = new Intent();
		intent.setAction(AppConnectStateRecever.ACTION_STRING);
		context.sendBroadcast(intent);// 网络连接端口通知广播
		LogUtil.i(LogUtil.LOG_TAG_CONNECT, "发送了服务端挂了消息");
	}

}
