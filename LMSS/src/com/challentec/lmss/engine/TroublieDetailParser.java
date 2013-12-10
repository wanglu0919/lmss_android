package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.UnitUtil;

/**
 * 故障详细信息解析
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class TroublieDetailParser {

	public static List<ParamsItem> getParamsItems(Context context, String data,
			int itemNameIds[]) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		for (int i = 0; i < itemNameIds.length; i++) {
			ParamsItem item = new ParamsItem();// 系统时间
			item.setValueType(ParamsItem.VAALUE_TYPE_TROUBLE);// 设置类型为故障类型
			item.setItemName(context.getString(itemNameIds[i]));

			switch (i) {
			case 0:// 故障日期
				item.setItemValue(data.substring(0, 4) + "."
						+ data.substring(4, 6) + "." + data.substring(6, 8));
				item.setDataType(ParamsItem.VALUE_STING_DATE);
				break;
			case 1:// 故障时间
				item.setItemValue(data.substring(8, 10) + ":"
						+ data.substring(10, 12) + ":" + data.substring(12, 14));
				item.setDataType(ParamsItem.VALUE_STING_TIME);
				break;
			case 2:// 故障代码
				item.setItemValue(DataPaseUtil.getDataInt(data, 14, 16) + "");
				item.setLimit(0, 99);
				break;
			case 3:// 当前位置 3 个字节
				item.setItemValue(DataPaseUtil.getDataInt(data, 16, 22) + "");
				item.setLimit(0, 160000);
				break;
			case 4:// 当前楼层
				item.setItemValue(DataPaseUtil.getDataInt(data, 22, 24) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.FLOOR));
				item.setLimit(1, 40);
				break;

			case 5:// 输入信息 4字节
				item.setItemValue(data.substring(24, 32).toUpperCase());
				item.setDataType(ParamsItem.VALUE_STING);
				break;

			case 6:// 输出信息
				item.setItemValue(data.substring(32, 36).toUpperCase());
				item.setDataType(ParamsItem.VALUE_STING);
				break;

			case 7:// 曲线信息
				String qx = data.substring(36, 38);
				if (qx.equals("00")) {
					item.setItemValue(context
							.getString(R.string.trouble_detail_add_speed));// 加速
				} else if (qx.equals("01")) {
					item.setItemValue(context
							.getString(R.string.trouble_detail_fix_speed));// 额定
				} else if (qx.equals("02")) {
					item.setItemValue(context
							.getString(R.string.trouble_detail_slow_speed));// 减速
				}else{
					item.setItemValue("Error");
				}
				item.setDataType(ParamsItem.VALUE_STING);
				break;

			case 8:// 给定速度 3个字节，整数2字节
				item.setItemValue(DataPaseUtil.getDataFloat(data, 38, 44, 4, 2));
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.UNIT_Z));
				item.setLimit((float)0.00, (float)500.00);
				break;

			case 9:// 反馈速度 3个字节，整数2字节
				item.setItemValue(DataPaseUtil.getDataFloat(data, 44, 50, 4, 2));
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.UNIT_Z));
				item.setLimit((float)0.00, (float)500.00);
				break;

			case 10:// 母线电压
				item.setItemValue(DataPaseUtil.getDataInt(data, 50, 54) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.FT));
				item.setLimit(0, 1000);
				break;

			case 11:// 输出电流
				item.setItemValue(DataPaseUtil.getDataInt(data, 54, 58) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.AP));
				item.setLimit(0, 500);
				break;

			case 12:// 输出频率
				item.setItemValue(DataPaseUtil.getDataInt(data, 58, 60) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.HZ));
				item.setLimit(0, 50);
				break;

			case 13:// 转矩电流
				item.setItemValue(DataPaseUtil.getDataInt(data, 60, 64) + "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.AP));
				item.setLimit(0, 500);
				break;

			}

			paramsItems.add(item);

		}

		return paramsItems;

	}

}
