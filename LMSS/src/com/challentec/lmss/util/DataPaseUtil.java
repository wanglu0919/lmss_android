package com.challentec.lmss.util;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;

public class DataPaseUtil {

	private static String hexString = "0123456789abcdef"; // 此处不可随意改动

	/**
	 * 将16进制字符串转换为字节数组
	 * 
	 * @author 泰得利通 wanglu
	 * @param hexString
	 * @return 字节数组
	 */
	public static byte[] hexString2byte(String hexStr) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				hexStr.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < hexStr.length(); i += 2)
			baos.write((hexString.indexOf(hexStr.charAt(i)) << 4 | hexString
					.indexOf(hexStr.charAt(i + 1))));
		return baos.toByteArray();
	}

	/**
	 * 获取16进制数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 *            数据
	 * @param byteLen
	 *            字节数
	 * @return
	 */

	public static String getHexStr(int data, int byteLen) {

		String hexData = Integer.toHexString(data).toLowerCase();
		String spre = "";
		for (int i = 0; i < byteLen * 2 - hexData.length(); i++) {// 不够高位补0
			spre += "0";
		}

		return spre + hexData;

	}

	/**
	 * 小数获取16进制
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 * @param inLen
	 *            整数字节数
	 * @param floatLen
	 *            小数部分字节数
	 * @return
	 */
	public static String getHexStr(String data, int intLen, int floatLen) {
		String floatData[] = data.split("\\.");
		return getHexStr(Integer.parseInt(floatData[0]), intLen)
				+ getHexStr(Integer.parseInt(floatData[1]), floatLen);

	}

	/**
	 * 比列型获取
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 * @param intLen
	 *            前项比字节数
	 * @param floatLen
	 *            后项比字节数
	 * @return
	 */
	public static String getCompareHexStr(String data, int intLen, int floatLen) {
		String floatData[] = data.split(":");
		return getHexStr(Integer.parseInt(floatData[0]), intLen)
				+ getHexStr(Integer.parseInt(floatData[1]), floatLen);

	}

	/**
	 * 二进制字符穿转为16进制
	 * 
	 * @author 泰得利通 wanglu
	 * @param binary
	 *            位字符串
	 * @param byteLen
	 *            字节数
	 * @return
	 */
	public static String binaryToHex(String binary, int byteLen) {
		int result = 0;
		int j = 0;
		for (int i = binary.length(); i > 0; i--) {

			result += Integer.parseInt(binary.substring(i - 1, i))
					* Math.pow(2, j);
			j++;
		}

		return getHexStr(result, byteLen);

	}

	/**
	 * 16进制的字符串的字节数组转换为字节数组
	 * 
	 * @author 泰得利通 wanglu
	 * @param b
	 * @return
	 */
	public static byte[] hexStr2byte(byte[] b) {

		if ((b.length % 2) != 0)

			throw new IllegalArgumentException("长度不是偶数");

		byte[] b2 = new byte[b.length / 2];

		for (int n = 0; n < b.length; n += 2) {

			String item = new String(b, n, 2);

			b2[n / 2] = (byte) Integer.parseInt(item, 16);

		}

		return b2;
	}

	/**
	 * 字节数组转成16进制字符串 二行制转字符串
	 * 
	 * @param b
	 * 
	 * @return
	 */

	@SuppressLint("DefaultLocale")
	public static String bytes2hexStr(byte[] b) {

		String hs = "";

		String stmp = "";

		for (int n = 0; n < b.length; n++) {

			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1)

				hs = hs + "0" + stmp;

			else

				hs = hs + stmp;

		}

		return hs.toLowerCase();

	}

	/**
	 * 获取16进制长度
	 * 
	 * @author 泰得利通 wanglu
	 * @param len
	 * @return
	 */
	public static String getHexLen(int len) {
		String hexLen = Integer.toHexString(len);
		String spre = "";

		for (int i = 0; i < 4 - hexLen.length(); i++) {
			spre += "0";
		}

		return spre + hexLen;

	}

	/**
	 * 16进制自负穿转换为10进制无符号整数
	 * 
	 * @author 泰得利通 wanglu
	 * @param s
	 * @return
	 */
	public static int hexStrToInt(String s) {
		int sub = 0;

		for (int i = 0; i < s.length(); i++) {

			int sub1 = hexString.indexOf(s.charAt(i));

			for (int j = 0; j < s.length() - i - 1; j++) {

				sub1 = sub1 * 16;

			}

			sub += sub1;

		}

		return sub;

	}

	/**
	 * 截取返回数据中的指定为10进制数字
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return
	 */
	public static int getDataInt(String data, int start, int end) {

		return hexStrToInt(data.substring(start, end));
	}

	/**
	 * 截取返回数据中的小数
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 *            返回数据
	 * @param start开始位置
	 * @param end
	 *            结束位置
	 * @param intLen
	 *            整数位长度
	 * @accuracy 精确度
	 * @return
	 */

	public static String getDataFloat(String data, int start, int end,
			int intLen, int accuracy) {
		String floatData = data.substring(start, end);
		String intStr = floatData.substring(0, intLen);// 整数部分
		String floatStr = floatData.substring(intLen);// 小数部分

		floatStr = hexStrToInt(floatStr) + "";
		String pre = "";
		for (int i = 0; i < accuracy - floatStr.length(); i++) {// 不够精确度，补0
			pre += "0";
		}
		floatStr = pre + floatStr;
		String floatDataStr = hexStrToInt(intStr) + "." + floatStr;
		return floatDataStr;
		
	}

	/**
	 * 截取比
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 *            返回数据
	 * @param start开始位置
	 * @param end
	 *            结束位置
	 * @param preLen
	 *            前相比长度
	 * @return
	 */
	public static String getCompareData(String data, int start, int end,
			int preLen) {
		String floatData = data.substring(start, end);
		String preStr = floatData.substring(0, preLen);// 前相比
		String lastStr = floatData.substring(preLen);// 后相比

		String compareData = hexStrToInt(preStr) + ":" + hexStrToInt(lastStr);

		return compareData;
	}

	/**
	 * 将16进制字符转化为2进制值字符串
	 * 
	 * @author 泰得利通 wanglu
	 * @param hexStr
	 * @return
	 */
	public static String hexString2BinaryString(String hexStr) {

		byte[] buffer = hexString2byte(hexStr);// 转化为字节数组
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buffer.length; i++) {

			String data = Integer.toBinaryString(buffer[i] & 0x00ff);// 取低八位

			if (data.length() < 8) {// 不够8位，高位补0
				String pre = "";
				for (int j = 0; j < 8 - data.length(); j++) {
					pre += "0";
				}
				data = pre + data;

			}
			sb.append(data);

		}

		return sb.toString();

	}

	/**
	 * 将16进制字符转化为2进制值字符串 逆转
	 * 
	 * @author 泰得利通 wanglu
	 * @param hexStr
	 * @return
	 */
	public static String hexString2BinaryRe(String hexStr) {

		byte[] buffer = hexString2byte(hexStr);// 转化为字节数组
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buffer.length; i++) {

			String data = Integer.toBinaryString(buffer[i] & 0x00ff);// 取低八位

			if (data.length() < 8) {// 不够8位，高位补0
				String pre = "";
				for (int j = 0; j < 8 - data.length(); j++) {
					pre += "0";
				}
				data = pre + data;

			}
			StringBuffer sbrevoce = new StringBuffer(data);
			sb.append(sbrevoce.reverse());// 高位地位颠倒

		}

		return sb.toString();

	}
}
