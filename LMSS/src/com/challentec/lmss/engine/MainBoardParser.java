package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.util.DataPaseUtil;

/**
 * 主板输入逻辑解析类
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class MainBoardParser {

	public static List<ParamsItem> getParamsItems(Context context,
			String parseData, int itemNameIds[]) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		for (int i = 0; i < itemNameIds.length; i++) {
			ParamsItem item = new ParamsItem();// 系统时间
			item.setItemName(context.getString(itemNameIds[i]));
			item.setOrder(i);
			switch (i) {
			case 0:// X1--X8设定值
				item.setItemValue(parseData.substring(0, 2).toUpperCase());
				item.setValueType(ParamsItem.VALUE_TYPE_HEX);
				item.setHexLimit(0, 255, 1);
				break;
			case 1:// X11--X18设定值
				item.setItemValue(parseData.substring(2, 4).toUpperCase());
				item.setValueType(ParamsItem.VALUE_TYPE_HEX);
				item.setHexLimit(0, 255, 1);
				break;
			case 2:// X21--X28设定值
				item.setItemValue(parseData.substring(4, 6).toUpperCase());
				item.setValueType(ParamsItem.VALUE_TYPE_HEX);
				item.setHexLimit(0, 255, 1);

				break;
			case 3:// X27功能设定值
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 6, 8) + "");// 十进制显示
				item.setLimit(0, 50);
				break;
			case 4:// X28功能设定值
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 8, 10)
						+ "");// 十进制显示
				item.setLimit(0, 50);
				break;
			case 5:// Y7功能设定值
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 10, 12)
						+ "");// 十进制显示
				item.setLimit(0, 15);
				break;
			case 6:// Y8功能设定值
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 12, 14)
						+ "");// 十进制显示
				item.setLimit(0, 15);
				break;

			}

			paramsItems.add(item);

		}

		return paramsItems;

	}

	/**
	 * 获取保存数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param paramsItems
	 * @return
	 */
	public static String getSaveHexData(List<ParamsItem> paramsItems) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < paramsItems.size(); i++) {
			ParamsItem paramsItem = paramsItems.get(i);
			String itemValue = paramsItem.getItemValue().toLowerCase();
			String hexData = "";

			switch (i) {
			case 0:// X1--X8设定值
				hexData = itemValue;
				break;
			case 1:// X11--X18设定值
				hexData = itemValue;
				break;
			case 2:// X21--X28设定值
				hexData = itemValue;

				break;
			case 3:// X27功能设定值
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 4:// X28功能设定值
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 5:// Y7功能设定值
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 6:// Y8功能设定值
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;

			}

			sb.append(hexData);

		}

		return sb.toString();
	}

}
