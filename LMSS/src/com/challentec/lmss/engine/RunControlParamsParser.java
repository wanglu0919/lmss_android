package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.UnitUtil;

/**
 * 测试_运行控制参数
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class RunControlParamsParser {

	public static List<ParamsItem> getParamsItems(Context context, String data,
			int itemNameIds[]) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		for (int i = 0; i < itemNameIds.length; i++) {
			ParamsItem item = new ParamsItem();
			item.setItemName(context.getString(itemNameIds[i]));
			item.setOrder(i);
			switch (i) {
			case 0:// 启动速度
				item.setItemValue(DataPaseUtil.getDataFloat(data, 0, 4, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SPEED));
				item.setLimit((float) 0.00, (float) 1.00);
				break;
			case 1:// 启动速度保持时间
				item.setItemValue(DataPaseUtil.getDataFloat(data, 4, 8, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 2.00);
				break;
			case 2:// 开始零速输出时间
				item.setItemValue(DataPaseUtil.getDataFloat(data, 8, 12, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 2.00);
				break;
			case 3:// 曲线给定延时
				item.setItemValue(DataPaseUtil.getDataFloat(data, 12, 16, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 2.00);
				break;
			case 4:// 加速度斜率 16 20
				item.setItemValue(DataPaseUtil.getDataFloat(data, 16, 20, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setLimit((float) 0.00, (float) 5.00);

				break;
			case 5:// 拐点加速时间1
				item.setItemValue(DataPaseUtil.getDataFloat(data, 20, 24, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 5.00);
				break;
			case 6:// 拐点加速时间2
				item.setItemValue(DataPaseUtil.getDataFloat(data, 24, 28, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 5.00);
				break;
			case 7:// 减速度斜率
				item.setItemValue(DataPaseUtil.getDataFloat(data, 28, 32, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setLimit((float) 0.00, (float) 5.00);
				break;
			case 8:// 拐点减速时间1
				item.setItemValue(DataPaseUtil.getDataFloat(data, 32, 36, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 5.00);
				break;
			case 9:// 拐点减速时间2
				item.setItemValue(DataPaseUtil.getDataFloat(data, 36, 40, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 0.00, (float) 5.00);
				break;
			case 10:// 特殊减速度斜率 48 56
				item.setItemValue(DataPaseUtil.getDataFloat(data, 40, 44, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setLimit((float) 0.00, (float) 5.00);
				break;
			case 11:// 停车距离裕量
				item.setItemValue(DataPaseUtil.getDataInt(data, 44, 46) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.MILLIMETRE));
				item.setLimit(0, 50);
				break;
			case 12:// 再平层速度
				item.setItemValue(DataPaseUtil.getDataFloat(data, 46, 50, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SPEED));
				item.setLimit((float) 0.00, (float) 0.30);
				break;
			case 13:// 检修速度
				item.setItemValue(DataPaseUtil.getDataFloat(data, 50, 54, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SPEED));
				item.setLimit((float) 0.00, (float) 0.30);
				break;
			case 14:// 返平层速度
				item.setItemValue(DataPaseUtil.getDataFloat(data, 54, 58, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SPEED));
				item.setLimit((float) 0.00, (float) 0.30);
				break;
			case 15:// 平层调整
				item.setItemValue(DataPaseUtil.getDataFloat(data, 58, 62, 2, 2)
						+ "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.MILL));
				item.setLimit((float) 0.00, (float) 0.30);
				break;

			}

			paramsItems.add(item);

		}

		return paramsItems;

	}

	/**
	 * 构建提交数据数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param paramsItems
	 * @return
	 */
	public static String getHexSaveData(List<ParamsItem> paramsItems) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < paramsItems.size(); i++) {
			ParamsItem paramsItem = paramsItems.get(i);
			String itemValue = paramsItem.getItemValue();
			String hexData = "";

			switch (i) {
			case 0:// 启动速度
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 1:// 启动速度保持时间
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 2:// 开始零速输出时间
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 3:// 曲线给定延时
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 4:// 加速度斜率 16 24
				
				hexData=DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 5:// 拐点加速时间1
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 6:// 拐点加速时间2
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 7:// 减速度斜率 32 40
				hexData=DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 8:// 拐点减速时间1
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 9:// 拐点减速时间2
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 10:// 特殊减速度斜率 48 56
				hexData=DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 11:// 停车距离裕量
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 0);
				break;
			case 12:// 再平层速度
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 13:// 检修速度
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 14:// 返平层速度
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 15:// 平层调整
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;

			}

			sb.append(hexData);

		}

		return sb.toString();
	}

}
