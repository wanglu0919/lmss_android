package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.util.DataPaseUtil;

/**
 * 楼层显示设定值
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class FloorDisplayParser {

	public static List<ParamsItem> getParamsItems(Context context,
			String parseData) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		String rex = "(\\w{2})";

		Pattern p = Pattern.compile(rex);

		Matcher matcher = p.matcher(parseData);
		int i = 1;
		while (matcher.find()) {
			ParamsItem paramsItem = new ParamsItem();
			paramsItem.setOrder(i - 1);
			paramsItem.setItemName(i
					+ context.getString(R.string.floor_display_set_lable));
			String data = matcher.group();
			paramsItem.setItemValue(DataPaseUtil.hexStrToInt(data) + "");
			paramsItem.setLimit(0, 99);
			i++;

			paramsItems.add(paramsItem);
		}

		return paramsItems;

	}

	public static String getHexSaveData(List<ParamsItem> paramsItems) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < paramsItems.size(); i++) {
			String itemValue = paramsItems.get(i).getItemValue();
			String hexData = DataPaseUtil.getHexStr(
					Integer.parseInt(itemValue), 1);
			sb.append(hexData);

		}

		return sb.toString();
	}

}
