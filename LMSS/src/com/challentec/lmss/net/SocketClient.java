package com.challentec.lmss.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import com.challentec.lmss.exception.ConnectServerTimeOutException;
import com.challentec.lmss.exception.ReadDataException;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.LogUtil;

/**
 * socket通信
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class SocketClient {

	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private static SocketClient instance;
	private static final String INTRANET_IP = "172.16.40.97";// 内服务器IP
	private static final String OUTERNET_IP = "61.160.96.205";// 外网服务器IP
	private String ip = "";// ip地址
	private static final int PORT = 9721;// 服务器端口
	private static final int CONNECT_SERVER_TIME_OUT = 2000;

	// private static final int READ_TIME_OUT=10000;//读取数据超时

	private SocketClient() {
	}

	/**
	 * 单利
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	public static synchronized SocketClient getSocketClient() {

		if (instance == null) {
			instance = new SocketClient();
		}

		return instance;
	}

	/**
	 * 连接服务器 每隔两米尝试连接服务器，连接成功保存输入和输出流
	 * 
	 * @author 泰得利通 wanglu
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ConnectServerTimeOutException
	 * @throws AppExctption
	 * @throws TimeoutException
	 */
	public void connect() throws UnknownHostException, IOException,
			ConnectServerTimeOutException {

		boolean flag = true;
		int i = 0;
		while (flag) {// 连接服务器
			try {
				LogUtil.i(LogUtil.LOG_TAG_CONNECT, "连接服务器一次");
				Thread.sleep(500);// 每个两秒尝试连接一次服务器
				socket = null;
				socket = new Socket();
				
				if (i % 2 == 0) {//轮询连接服务器
					ip = OUTERNET_IP;
				} else {
					ip = INTRANET_IP;
				}
				i++;
				socket.connect(new InetSocketAddress(ip, PORT),
						CONNECT_SERVER_TIME_OUT);
				is = socket.getInputStream();
				os = socket.getOutputStream();
				flag = false;
			} catch (ConnectException e) {
				e.printStackTrace();
				LogUtil.i(LogUtil.LOG_TAG_CONNECT, "连接服务器异常");

			} catch (java.net.SocketException e) {
				e.printStackTrace();
				LogUtil.i(LogUtil.LOG_TAG_CONNECT, "连接服务器异常");
			} catch (InterruptedException e) {
				e.printStackTrace();

				LogUtil.i(LogUtil.LOG_TAG_CONNECT, "连接服务器异常");

			} catch (SocketTimeoutException e) {

				LogUtil.i(LogUtil.LOG_TAG_CONNECT, "连接服务器超时");
			}

		}

	}

	/**
	 * 写出16进制字符串
	 * 
	 * @author 泰得利通 wanglu
	 * @param hexStr
	 *            16进制字符串数据
	 * @throws IOException
	 */
	public void writeHexStr(String hexStr) throws IOException {
		if (os != null) {
			LogUtil.i(LogUtil.LOG_TAG_SEND, hexStr);// 打印日志
			os.write(DataPaseUtil.hexString2byte(hexStr));// 将16进值字符串数据转化为对应自己数组些出去
			os.flush();
		}
	}

	/**
	 * 读取数据
	 * 
	 * @author 泰得利通 wanglu
	 * @return 返回16进制字符串
	 * @throws IOException
	 * @throws ReadDataException
	 */
	public String readData() throws IOException, ReadDataException {
		byte buffer[] = new byte[1024];
		int len = is.read(buffer);// 输入流读取数据
		String responseData = null;
		if (len > 0) {
			responseData = DataPaseUtil.bytes2hexStr(buffer).substring(0,
					len * 2);
			LogUtil.i(LogUtil.LOG_TAG_READ, "读取的数据：" + responseData);// 打印日志
		} else {
			throw new ReadDataException();// 抛出读取数据异常，服务这时一般标识socket连接异常，soket断掉了.
		}
		return responseData;

	}

	/**
	 * 判断socket是否连接
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	public boolean isConnected() {
		if (socket != null) {
			return socket.isConnected();
		}

		return false;
	}

	public Socket getSocket() {

		return socket;
	}

	/**
	 * 关闭socket 释放资源
	 * 
	 * @author 泰得利通 wanglu
	 * @throws IOException
	 */
	public void dispose() {
		try {
			if (socket != null && socket.isConnected()) {

				is.close();
				os.close();
				socket.close();
				socket = null;
				is = null;
				os = null;
			}
		} catch (IOException e) {

		}

	}

}
