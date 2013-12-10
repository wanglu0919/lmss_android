package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.UnitUtil;

/**
 * 电梯自学习-参数解析类
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ElecAutoStudyParamsParser {

	public static List<ParamsItem> getParamsItems(Context context, String data,
			int itemNameIds[]) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		for (int i = 0; i < itemNameIds.length; i++) {
			ParamsItem item = new ParamsItem();
			item.setItemName(context.getString(itemNameIds[i]));
			item.setOrder(i);
			switch (i) {
			case 0:// 异步机空载电流
				item.setItemValue(DataPaseUtil.getDataInt(data, 0, 2) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.AP));
				item.setLimit(10, 100);
				break;
			case 1:// 异步机定子电阻
				item.setItemValue(DataPaseUtil.getDataFloat(data, 2, 6, 2,2) + "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.OM));
				item.setLimit((float) 0.00, (float) 20.00);
				break;
			case 2:// 异步机转子电阻
				item.setItemValue(DataPaseUtil.getDataFloat(data, 6, 10, 2,2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.OM));
				item.setLimit((float) 0.00, (float) 20.00);
				break;
			case 3:// 异步机漏感抗
				item.setItemValue(DataPaseUtil.getDataFloat(data, 10, 14, 2,2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.OM));
				item.setLimit((float) 0.00, (float) 20.00);
				break;
			case 4:// 异步机互感抗
				item.setItemValue(DataPaseUtil.getDataFloat(data, 14, 18, 2,2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.OM));
				item.setLimit((float) 0.00, (float) 20.00);
				break;
			case 5:// 同步机初始位置 3字节
				item.setItemValue(DataPaseUtil.getDataFloat(data, 18, 24, 4,2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.JD));
				item.setLimit((float) 0.00, (float) 359.99);
				break;

			case 6:// 同步机断电位置 3字节
				item.setItemValue(DataPaseUtil.getDataFloat(data, 24, 30, 4,2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.JD));
				item.setLimit((float) 0.00, (float) 359.99);
				break;
			case 7:// 同步机Q轴电感
				item.setItemValue(DataPaseUtil.getDataFloat(data, 30, 34, 2,1)
						+ "");
				item.setAccuracy(1);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.HH));
				item.setLimit((float) 0.0, (float) 100.0);
				break;
			case 8:// 同步机D轴电感
				item.setItemValue(DataPaseUtil.getDataFloat(data, 34, 38, 2,2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.HH));
				item.setLimit((float) 0.0, (float) 100.00);
				break;
			case 9:// 自学习故障编码
				item.setItemValue(DataPaseUtil.getDataInt(data, 38, 40) + "");
				item.setLimit(0, 10);
				break;
			case 10:// 相间线圈电阻

				item.setItemValue(DataPaseUtil.getDataFloat(data, 40, 44, 2,1)
						+ "");
				item.setAccuracy(1);
				item.setLimit((float) 5.0, (float) 50.0);
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
			String itemValue = paramsItem.getItemValue();
			String hexData = "";

			switch (i) {
			case 0:// 异步机空载电流
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 1:// 异步机定子电阻
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 2:// 异步机转子电阻
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 3:// 异步机漏感抗
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 4:// 异步机互感抗
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 5:// 同步机初始位置 3字节
				hexData = DataPaseUtil.getHexStr(itemValue, 2, 1);
				break;

			case 6:// 同步机断电位置 3字节
				hexData = DataPaseUtil.getHexStr(itemValue, 2, 1);
				break;
			case 7:// 同步机Q轴电感
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 8:// 同步机D轴电感
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 9:// 自学习故障编码
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 10:// 相间线圈电阻
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);

				break;
			}
			sb.append(hexData);

		}

		return sb.toString();
	}

}
