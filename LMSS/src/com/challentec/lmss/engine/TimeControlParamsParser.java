package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.UnitUtil;

/**
 * 时间控制参数解析类
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class TimeControlParamsParser {

	public static List<ParamsItem> getParamsItems(Context context, String data,
			int itemNameIds[]) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		for (int i = 0; i < itemNameIds.length; i++) {
			ParamsItem item = new ParamsItem();
			item.setItemName(context.getString(itemNameIds[i]));
			item.setOrder(i);
			switch (i) {
			case 0:// 单程运行时间限制
				item.setItemValue(DataPaseUtil.getDataInt(data, 0, 2) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit(0, 40);
				break;
			case 1:// 启动防颠延时
				item.setItemValue(DataPaseUtil.getDataFloat(data, 2, 6, 2,2) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 5.00);
				item.setAccuracy(2);
				break;
			case 2:// 曲线给定延时
				item.setItemValue(DataPaseUtil.getDataFloat(data, 6, 10, 2,2)
						+ "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 5.00);
				item.setAccuracy(2);
				break;
			case 3:// 抱闸打开延时
				item.setItemValue(DataPaseUtil.getDataFloat(data, 10, 14, 2,2)
						+ "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 5.00);
				item.setAccuracy(2);
				break;
			case 4:// 抱闸释放延时
				item.setItemValue(DataPaseUtil.getDataFloat(data, 14, 18, 2,2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 5.00);
				break;
			case 5:// 方向撤销延时
				item.setItemValue(DataPaseUtil.getDataInt(data, 18, 20) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit(0, 50);
				break;
			case 6:// 平层感应器延时
				item.setItemValue(DataPaseUtil.getDataInt(data, 20, 22) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.KILLMI));
				item.setLimit(10, 50);
				break;
			case 7:// 检修停车延时
				item.setItemValue(DataPaseUtil.getDataFloat(data, 22, 26, 2,2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.0, (float) 5.00);
				break;
			case 8:// 自动关门时间
				item.setItemValue(DataPaseUtil.getDataInt(data, 26, 30) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit(0, 500);
				break;
			case 9:// 返回基站延时 3个字节，整数2个字节，小数1 个字节
				item.setItemValue(DataPaseUtil.getDataFloat(data, 30, 36, 4,1)
						+ "");
				item.setAccuracy(1);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.0, (float)500.0);
				break;
			case 10:// 节能控制时间
				item.setItemValue(DataPaseUtil.getDataInt(data, 36, 40) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit(0, 999);
				break;
			case 11:// 司机、自动转换时间
				item.setItemValue(DataPaseUtil.getDataFloat(data, 40, 44, 2,2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 5.00);
				break;
			case 12:// UPS运行保护时间
				item.setItemValue(DataPaseUtil.getDataInt(data, 44, 46) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit(0, 199);
				break;
			case 13:// 到站钟维持时间
				item.setItemValue(DataPaseUtil.getDataFloat(data, 46, 50, 2,2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 5.00);
				break;
			case 14:// 消防员运行延时
				item.setItemValue(DataPaseUtil.getDataInt(data, 50, 52) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit(0, 199);
				break;

			}

			paramsItems.add(item);

		}

		return paramsItems;

	}

	/**
	 * 构建16进制
	 * 
	 * @author 泰得利通 wanglu
	 * @param paramsItems
	 * @return
	 */
	public static String getTimeHexData(List<ParamsItem> paramsItems) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < paramsItems.size(); i++) {
			ParamsItem paramsItem = paramsItems.get(i);
			String itemValue = paramsItem.getItemValue();
			String hexData = "";
			switch (i) {
			case 0:// 单程运行时间限制
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 1:// 启动防颠延时
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 2:// 曲线给定延时
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 3:// 抱闸打开延时
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 4:// 抱闸释放延时
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 5:// 方向撤销延时
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 6:// 平层感应器延时
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 7:// 检修停车延时
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 8:// 自动关门时间
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 9:// 返回基站延时 3个字节，整数2个字节，小数1 个字节
				hexData = DataPaseUtil.getHexStr(itemValue, 2, 1);
				break;
			case 10:// 节能控制时间
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 11:// 司机、自动转换时间
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 12:// UPS运行保护时间
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 13:// 到站钟维持时间
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 14:// 消防员运行延时
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;

			}
			sb.append(hexData);
		}

		return sb.toString();
	}

}
