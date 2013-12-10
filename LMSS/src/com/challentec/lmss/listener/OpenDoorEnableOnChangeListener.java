package com.challentec.lmss.listener;

import com.challentec.lmss.bean.ParamsItem;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 开门使能
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class OpenDoorEnableOnChangeListener implements OnCheckedChangeListener {
	private ParamsItem paramsItem;

	public OpenDoorEnableOnChangeListener(ParamsItem paramsItem) {
		this.paramsItem = paramsItem;

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean checked) {

		if (checked) {
			paramsItem.setItemValue("1");
			paramsItem.setOFF_ON(true);

		} else {
			paramsItem.setItemValue("0");
			paramsItem.setOFF_ON(false);
			
		}
	}

}
