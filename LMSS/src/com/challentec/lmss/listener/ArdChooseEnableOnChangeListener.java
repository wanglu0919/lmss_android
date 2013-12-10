package com.challentec.lmss.listener;

import com.challentec.lmss.bean.ParamsItem;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * ARD选折
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ArdChooseEnableOnChangeListener implements
		OnCheckedChangeListener {
	private ParamsItem paramsItem;

	public ArdChooseEnableOnChangeListener(ParamsItem paramsItem) {
		this.paramsItem = paramsItem;

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean checked) {

		if (checked) {
			paramsItem.setItemValue("01");
			paramsItem.setOFF_ON(true);
		} else {
			paramsItem.setItemValue("00");
			paramsItem.setOFF_ON(false);
		}
	}

}
