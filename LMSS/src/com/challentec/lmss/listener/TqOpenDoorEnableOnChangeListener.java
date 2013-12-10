package com.challentec.lmss.listener;

import com.challentec.lmss.bean.ParamsItem;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 提前开门
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class TqOpenDoorEnableOnChangeListener implements
		OnCheckedChangeListener {
	private ParamsItem paramsItem;

	public TqOpenDoorEnableOnChangeListener(ParamsItem paramsItem) {
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
