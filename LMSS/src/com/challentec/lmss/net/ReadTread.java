package com.challentec.lmss.net;

import java.io.IOException;

import com.challentec.lmss.app.AppContext;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.exception.ReadDataException;
import com.challentec.lmss.util.HandlerMessage;
import com.challentec.lmss.util.LogUtil;
import com.challentec.lmss.util.Protocol;

import android.content.Context;

/**
 * 读取数据线程
 * 
 * @author wanglu 泰得利通
 * 
 */
public class ReadTread extends Thread {


	private AppContext context;
	private SocketClient socketClient;
	

	public  ReadTread(AppContext appContext,SocketClient socketClient) {
		this.context=appContext;
		this.socketClient=socketClient;
	}

	

	@Override
	public void run() {

		LogUtil.i(LogUtil.LOG_TAG_READ, "读取线程启动");

		while (true) {// 循环从缓冲区读取消息
			try {
				
				if(this.isInterrupted()){
					LogUtil.i(LogUtil.LOG_TAG_READ, "读取线程打断,释放读取线程");
					break;
				}
				if (!context.isNetworkConnected()
						|| !socketClient.isConnected()) {// 检查网络连接和socket状态
					LogUtil.i(LogUtil.LOG_TAG_READ, "读取连接断开或未连接");
					continue;
				}
				LogUtil.i(LogUtil.LOG_TAG_READ, "读取数据等待中....");
				String responseData = socketClient.readData();// 该方法为阻塞方法，阻塞读取服务器返回数据
				if (responseData != null) {
					ResponseData rd = Protocol.paseData(responseData);// 解析返回数据
					HandlerMessage.handlerMessage(context, rd);// 分发读取返回的消息
					LogUtil.i(LogUtil.LOG_TAG_READ,
							"读取到了数据功能号为" + rd.getFunctionCode());
				}
			} catch (IOException e) {

				LogUtil.i(LogUtil.LOG_TAG_READ, "读取数据异常");
			} catch (ReadDataException e) {
				e.printStackTrace();
				LogUtil.i(LogUtil.LOG_TAG_READ, "服务端挂了");
				HandlerMessage.handlerUNConnectMessage(context);// 发送服务挂了消息
				break;
			}
		}

	}

	

}
