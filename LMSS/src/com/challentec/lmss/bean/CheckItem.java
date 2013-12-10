package com.challentec.lmss.bean;

import java.io.Serializable;

/**
 * 可选项封装
 * @author 泰得利通 wanglu
 *
 */
public class CheckItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1700311222680437255L;
	private boolean isCheck;//是选中
	private String itemName;//可选项名称
	private String itemValue;//可选项值
	
	public CheckItem(){}
	public CheckItem(String itemName,String itemValue  ){
		this.itemName=itemName;
		this.itemValue=itemValue;
	}
	
	
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	
	
	
	
}
