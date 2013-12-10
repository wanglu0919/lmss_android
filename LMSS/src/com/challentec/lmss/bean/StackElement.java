package com.challentec.lmss.bean;

import android.view.View;

/**
 * tab页面子activity管理栈封装bean
 * @author 泰得利通 wanglu
 *
 */
public class StackElement {

	private String tag;

	private View view;

	public StackElement(String tag, View view) {
		this.tag = tag;
		this.view = view;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setView(View view) {
		this.view = view;
	}

	public String getTag() {
		return tag;
	}

	public View getView() {
		return view;
	}

}
