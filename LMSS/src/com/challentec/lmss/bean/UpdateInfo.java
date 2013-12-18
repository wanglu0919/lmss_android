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
	private String url;//URL地址
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
