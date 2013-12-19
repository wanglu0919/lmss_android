package com.challentec.lmss.bean;


/**
 * 
 * @author wanglu 泰得利通
 * 软件更新信息
 *
 */
public class UpdateInfo {
	private String version;//版本
	private String description;//说嘛
	private String url1;//内网URL地址
	private String url2;//外围地址
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl1() {
		return url1;
	}
	public void setUrl1(String url1) {
		this.url1 = url1;
	}
	public String getUrl2() {
		return url2;
	}
	public void setUrl2(String url2) {
		this.url2 = url2;
	}
	
}
