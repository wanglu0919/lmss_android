package com.challentec.lmss.app;

import java.util.HashMap;

import android.content.Context;

/**
 * app 提示消息管理类
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class AppTipMessage {

	// ---协议定义错误信息
	public static final String NO_TEL_USE = "01";// 手机号码不存在(非法用户)
	public static final String NO_IME_USE = "02";// 手机序列号不存在(非法用户)
	public static final String TEL_IME_NOT_PAIR = "03";// 手机号和序列号不匹配（非法用户）
	public static final String VECODE_ERROR = "04";// 验证码错误
	public static final String VECODE_TIME_OUT = "05";// 验证码过期
	public static final String NO_DEVICE = "06";// 选择设备不存在
	public static final String NO_DEVICE_AUTHORITY = "07";// 选择设备权限不足

	public static final String UN_KNOW_SEVER_ERROR = "08";// 未知服务端错误，请联系管理员

	public static final String FLOOR_DEVICE_OFFLINE = "09";// 电梯设备未在线
	public static final String GET_DATA_FAIL = "10";// 获取数据失败
	public static final String DATA_FORMAT_ERROR = "11";// 数据格式错误,无法解析
	public static final String GET_VECODE_FAST = "12";// 发送验证码太频繁
	public static final String TEL_NUMBER_NOT_MATCH = "13";// 手机号码不匹配
	public static final String NO_AUTH_DEVICE = "14";// 未获得设备授权而进行设备操作错误

	// 自定义错误信息
	public static final String IOEXCEPTION = "20";// 网络异常
	public static final String UN_CONNECT = "21";// 未连接
	public static final String CONNECT_SUCCESS = "22";// 连接成功
	public static final String UN_UNKNOWNHOST = "23";// 服务器不存在
	public static final String NO_NET = "24";// 没有网络连接
	public static final String CONNECT_SERVER_TIME_OUT = "25";// 连接服务器超时
	public static final String READ_DATA_TIME_OUT = "26";// 读取数据超时
	public static final String UNKNOW_ERROR = "27";// 未知错误

	private static HashMap<String, Integer> tipMessage = new HashMap<String, Integer>();

	static {

		// ---协议定义错误信息
		tipMessage.put(NO_TEL_USE, R.string.tip_msg_no_tel_use);
		tipMessage.put(NO_IME_USE, R.string.tip_msg_no_ime_use);
		tipMessage.put(TEL_IME_NOT_PAIR, R.string.tip_msg_tel_ime_not_pair);
		tipMessage.put(VECODE_ERROR, R.string.tip_msg_vecode_error);
		tipMessage.put(VECODE_TIME_OUT, R.string.tip_msg_vecode_time_out);
		tipMessage.put(NO_DEVICE, R.string.tip_msg_no_device);
		tipMessage.put(NO_DEVICE_AUTHORITY,
				R.string.tip_msg_no_device_authority);
		tipMessage.put(UN_KNOW_SEVER_ERROR,
				R.string.tip_msg_un_know_sever_error);
		tipMessage.put(FLOOR_DEVICE_OFFLINE,
				R.string.tip_msg_floor_device_offline);
		tipMessage.put(GET_DATA_FAIL, R.string.tip_msg_get_data_fail);

		tipMessage.put(DATA_FORMAT_ERROR, R.string.tip_msg_data_format_error);

		tipMessage.put(GET_VECODE_FAST, R.string.tip_msg_get_vecode_fast);

		tipMessage.put(TEL_NUMBER_NOT_MATCH, R.string.tip_msg_tel_num_no_match);
		tipMessage.put(NO_AUTH_DEVICE, R.string.tip_msg_no_auth_device);

		// 自定义错误信息
		tipMessage.put(IOEXCEPTION, R.string.tip_msg_net_error);
		tipMessage.put(UN_CONNECT, R.string.tip_msg_net_un_connect);
		tipMessage.put(CONNECT_SUCCESS, R.string.tip_msg_net_connected);
		tipMessage.put(UN_UNKNOWNHOST, R.string.tip_msg_net_nuknow_server);
		tipMessage.put(NO_NET, R.string.tip_msg_no_net);
		tipMessage.put(CONNECT_SERVER_TIME_OUT,
				R.string.tip_msg_net_connect_server_time_out);
		tipMessage.put(READ_DATA_TIME_OUT,
				R.string.tip_msg_net_read_data_time_out);
		tipMessage.put(UNKNOW_ERROR, R.string.tip_msg_unknow_error);

	}

	/**
	 * 根据code编码获取异常资源文件名称
	 * 
	 * @author 泰得利通 wanglu
	 * @param code
	 * @return 提示数据
	 */
	public static String getString(Context context, String code) {

		if (tipMessage.get(code) == null) {
			return context.getString(R.string.tip_msg_unknow_error);// 未知错误
		}
		return context.getString(tipMessage.get(code));
	}

	/**
	 * 返回字符串资源ID
	 * 
	 * @author 泰得利通 wanglu
	 * @param code
	 * @return 字符串资源文件id
	 */
	public static int getResouceStringId(String code) {

		return tipMessage.get(code) == null ? R.string.tip_msg_unknow_error
				: tipMessage.get(code);
	}

}
