package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.listener.CallFunctionEnableOnChangeListener;
import com.challentec.lmss.listener.OpenDoorEnableOnChangeListener;
import com.challentec.lmss.listener.OverLoadEnableOnChangeListener;
import com.challentec.lmss.listener.TestFunctionEnableOnChangeListener;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.UnitUtil;

/**
 * 测试->测试功能参数解析
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class TestFunctionParamsParser {

	public static List<ParamsItem> getParamsItems(Context context, String data,
			int itemNameIds[]) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		for (int i = 0; i < itemNameIds.length; i++) {
			ParamsItem item = new ParamsItem();// 系统时间
			item.setItemName(context.getString(itemNameIds[i]));
			item.setOrder(i);
			switch (i) {
			case 0:// 内指令登记
				item.setItemValue(DataPaseUtil.getDataInt(data, 0, 2) + "");
				item.setLimit(1, AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY)>40 ? 40 :AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY));
				break;
			case 1:// 上召唤登记
				item.setItemValue(DataPaseUtil.getDataInt(data, 2, 4) + "");
				item.setLimit(1, AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY)>40 ? 39 :AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY)-1);
				break;
			case 2:// 下召唤登记
				item.setItemValue(DataPaseUtil.getDataInt(data, 4, 6) + "");
				item.setLimit(2, AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY)>40 ? 40 :AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY));
				break;
			case 3:// 随机运行次数
				item.setItemValue(DataPaseUtil.getDataInt(data, 6, 10) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.TIME));
				item.setLimit(500, 10000);
				break;
			case 4:// 随机运行间隔
				item.setItemValue(DataPaseUtil.getDataInt(data, 10, 12) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit(0, 180);
				break;
			case 5:// 随机运行使能
				int runData = DataPaseUtil.getDataInt(data, 12, 14) & 1;// 取第一位
				item.setValueType(ParamsItem.VALUE_TYPE_ON_OFF);

				if (runData == 0) {
					item.setOFF_ON(false);
					item.setItemValue("0");
				} else if (runData == 1) {
					item.setOFF_ON(true);
					item.setItemValue("1");
				}
				item.setOnCheckedChangeListener(new TestFunctionEnableOnChangeListener(
						item));// 设置监听器
				break;
			case 6:// 召唤使能
				int callData = DataPaseUtil.getDataInt(data, 12, 14) >> 1 & 1;// 取第二位
				item.setValueType(ParamsItem.VALUE_TYPE_ON_OFF);
				if (callData == 0) {
					item.setOFF_ON(false);
					item.setItemValue("0");
				} else if (callData == 1) {
					item.setOFF_ON(true);
					item.setItemValue("1");
				}
				item.setOnCheckedChangeListener(new CallFunctionEnableOnChangeListener(
						item));
				break;
			case 7:// 开门使能
				int openData = DataPaseUtil.getDataInt(data, 12, 14) >> 2 & 1;// 取第三位
				item.setValueType(ParamsItem.VALUE_TYPE_ON_OFF);
				if (openData == 0) {
					item.setOFF_ON(false);
					item.setItemValue("0");
				} else if (openData == 1) {
					item.setOFF_ON(true);
					item.setItemValue("1");
				}
				item.setOnCheckedChangeListener(new OpenDoorEnableOnChangeListener(
						item));
				break;
			case 8:// 超载使能
				int loadData = DataPaseUtil.getDataInt(data, 12, 14) >> 3 & 1;// 取第四位
				item.setValueType(ParamsItem.VALUE_TYPE_ON_OFF);
				if (loadData == 0) {
					item.setOFF_ON(false);
					item.setItemValue("0");
				} else if (loadData == 1) {
					item.setOFF_ON(true);
					item.setItemValue("1");
				}
				item.setOnCheckedChangeListener(new OverLoadEnableOnChangeListener(
						item));
				break;
			}

			paramsItems.add(item);

		}

		return paramsItems;

	}

	/**
	 * 保存数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param paramsItems
	 * @return
	 */
	public static String getHexSaveData(List<ParamsItem> paramsItems) {
		StringBuffer sb = new StringBuffer();
		StringBuffer enableBinary = new StringBuffer();// 记录位数据
		for (int i = 0; i < paramsItems.size(); i++) {
			ParamsItem paramsItem = paramsItems.get(i);
			String itemValue = paramsItem.getItemValue();
			String hexData = "";
			switch (i) {

			case 0:// 内指令登记
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 1:// 上召唤登记
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 2:// 下召唤登记
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 3:// 随机运行次数
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 4:// 随机运行间隔
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 5:// 随机运行使能

				enableBinary.append(itemValue);
				break;
			case 6:// 召唤使能
				enableBinary.append(itemValue);
				break;
			case 7:// 开门使能
				enableBinary.append(itemValue);
				break;
			case 8:// 超载使能
				enableBinary.append(itemValue);

				hexData = DataPaseUtil.binaryToHex(enableBinary.reverse()
						.toString(), 1);
				break;

			}

			sb.append(hexData);

		}

		return sb.toString();
	}

}
