package com.challentec.lmss.net;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.os.Message;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.AppContext;
import com.challentec.lmss.app.AppManager;
import com.challentec.lmss.exception.ConnectServerTimeOutException;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.LogUtil;

/**
 * app异步任务
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class SynTask {

	private SynHandler handler;//
	private SocketClient socketClient;
	private AppContext context;
	private AppConfig appConfig;
	private AppManager appManager;

	public SynTask(SynHandler handler, AppContext context) {
		this.handler = handler;
		this.handler.setContext(context);
		this.context = context;
		appConfig = AppConfig.getAppConfig(context);
		socketClient = SocketClient.getSocketClient();
		appManager = AppManager.getManager(context);
	}

	public SynTask(AppContext context) {

		this.context = context;
		appConfig = AppConfig.getAppConfig(context);
		socketClient = SocketClient.getSocketClient();
	}

	/**
	 * 设置handler
	 * 
	 * @author 泰得利通 wanglu
	 * @param handler
	 */
	public void setHandler(SynHandler handler) {
		this.handler = handler;
		this.handler.setContext(this.context);
	}

	/**
	 * 写出数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param hexStr
	 * @param isVerfyServer 是否检查服务器通过验证
	 * @isverfy 是否过服务器验证 16进制数据字符串
	 */
	public void writeData(final String hexStr,final boolean isVerfyServer) {

		if (!context.isNetworkConnected()) {// 检查网络连接
			if (handler != null) {
				Message msg = handler.obtainMessage();
				msg.what = SynHandler.NO_NET;
				handler.sendMessage(msg);

			}

			return;

		}

		if (!socketClient.isConnected()) {// 检查连接受否断开
			if (handler != null) {
				Message msg = handler.obtainMessage();
				msg.what = SynHandler.UN_CONNECT;
				handler.sendMessage(msg);
			}

			return;
		}

		/*
		if(isVerfyServer&&!socketClient.isVerify()){//socket连上但是没有通过服务器验证
			
			if (handler != null) {
				Message msg = handler.obtainMessage();
				msg.what = SynHandler.UN_CONNECT;
				handler.sendMessage(msg);
			}

			return ;
			
		}*/
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					socketClient.writeHexStr(hexStr);
					if (handler != null) {
						Message msg = handler.obtainMessage();
						msg.what = SynHandler.SEND_SUCCESS;
						handler.sendMessage(msg);
					}

				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					if (handler != null) {
						Message msg = handler.obtainMessage();
						msg.what = SynHandler.READ_DATA_TIME_OUT;// 读取数据超时处理
						handler.sendMessage(msg);
					}

				} catch (IOException e) {
					e.printStackTrace();
					if (handler != null) {
						Message msg = handler.obtainMessage();
						msg.what = SynHandler.IOEXCEPTION;
						handler.sendMessage(msg);
					}

				}

			}
		}).start();

	}

	/**
	 * 连接服务
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void connectServer(final SocketClient socketClient) {

		if (!context.isNetworkConnected()) {// 检查网络连接
			LogUtil.i(LogUtil.LOG_TAG_CONNECT, "连接服务器没有网络");
			handler.sendEmptyMessage(SynHandler.NO_NET);
			return;
		}

		socketClient.dispose();// 将原有soket连接 资源释放

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					long serverHandTime = appConfig.getSharedPreferences()
							.getLong(AppConfig.SERVER__HANG_TIME_KEY, 0);// 获取服务器挂掉时间

					if (serverHandTime != 0) {// 如果服务器是已经连上后挂掉的，停止1秒后再建立连接
						LogUtil.i(LogUtil.LOG_TAG_CONNECT, "延时连接");
						Thread.sleep(1000);
					}

					socketClient.connect();
					if (handler != null) {
						appManager.setPollCount(0);// 心跳次数清0
						handler.sendEmptyMessage(SynHandler.CONNECTION_SUCCESS);

					}
					ReadTread readTread = new ReadTread(context, socketClient);
					SocketClient.readTread = readTread;
					SocketClient.readTread.start();// 启动读取数据线
				} catch (UnknownHostException e) {

					e.printStackTrace();
					if (handler != null) {
						handler.sendEmptyMessage(SynHandler.UNKNOWNHOST);
					}

				} catch (IOException e) {
					e.printStackTrace();
					if (handler != null) {
						handler.sendEmptyMessage(SynHandler.IOEXCEPTION);
					}

				} catch (ConnectServerTimeOutException e) {

					e.printStackTrace();
					if (handler != null) {
						handler.sendEmptyMessage(SynHandler.CONET_SEVER_TIME_OUT);
					}

				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * UI操作日志记录
	 * 
	 * @author 泰得利通 wanglu
	 * @param uicode
	 */
	public void uiLog(final String ui_code) {
		if (!context.isNetworkConnected()) {// 检查网络连接

			return;
		}

		if (!socketClient.isConnected()) {// 检查连接受否断开

			return;
		}

		if(!socketClient.isVerify()){//没有通过服务器验证
			
			return ;
		}
		
		
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				String logData = ClientAPI.getLOGStr(ui_code);
				try {
					socketClient.writeHexStr(logData);
					LogUtil.i(LogUtil.LOG_TAG_LOG, "提交日志数据" + ui_code + ":"
							+ logData);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

}
