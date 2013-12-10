package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.CheckItem;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.listener.CodeEnableOnChangeListener;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.UnitUtil;

/**
 * 设置-电机参数解析
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ElecParamsParser {

	public static List<ParamsItem> getParamsItems(Context context,
			String parseData, int itemNameIds[]) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		for (int i = 0; i < itemNameIds.length; i++) {
			ParamsItem item = new ParamsItem();
			item.setItemName(context.getString(itemNameIds[i]));
			item.setOrder(i);
			switch (i) {
			case 0:// 电机额定功率
				item.setItemValue(DataPaseUtil.getDataFloat(parseData, 0, 4, 2,
						2) + "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.KW));
				item.setLimit((float) 0.75, (float) 50.0);
				break;
			case 1:// 电机额定频率
				item.setItemValue(DataPaseUtil.getDataFloat(parseData, 4, 8, 2,
						1) + "");
				item.setAccuracy(1);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.HZ));
				item.setLimit((float) 5.0, (float) 60.0);
				break;
			case 2:// 电机额定转速
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 8, 12)
						+ "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.ZF));
				item.setLimit(10, 1500);
				break;
			case 3:// 电机额定电压
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 12, 16)
						+ "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.FT));
				item.setLimit(190, 480);
				break;
			case 4:// 电机额定电流
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 16, 20)
						+ "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.AP));
				item.setLimit(1, 300);
				break;
			case 5:// 电机极对数
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 20, 22)
						+ "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.APAIR));
				item.setLimit(2, 40);
				break;
			case 6:// 电机运行方向
				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);// 可选项
				List<CheckItem> dirCheckItems = new ArrayList<CheckItem>();
				CheckItem dirCheckItem1 = new CheckItem(
						context.getString(R.string.elec_check_run_dir_positive),
						"01");// 正
				CheckItem dirCheckItem2 = new CheckItem(
						context.getString(R.string.elec_check_run_dir_against),
						"00");// 反
				int dirData = DataPaseUtil.getDataInt(parseData, 22, 24) & 1;// 取第一位
				if (dirData == 0) {
					dirCheckItem2.setCheck(true);
				} else if (dirData == 1) {
					dirCheckItem1.setCheck(true);
				}
				dirCheckItems.add(dirCheckItem1);
				dirCheckItems.add(dirCheckItem2);

				item.setCheckItems(dirCheckItems);
				break;
			case 7:// 过载系数
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 24, 26)
						+ "");
				item.setLimit(100, 150);
				item.setUnit("%");
				break;
			case 8:// 过载时间
				item.setItemValue(DataPaseUtil.getDataFloat(parseData, 26, 30,
						2, 1) + "");
				item.setAccuracy(1);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SECONDS));
				item.setLimit((float) 5.0, (float) 120.0);
				break;
			case 9:// 编码类型
				String codeType = parseData.substring(30, 32);
				List<CheckItem> codeCheckItems = new ArrayList<CheckItem>();

				CheckItem codeCheckItem1 = new CheckItem("u.v.w", "00");
				CheckItem codeCheckItem2 = new CheckItem("sin.cos", "01");
				CheckItem codeCheckItem3 = new CheckItem("endat", "02");

				if (codeType.equals("00")) {
					codeCheckItem1.setCheck(true);
				} else if (codeType.equals("01")) {
					codeCheckItem2.setCheck(true);
				} else if (codeType.equals("02")) {
					codeCheckItem3.setCheck(true);
				}
				codeCheckItems.add(codeCheckItem1);
				codeCheckItems.add(codeCheckItem2);
				codeCheckItems.add(codeCheckItem3);
				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);
				item.setCheckItems(codeCheckItems);

				break;
			case 10:// 编码分辨率
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 32, 36)
						+ "");
				item.setLimit(500, 4096);
				break;
			case 11:// 编码方向

				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);// 可选项
				List<CheckItem> codeDirCheckItems = new ArrayList<CheckItem>();
				CheckItem codeDirCheckItem1 = new CheckItem(
						context.getString(R.string.elec_check_run_encode_positive),
						"1");
				CheckItem codeDirCheckItem2 = new CheckItem(
						context.getString(R.string.elec_check_run_encode_against),
						"0");
				int codeDirData = DataPaseUtil.getDataInt(parseData, 36, 38) & 1;// 取第一位
				if (codeDirData == 0) {
					codeDirCheckItem2.setCheck(true);
				} else if (codeDirData == 1) {
					codeDirCheckItem1.setCheck(true);
				}
				codeDirCheckItems.add(codeDirCheckItem1);
				codeDirCheckItems.add(codeDirCheckItem2);

				item.setCheckItems(codeDirCheckItems);

				break;
			case 12:// 编码断线检测使能

				item.setValueType(ParamsItem.VALUE_TYPE_ON_OFF);// 开关

				int enableData = DataPaseUtil.getDataInt(parseData, 36, 38) >> 1 & 1;// 取第二位
				if (enableData == 0) {
					item.setItemValue("0");
					item.setOFF_ON(false);// 关闭
				} else if (enableData == 1) {
					item.setItemValue("1");
					item.setOFF_ON(true);
				}

				item.setItemValue(parseData.substring(36, 38));
				item.setOnCheckedChangeListener(new CodeEnableOnChangeListener(
						item));

				break;
			case 13:// 编码器断线检测时间
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 38, 42)
						+ "");
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.KILLMI));
				item.setLimit(50, 10000);
				break;
			}

			paramsItems.add(item);

		}

		return paramsItems;

	}

	/**
	 * 16进制使能保存
	 * 
	 * @author 泰得利通 wanglu
	 * @param paramsItems
	 * @return
	 */
	public static String getHexSaveData(List<ParamsItem> paramsItems) {
		StringBuffer sb = new StringBuffer();
		String binaryString = "";
		for (int i = 0; i < paramsItems.size(); i++) {
			ParamsItem paramsItem = paramsItems.get(i);
			String itemValue = paramsItem.getItemValue();
			String hexData = "";

			switch (i) {
			case 0:// 电机额定功率
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 1:// 电机额定频率
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 2:// 电机额定转速
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 3:// 电机额定电压
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 4:// 电机额定电流
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 5:// 电机极对数
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 6:// 电机运行方向
				hexData = paramsItem.getCheckedItem().getItemValue();
				break;
			case 7:// 过载系数
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 8:// 过载时间
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 9:// 编码类型
				hexData = paramsItem.getCheckedItem().getItemValue();
				break;
			case 10:// 编码分辨率
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 11:// 编码方向
				binaryString = paramsItem.getCheckedItem().getItemValue();
				break;
			case 12:// 编码断线检测使能
				binaryString = paramsItem.getItemValue() + binaryString;
				hexData = DataPaseUtil.binaryToHex(binaryString, 1);
				break;
			case 13:// 编码器断线检测时间
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			}

			sb.append(hexData);

		}

		return sb.toString();
	}

}
