package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.UnitUtil;

/**
 * 测试->矢量控制参数 解析
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class VecotorControlParamsParser {

	public static List<ParamsItem> getParamsItems(Context context, String data,
			int itemNameIds[]) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		for (int i = 0; i < itemNameIds.length; i++) {
			ParamsItem item = new ParamsItem();
			item.setItemName(context.getString(itemNameIds[i]));
			item.setOrder(i);
			switch (i) {
			case 0:// 速度环比例增益1
				item.setItemValue(DataPaseUtil.getDataInt(data, 0, 4) + "");
				item.setLimit(0, 800);
				break;
			case 1:// 速度环积分时间1
				item.setItemValue(DataPaseUtil.getDataInt(data, 4, 8) + "");
				item.setLimit(0, 800);
				break;
			case 2:// 速度环饱和上限1
				item.setItemValue(DataPaseUtil.getDataInt(data, 8, 12) + "");
				item.setLimit(0, 800);
				break;
			case 3:// 切换频率
				item.setItemValue(DataPaseUtil.getDataFloat(data, 12, 16, 2,1)
						+ "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.HZ));
				item.setLimit((float) 0.0, (float) 20.0);
				item.setAccuracy(1);
				break;
			case 4:// 速度环比例增益2
				item.setItemValue(DataPaseUtil.getDataInt(data, 16, 20) + "");
				item.setLimit(0, 800);
				break;
			case 5:// 速度环积分时间2
				item.setItemValue(DataPaseUtil.getDataInt(data, 20, 24) + "");
				item.setLimit(0, 800);
				break;
			case 6:// 速度环饱和上限2
				item.setItemValue(DataPaseUtil.getDataInt(data, 24, 28) + "");
				item.setLimit(0, 800);
				break;
			case 7:// 电流环比例增益
				item.setItemValue(DataPaseUtil.getDataInt(data, 28, 32) + "");
				item.setLimit(0, 800);

				break;
			case 8:// 电流环积分时间
				item.setItemValue(DataPaseUtil.getDataInt(data, 32, 36) + "");
				item.setLimit(0, 800);
				break;
			case 9:// 电流环饱和上限
				item.setItemValue(DataPaseUtil.getDataInt(data, 36, 40) + "");
				item.setLimit(0, 800);
				break;
			case 10:// 转矩上限
				item.setItemValue(DataPaseUtil.getDataInt(data, 40, 42) + "");
				item.setLimit(50, 200);
				item.setUnit("%");
				break;
			case 11:// 零伺服电流系数
				item.setItemValue(DataPaseUtil.getDataInt(data, 42, 46) + "");
				item.setLimit(0, 800);
				break;
			case 12:// 零伺服速度环Kp
				item.setItemValue(DataPaseUtil.getDataInt(data, 46, 50) + "");
				item.setLimit(0, 800);
				break;
			case 13:// 零伺服速度环Ki
				item.setItemValue(DataPaseUtil.getDataInt(data, 50, 54) + "");
				item.setLimit(0, 800);
				break;
			case 14:// 力矩加速时间
				item.setItemValue(DataPaseUtil.getDataFloat(data, 54, 58, 2,1)
						+ "");
				item.setAccuracy(1);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 1.0, (float) 100.0);
				break;
			case 15:// 力矩减速时间
				item.setItemValue(DataPaseUtil.getDataFloat(data, 58, 62, 2,1)
						+ "");
				item.setAccuracy(1);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 1.0, (float) 100.0);
				break;
			case 16:// 同步机电流滤波常数
				item.setItemValue(DataPaseUtil.getDataInt(data, 62, 64) + "");
				item.setLimit(1, 128);
				break;
			}

			paramsItems.add(item);

		}

		return paramsItems;

	}

	public static String getHexSaveData(List<ParamsItem> paramsItems) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < paramsItems.size(); i++) {

			ParamsItem item = paramsItems.get(i);
			String itemValue = item.getItemValue();
			String hexData = "";

			switch (i) {
			case 0:// 速度环比例增益1
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 1:// 速度环积分时间1
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 2:// 速度环饱和上限1
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 3:// 切换频率
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 4:// 速度环比例增益2
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 5:// 速度环积分时间2
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 6:// 速度环饱和上限2
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 7:// 电流环比例增益
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 8:// 电流环积分时间
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 9:// 电流环饱和上限
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 10:// 转矩上限
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 11:// 零伺服电流系数
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 12:// 零伺服速度环Kp
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 13:// 零伺服速度环Ki
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 14:// 力矩加速时间
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 15:// 力矩减速时间
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 16:// 同步机电流滤波常数
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			}

			sb.append(hexData);
		}

		return sb.toString();
	}

}
