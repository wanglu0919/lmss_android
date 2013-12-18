package com.challentec.lmss.util;

import java.util.ArrayList;
import java.util.List;

import com.challentec.lmss.bean.ResponseData;

/**
 * 协议规则
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class Protocol {

	/**
	 * 协议头
	 */
	public static final String PROTOCOL_HEADER = "4c4d5353";

	// -----------------------------功能编码-----------------------//
	/**
	 * 心跳
	 */
	public static final String C_BEAT = "0000";

	/**
	 * 连接服务器验证
	 */
	public static final String C_SEVER_VERIFY = "0001";

	/**
	 * 获取验证码
	 */
	public static final String C_GET_VCODE = "0101";

	/**
	 * 登陆
	 */
	public static final String C_LOGIN = "0102";

	/**
	 * 语言设置
	 */
	public static final String C_SET_LANGUAGE = "0103";

	/**
	 * 发送授权码
	 */
	public static final String C_SEND_AUTHORIZATION_CODE = "0104";

	/**
	 * 日志
	 */
	public static final String C_LOG = "0105";

	/**
	 * 自动重连
	 */
	public static final String C_AUTO_CONNECT = "0106";
	/**
	 * 获取设备基础信息
	 */
	public static final String C_GET_DEVICE_INFO = "0201";

	/**
	 * 恢复设备出场设置
	 */
	public static final String C_RECOVER_DEVICE = "0202";

	/**
	 * 恢复上次设置值
	 */
	public static final String C_RECOVER_LAST_SET = "0203";

	/**
	 * 退出到登录界面
	 */
	public static final String C_EXIT_LOGIN = "0107";

	/**
	 * 获取设备基本参数
	 */
	public static final String S_GET_DEVICE_BASE_P = "0310";

	/**
	 * 保存设备基本参数
	 */
	public static final String S_SAVE_DEVICE_BASE_P = "0311";

	/**
	 * 获取电机参数
	 */
	public static final String S_GET_ELEC_P = "0320";

	/**
	 * 保存电机参数
	 */
	public static final String S_SAVE_ELEC_P = "0321";

	/**
	 * 电机自学习开始
	 */
	public static final String S_ELEC_AUTO_STUDY = "0330";

	/**
	 * 进道子学习开始
	 */
	public static final String S_WELL_AUTO_STUDY = "0331";

	/**
	 * 电机自学习参数获取
	 */
	public static final String S_ELEC_STUDY_GET_P = "0332";

	/**
	 * 保存电机自学习参数
	 */
	public static final String S_SAVE_ELEC_STUDY_P = "0333";

	/**
	 * 进道自学习参数获取
	 */
	public static final String S_WELL_STUDY_GET_P = "0334";

	/**
	 * 保存进道自学习参数
	 */
	public static final String S_SAVE_WELL_STUDY_P = "0335";

	/**
	 * 获取层楼前后门设置-前门
	 */
	public static final String S_GET_FLOOR_SET_FRONT = "0340";

	/**
	 * 保存层楼前后门设置-前门
	 */
	public static final String S_SAVE_FLOOR_SET_FRONT = "0341";

	/**
	 * 获取层楼前后门设置-后门
	 */
	public static final String S_GET_FLOOR_SET_BACK = "0342";

	/**
	 * 保存层楼前后门设置-后门
	 */
	public static final String S_SAVE_FLOOR_SET_BACK = "0343";

	/**
	 * 获取层楼显示设置
	 */
	public static final String S_GET_FLOOR_DISPALY = "0350";
	/**
	 * 保存层楼显示设置
	 */
	public static final String S_SAVE_FLOOR_DISPALY = "0351";
	/**
	 * 获取主板输入逻辑设置
	 */
	public static final String S_GET_MAINBOARD = "0360";
	/**
	 * 保存主板输入逻辑设置
	 */

	public static final String S_SAVE_MAINBOARD = "0361";

	/**
	 * 设备更换
	 */
	public static final String S_CHANGE_DEVICE = "0370";

	/**
	 * 获取矢量控制参数
	 */
	public static final String T_GET_VECTOR_P = "0410";

	/**
	 * 保存矢量控制参数
	 */
	public static final String T_SAVE_VECTOR_P = "0411";

	/**
	 * 获取运行控制参数
	 */
	public static final String T_GET_RUN_P = "0420";

	/**
	 * 保存运行控制参数
	 */
	public static final String T_SAVE_RUN_P = "0421";

	/**
	 * 获取时间控制参数
	 */
	public static final String T_GET_TIME_P = "0430";

	/**
	 * 保存时间控制参数
	 */
	public static final String T_SAVE_TIME_P = "0431";

	/**
	 * 获取测试功能参数
	 */
	public static final String T_GET_TEST_P = "0440";

	/**
	 * 保存测试功能参数
	 */
	public static final String T_SAVE_TEST_P = "0441";

	/**
	 * 开始请求监控数据
	 */

	public static final String M_START_MON = "0510";

	/**
	 * 结束请求监控数据
	 */
	public static final String M_END_MON = "0511";

	/**
	 * 开始请求驱动状态
	 */
	public static final String M_START_DRIVE = "0520";

	/**
	 * 结束请求驱动状态
	 */
	public static final String M_END_DRIVE = "0521";

	/**
	 * 开始请求端口状态1
	 */

	public static final String M_START_PORT_1 = "0530";
	/**
	 * 结束请求端口状态1
	 */

	public static final String M_END_PORT_1 = "0531";

	/**
	 * 开始请求端口状态2
	 */

	public static final String M_START_PORT_2 = "0532";

	/**
	 * 结束请求端口状态2
	 */
	public static final String M_END_PORT_2 = "0533";

	/**
	 * 开始请求召唤->上召唤
	 */
	public static final String M_START_UP_CALL = "0540";

	/**
	 * 结束请求召唤->上召唤
	 */
	public static final String M_END_UP_CALL = "0541";

	/**
	 * 开始请求召唤->下召唤
	 */
	public static final String M_START_DOWN_CALL = "0542";

	/**
	 * 结束请求召唤->下召唤
	 */
	public static final String M_END_DOWN_CALL = "0543";

	/**
	 * 开始请求指令数据
	 */
	public static final String M_START_COMMAND = "0550";

	/**
	 * 结束请求指令数据
	 */
	public static final String M_END_COMMAND = "0551";

	/**
	 * 获取故障数据
	 */
	public static final String B_GET_TROUBLE = "0610";
	/**
	 * 获取故障详细数据
	 */

	public static final String B_GET_TROUBLE_D = "0611";

	public static final String A_TEST_SEND = "0700";// 测试发送一字节数据

	public static final String A_TEST_RECEIVE = "0701";// 测试接受数据

	// --------------UI操作LOG-----------------//编码

	public static final String UI_LOGIN = "01";// 主页(登陆)
	public static final String UI_LOGIN_SUCCESS = "02";// 登陆成功
	public static final String UI_INPUT_DEVICE_NO = "03";// 输入设备授权码
	public static final String UI_CODE_SCAN = "04";// 二维码扫描
	public static final String UI_CONFORM_SCAN = "05";// 确认扫描结果
	public static final String UI_DEVICE_DISPLAY = "06";// 电梯设备信息展现
	public static final String UI_SETTING_HOME = "07";// 设置首页
	public static final String UI_BASIC_P = "08";// 基本参数
	public static final String UI_ELEC_P = "09";// 电机参数
	public static final String UI_AUTO_STUDY_ELC = "10";// 自学习-电机
	public static final String UI_AUTO_STUDY_WELL = "11";// 自学习-进道
	public static final String UI_ELE_AUTO_STUDY_P = "12";// 电机自学习参数
	public static final String UI_WELL_AUTO_STUDY_P = "13";// 进道自学习参数

	public static final String UI_FLOOR_SET_FRONT = "14";// 楼层前后门设置-前门

	public static final String UI_FLOOR_SET_BACK = "15";// 楼层前后门设置-后门

	public static final String UI_FLOOR_DISPLAY_SET = "16";// 楼层显示设置
	public static final String UI_MAINBOARD_INPUT_SET = "17";// 主板输入逻辑设置

	public static final String UI_CHANGE_DEVICE = "18";// 设备更换
	public static final String UI_TEST_HOME = "19";// 测试首页
	public static final String UI_VETOR_CONTROL_P = "20";// 矢量控制参数
	public static final String UI_RUN_CONTROL_P = "21";// 运行控制参数
	public static final String UI_TIME_CONTROL_P = "22";// 时间控制参数
	public static final String UI_TEST_FUNCTION_P = "23";// 测试功能参数
	public static final String UI_FAST_DEBUG = "24";// 快速调试
	public static final String UI_MONITOR_HOME = "25";// 监控首页
	public static final String UI_DRIVER_STATE = "26";// 驱动状态
	public static final String UI_PORT_STATE_1 = "27";// 端口状态1
	public static final String UI_PORT_STATE_2 = "28";// 端口状态2
	public static final String UI_COMMAND = "29";// 指令
	public static final String UI_UP_CALL = "30";// 上召唤
	public static final String UI_DOWN_CALL = "31";// 下召唤
	public static final String UI_TROUBLE_HOME = "32";// 故障首页
	public static final String UI_TROUBLE_DETAIL = "33";// 故障明细
	public static final String UI_SOFT_ABOUT = "34";// 软件相关
	public static final String UI_SOFT_UPDATE = "35";// 软件升级
	public static final String UI_LOGINOUT = "36";// 退出登录

	/**
	 * 检查接收的数据是否合法
	 * 
	 * @author 泰得利通 wanglu
	 * @param hex
	 * @return
	 */
	private static boolean checkReceive(String hexStr) {
		return hexStr.startsWith(PROTOCOL_HEADER);
	}

	/**
	 * 解析数据长度
	 * 
	 * @author 泰得利通 wanglu
	 * @param hexStr
	 * @return
	 */
	public static int getDataLength(String hexStr) {

		String hexLen = hexStr.substring(PROTOCOL_HEADER.length(),
				PROTOCOL_HEADER.length() + 4);

		return DataPaseUtil.hexStrToInt(hexLen);

	}

	/**
	 * 解析返回数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param hexStr
	 *            返回数据
	 * @return ReponseData 返回包装对象
	 */
	public static List<ResponseData> paseData(String hexStr) {

		String recePackageData[] = hexStr.split(PROTOCOL_HEADER);// 粘包处理
		List<ResponseData> responseDatas = new ArrayList<ResponseData>();
		if (recePackageData != null) {
			for (String packageData : recePackageData) {
				
				if(packageData.equals("")){
					continue;
				}
				packageData=PROTOCOL_HEADER+packageData;
				if (checkReceive(packageData)) {// 检查是否符合数据格式
					ResponseData reponseData = new ResponseData();
					int len = getDataLength(packageData);// 长度

					String enData = packageData.substring(PROTOCOL_HEADER
							.length() + 4);// 加密的数据部分

					String desData = Des.deHexStr(enData).substring(0,
							len * 2 + 4);// 4为功能代码的长度
											// 解密后的数据

					reponseData.setLen(len);
					reponseData.setEnHexStrData(enData);
					reponseData.setDesHexStrData(desData);
					reponseData.setFunctionCode(desData.substring(0, 4));// 功能代码
					reponseData.setReslutCode(DataPaseUtil.hexStrToInt(desData
							.substring(4, 6)) + "");// 返回结果

					if (!reponseData.isSuccess()) {// 失败,解析错误代码

						reponseData.setErrorCode(desData.substring(6, 8));// 返回结果错误代码
					} else {// 成功解析数据部分
						reponseData.setData(desData.substring(6));// 数据
					}
					LogUtil.i(LogUtil.LOG_TAG_REPONSE_DATA, "长度:" + len);
					LogUtil.i(LogUtil.LOG_TAG_REPONSE_DATA, "密文数据:"
							+ reponseData.getEnHexStrData());
					LogUtil.i(LogUtil.LOG_TAG_REPONSE_DATA, "解密数据:"
							+ reponseData.getDesHexStrData());
					LogUtil.i(LogUtil.LOG_TAG_REPONSE_DATA, "功能代码:"
							+ reponseData.getFunctionCode());// 功能代码
					LogUtil.i(LogUtil.LOG_TAG_REPONSE_DATA, "结果代码:"
							+ reponseData.getReslutCode());
					LogUtil.i(LogUtil.LOG_TAG_REPONSE_DATA, "错误代码:"
							+ reponseData.getErrorCode());
					if (reponseData.isSuccess()) {
						LogUtil.i(LogUtil.LOG_TAG_REPONSE_DATA, "数据:"
								+ reponseData.getData());
					}
					responseDatas.add(reponseData);
				}

			}
		}
		return responseDatas;

	}
}


