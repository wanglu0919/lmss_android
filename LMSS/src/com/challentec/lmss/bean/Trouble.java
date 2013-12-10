package com.challentec.lmss.bean;

/**
 * 故障
 * @author 泰得利通 wanglu
 *
 */
public class Trouble {

	private String t_id;//故障Id
	
	private String t_order;//故障序号
	private String t_time;//故障时间
	private String t_no;//故障代码
	public String getT_order() {
		return t_order;
	}
	public void setT_order(String t_order) {
		this.t_order = t_order;
	}
	public String getT_time() {
		return t_time;
	}
	public void setT_time(String t_time) {
		this.t_time = t_time;
	}
	public String getT_no() {
		return t_no;
	}
	public void setT_no(String t_no) {
		this.t_no = t_no;
	}
	public String getT_id() {
		return t_id;
	}
	public void setT_id(String t_id) {
		this.t_id = t_id;
	}
	
	
}
