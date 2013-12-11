package com.challentec.lmss.util;

/**
 * API
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ClientAPI {

	/**
	 * 功能
	 * 
	 * @author 泰得利通 wanglu
	 * @param function
	 *            功能
	 * @param data
	 *            原始数据
	 * @return 协议数据16进制的字符串数据
	 */
	public static String getApiStr(String function, String data) {

		String hexData = function + DataPaseUtil.bytes2hexStr(data.getBytes());// 字符串转换为16进制数据,功能代码+数据
		int len = hexData.length() / 2 - 2;// 数据原数据长度 不包含功能代码长度
		String hexLen = DataPaseUtil.getHexLen(len);

		String encryptData = Des.enHexStr(hexData);

		return Protocol.PROTOCOL_HEADER + hexLen + encryptData;// 协议+原数据长度+加密后数据
	}

	/**
	 * 构建发送16进值数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param function
	 *            功能代码
	 * @param hexStr 16进制数据
	 * @return 拼接的协议数据
	 */
	public static String getHexApiStr(String function, String hexStr) {

		String hexData = function + hexStr;// 字符串转换为16进制数据,功能代码+数据
		int len = hexStr.length() / 2;// 数据原数据长度 不包含功能代码长度
		String hexLen = DataPaseUtil.getHexLen(len);

		String encryptData = Des.enHexStr(hexData);

		return Protocol.PROTOCOL_HEADER + hexLen + encryptData;// 协议+原数据长度+加密后数据
	}

	/**
	 * 
	 * @author 泰得利通 wanglu
	 * @param function
	 *            功能码
	 * @return 协议数据
	 */
	public static String getApiStr(String function) {

		String hexData = function;// 字符串转换为16进制数据,功能代码+数据
		int len = 0;// 数据原数据长度 不包含功能代码长度
		String hexLen = DataPaseUtil.getHexLen(len);
		String encryptData = Des.enHexStr(hexData);

		return Protocol.PROTOCOL_HEADER + hexLen + encryptData;// 协议+原数据长度+加密后数据
	}

	/**
	 * 构建日志数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param ui_code
	 *            UI编码
	 * @return 协议数据
	 */
	public static String getLOGStr(String ui_code) {
		String hexData = Protocol.C_LOG + ui_code;
		int len = ui_code.length() / 2;
		String hexLen = DataPaseUtil.getHexLen(len);

		String encryptData = Des.enHexStr(hexData);
		return Protocol.PROTOCOL_HEADER + hexLen + encryptData;// 协议+原数据长度+加密后数据

	}

}
