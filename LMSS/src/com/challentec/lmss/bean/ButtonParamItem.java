package com.challentec.lmss.bean;

/**
 * 按钮类型参数封装bean,如楼层前后门设置，召唤指令
 * @author 泰得利通 wanglu
 *
 */
public class ButtonParamItem {

	private String itemName;
	private String itemValue;
	private boolean isHightLinght;//是否高亮显示
	public boolean isHightLinght() {
		return isHightLinght;
	}
	public void setHightLinght(boolean isHightLinght) {
		this.isHightLinght = isHightLinght;
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
