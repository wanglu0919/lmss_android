package com.challentec.lmss.bean;

import java.io.Serializable;

/**
 * 服务器返回数据封装
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ResponseData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6045861976160073973L;
	private int len;// 原始数据长度
	private String reslutCode;// 返回结果
	private String errorCode = "-1";// 错误代码
	private String data;// 数据
	private String enHexStrData;// 加密的16进制数据部分
	private String desHexStrData;// 16进制数据部分
	private String functionCode;// 功能代码

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getEnHexStrData() {
		return enHexStrData;
	}

	public void setEnHexStrData(String enHexStrData) {
		this.enHexStrData = enHexStrData;
	}

	public String getDesHexStrData() {
		return desHexStrData;
	}

	public void setDesHexStrData(String desHexStrData) {
		this.desHexStrData = desHexStrData;
	}

	public String getReslutCode() {
		return reslutCode;
	}

	public void setReslutCode(String reslutCode) {
		this.reslutCode = reslutCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 是否成功
	 * 
	 * @author 泰得利通 wanglu
	 * @return true成功 false失败
	 */
	public boolean isSuccess() {

		if (reslutCode.equals("0")) {
			return true;
		} else if (reslutCode.equals("1")) {
			return false;
		} else {
			return false;
		}

	}

}
