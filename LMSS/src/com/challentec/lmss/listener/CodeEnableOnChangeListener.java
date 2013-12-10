package com.challentec.lmss.listener;

import com.challentec.lmss.bean.ParamsItem;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 编码短线检测使能
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class CodeEnableOnChangeListener implements OnCheckedChangeListener {
	private ParamsItem paramsItem;

	public CodeEnableOnChangeListener(ParamsItem paramsItem) {
		this.paramsItem = paramsItem;

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean checked) {

		if (checked) {
			paramsItem.setItemValue("1");
			paramsItem.setOFF_ON(true);// 关闭
		} else {
			paramsItem.setItemValue("0");
			paramsItem.setOFF_ON(false);// 关闭
		}
	}

}
