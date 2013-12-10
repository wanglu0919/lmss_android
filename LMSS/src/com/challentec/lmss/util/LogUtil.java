package com.challentec.lmss.util;

import android.util.Log;

/**
 * log 统一管理类
 * 
 * @author wanglu
 * 
 */
public class LogUtil {

	private static final int LOG_LEVEL = 1;
	private static final int LEVEL = 0;

	public static final String LOG_TAG_POLL="POLL";
	public static final String LOG_TAG_LOCATION="LOCATION";//位置
	public static final String LOG_TAG_CONNECT="CONNECT";//连接
	public static final String LOG_TAG_RECEIVE="RECEIVE";//接受数据
	public static final String LOG_TAG_READ="READ";//读取数据
	public static final String LOG_TAG_LOG="UI_LOG";//UI日志
	public static final String LOG_TAG_AUTO_CONNECT="AUTO_CONNECT";//自动重连
	public static final String LOG_TAG_BEAT="BEAT";//心跳
	public static final String LOG_TAG_SEND="SEND";//发送数据
	public static final String LOG_TAG_REPONSE_DATA="RESPONSE_DATA";//返回数据
	
	
	/**
	 * 输出信息
	 * 
	 * @param tag
	 * @param msg
	 */
	
	
	public static void i(String tag, String msg) {

		if (log()) {
			Log.i(tag, msg);

		}

	}

	/**
	 * 输出错误信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (log()) {
			Log.e(tag, msg);
		}
	}

	/**
	 * 输出调试信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		if (log()) {
			Log.d(tag, msg);
		}
	}

	/**
	 * 是否打印log 根据定义级别判断
	 * LOG_LEVEL>LEVEL时输出打印日志
	 * @return
	 */

	private static boolean log() {
		return LOG_LEVEL > LEVEL;

	}
}
