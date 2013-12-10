package com.challentec.lmss.net;

import android.os.Handler;
import android.os.Message;

import com.challentec.lmss.app.AppContext;
import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.util.UIHelper;

/**
 * 异步handler处理类
 * @author 泰得利通 wanglu
 *
 */
public class SynHandler extends Handler {

	private AppContext context;

	public void setContext(AppContext context) {
		this.context = context;
	}

	public static final int IOEXCEPTION = 0x01;// ioExctption
	public static final int SEND_SUCCESS = 0x02;// 发送数据成功
	public static final int UN_CONNECT = 0x03;// 未连接，连接断开
	public static final int CONNECTION_SUCCESS = 0x04;// 连接服务器成功
	public static final int UNKNOWNHOST = 0x05;// 服务器不存在
	public static final int CONET_SEVER_TIME_OUT = 0x06;// 连接服务器超时
	public static final int READ_DATA_TIME_OUT = 0x07;// 读取数据超时
	public static final int NO_NET = 0x08;// 没有网络连接

	public SynHandler() {

	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {

		case IOEXCEPTION:// io 异常
			onIOException(AppTipMessage.IOEXCEPTION);

			break;
		case SEND_SUCCESS:// 写出数据成功

			onSendSuccess();
			break;
		case UN_CONNECT:// 未连接
			onUnConnect(AppTipMessage.UN_CONNECT);

			break;
		case CONNECTION_SUCCESS:// 连接服务器成功
			onConnectSuccess(AppTipMessage.CONNECT_SUCCESS);

			break;

		case UNKNOWNHOST:// 服务器不存在
			onUNKnowHost(AppTipMessage.UN_UNKNOWNHOST);

			break;

		case CONET_SEVER_TIME_OUT:// 连接服务器操时
			onConnectServerTimeOUt(AppTipMessage.CONNECT_SERVER_TIME_OUT);

			break;

		case READ_DATA_TIME_OUT:// 读取数据超时
			onReadDataTimeOut(AppTipMessage.READ_DATA_TIME_OUT);

			break;
		case NO_NET:// 没有网络连接
			onNoNet(AppTipMessage.NO_NET);

			break;

		}

		onFianly();

	}

	/**
	 * 写数据成功
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void onSendSuccess() {

	}

	/**
	 * 没有网络连接处理
	 * 
	 * @author 泰得利通 wanglu
	 * @param code
	 */
	private void onNoNet(String code) {
		onToast(code);

	}

	/**
	 * 读取数据超时处理
	 * 
	 * @author 泰得利通 wanglu
	 * @param code
	 */
	public void onReadDataTimeOut(String code) {
		onToast(code);

	}

	/**
	 * 最终执行方法
	 * 
	 * @author 泰得利通 wanglu
	 */

	public void onFianly() {

	}

	/**
	 * 连接服务器超时处理
	 * 
	 * @author 泰得利通 wanglu
	 * @param connectServerTimeOut
	 */
	private void onConnectServerTimeOUt(String code) {
		onToast(code);

	}

	/**
	 * 主机不存在处理
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void onUNKnowHost(String code) {
		onToast(code);

	}

	/**
	 * 异常处理
	 * 
	 * @author 泰得利通 wanglu
	 * @param code
	 */
	protected void onToast(String code) {
		UIHelper.showToask(context, AppTipMessage.getResouceStringId(code));

	}

	/**
	 * 连接服务器成功
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void onConnectSuccess(String code) {

		onToast(code);
	}

	/**
	 * 处理没有返回数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	public void onSuccess() {
	}

	/**
	 * IO异常处理
	 * 
	 * @author 泰得利通 wanglu
	 * @param code
	 */
	public void onIOException(String code) {

		onToast(code);

	}

	/**
	 * 未连接处理
	 * 
	 * @author 泰得利通 wanglu
	 * @param code
	 */
	public void onUnConnect(String code) {
		onToast(code);
	}
}
