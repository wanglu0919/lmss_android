package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.CheckItem;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.listener.ArdChooseEnableOnChangeListener;
import com.challentec.lmss.listener.TqOpenDoorEnableOnChangeListener;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.UnitUtil;

/**
 * 设置基本参数解析
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class BasicParamsParser {

	public static List<ParamsItem> getParamsItems(Context context,
			String parseData, int itemNameIds[]) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		for (int i = 0; i < itemNameIds.length; i++) {
			ParamsItem item = new ParamsItem();// 系统时间
			item.setItemName(context.getString(itemNameIds[i]));
			item.setOrder(i);

			switch (i) {
			case 0:// 系统时间
				item.setItemValue(parseData.substring(0, 4) + "-"
						+ parseData.substring(4, 6) + "-"
						+ parseData.substring(6, 8) + " "
						+ parseData.substring(8, 10) + ":"
						+ parseData.substring(10, 12) + ":00");
				item.setValueType(ParamsItem.VALUE_TYPE_DATE_TIME);
				break;
			case 1:// 总楼层
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 14, 16)
						+ "");

				item.setUnit(UnitUtil.getUnit(context, UnitUtil.FLOOR));

				item.setLimit(2, 40);// 设置限制范围
				break;
			case 2:// 地下城
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 16, 18)
						+ "");

				item.setUnit(UnitUtil.getUnit(context, UnitUtil.FLOOR));
				item.setLimit(1, 5);

				break;
			case 3:// 电梯额定速度
				item.setItemValue(DataPaseUtil.getDataFloat(parseData, 18, 22,
						2, 1) + "");
				item.setAccuracy(1);

				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SPEED));
				item.setLimit((float) 0.4, (float) 10);
				break;
			case 4:// 电梯运行速度
				item.setItemValue(DataPaseUtil.getDataFloat(parseData, 22, 26,
						2, 1) + "");
				item.setAccuracy(1);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SPEED));
				item.setLimit((float) 0.3, 10);
				break;
			case 5:// 拽引轮直径
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 26, 30)
						+ "");

				item.setUnit(UnitUtil.getUnit(context, UnitUtil.MILLIMETRE));
				item.setLimit(240, 900);
				break;
			case 6:// 拽绳悬挂比
				String xgb = parseData.substring(30, 32);

				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);// 选项类型
				List<CheckItem> checkItems = new ArrayList<CheckItem>();
				CheckItem checkItem1 = new CheckItem("1:1", "00");
				CheckItem checkItem2 = new CheckItem("2:1", "01");
				CheckItem checkItem3 = new CheckItem("4:1", "02");
				if (xgb.equals("00")) {
					checkItem1.setCheck(true);
				} else if (xgb.equals("01")) {
					checkItem2.setCheck(true);

				} else if (xgb.equals("02")) {
					checkItem3.setCheck(true);
				}
				checkItems.add(checkItem1);
				checkItems.add(checkItem2);
				checkItems.add(checkItem3);
				item.setCheckItems(checkItems);

				break;
			case 7:// 减速比
				item.setValueType(ParamsItem.VALUE_TYPE_NOMAIL_SCALE);
				item.setScacleLimit(1, 80, 1, 1);// 设置比的范围
				item.setItemValue(DataPaseUtil.getCompareData(parseData, 32,
						36, 2));
				break;
			case 8:// 高速计数学方向
				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);// 选项类型
				List<CheckItem> gsjsCheckItems = new ArrayList<CheckItem>();
				int gsjsData = (DataPaseUtil.getDataInt(parseData, 36, 38)) & 1;// 取最后一位
				CheckItem gsjscheckItem1 = new CheckItem("0", "0");
				CheckItem gsjscheckItem2 = new CheckItem("1", "1");
				if (gsjsData == 0) {
					gsjscheckItem1.setCheck(true);
				} else if (gsjsData == 1) {
					gsjscheckItem2.setCheck(true);
				}
				gsjsCheckItems.add(gsjscheckItem1);
				gsjsCheckItems.add(gsjscheckItem2);
				item.setCheckItems(gsjsCheckItems);
				break;
			case 9:// 电机形式

				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);// 选项类型
				List<CheckItem> djxsCheckItems = new ArrayList<CheckItem>();
				int djData = (DataPaseUtil.getDataInt(parseData, 36, 38) >> 1) & 1;// 取第二位
				CheckItem djDataCheckItem1 = new CheckItem(
						context.getString(R.string.basic_check_asynchronization),
						"0");
				CheckItem djDataCheckItem2 = new CheckItem(
						context.getString(R.string.basic_check_synchronization),
						"1");
				if (djData == 0) {
					djDataCheckItem1.setCheck(true);
				} else if (djData == 1) {
					djDataCheckItem2.setCheck(true);
				}
				djxsCheckItems.add(djDataCheckItem1);
				djxsCheckItems.add(djDataCheckItem2);
				item.setCheckItems(djxsCheckItems);
				break;
			case 10:// 载波频率
				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);// 选项类型
				List<CheckItem> zbplItems = new ArrayList<CheckItem>();
				CheckItem zbplCheckItem1 = new CheckItem("4", "00");
				CheckItem zbplCheckItem2 = new CheckItem("6", "01");
				CheckItem zbplCheckItem3 = new CheckItem("8", "02");
				CheckItem zbplCheckItem4 = new CheckItem("10", "03");
				CheckItem zbplCheckItem5 = new CheckItem("15", "04");
				String zpblData = parseData.substring(38, 40);

				if (zpblData.equals("00")) {
					zbplCheckItem1.setCheck(true);
				} else if (zpblData.equals("01")) {
					zbplCheckItem2.setCheck(true);
				} else if (zpblData.equals("02")) {
					zbplCheckItem3.setCheck(true);
				} else if (zpblData.equals("03")) {
					zbplCheckItem4.setCheck(true);
				} else if (zpblData.equals("04")) {
					zbplCheckItem5.setCheck(true);
				}
				zbplItems.add(zbplCheckItem1);
				zbplItems.add(zbplCheckItem2);
				zbplItems.add(zbplCheckItem3);
				zbplItems.add(zbplCheckItem4);
				zbplItems.add(zbplCheckItem5);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.RATE_KHZ));
				item.setCheckItems(zbplItems);
				break;
			case 11:// 驱动控制方式

				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);// 选项类型

				List<CheckItem> qdkzItems = new ArrayList<CheckItem>();
				CheckItem qdkzCheckItem1 = new CheckItem(
						context.getString(R.string.basic_check_vf_vecotor),
						"00");// VF矢量
				CheckItem qdkzCheckItem2 = new CheckItem(
						context.getString(R.string.basic_check_pf_vecotor),
						"01");// PG矢量

				String qdkzData = parseData.substring(40, 42);
				if (qdkzData.equals("00")) {
					qdkzCheckItem1.setCheck(true);
				} else if (qdkzData.equals("01")) {
					qdkzCheckItem2.setCheck(true);
				}
				qdkzItems.add(qdkzCheckItem1);
				qdkzItems.add(qdkzCheckItem2);
				item.setCheckItems(qdkzItems);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.VECTOR));
				break;

			case 12:// 消防车撤离曾
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 42, 44)
						+ "");
				
				item.setLimit(1, AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY)>40 ? 40 :AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY));
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.FLOOR));
				break;

			case 13:// 驻留层
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 44, 46)
						+ "");
				item.setLimit(1,  AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY)>40 ? 40 :AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY));
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.FLOOR));
				break;

			case 14:// 返回机站层
				item.setItemValue(DataPaseUtil.getDataInt(parseData, 46, 48)
						+ "");
				item.setLimit(1,  AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY)>40 ? 40 :AppConfig.getSharePreferenceInt(AppConfig.FLOOR_NUM_KEY));
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.FLOOR));
				break;

			case 15:// ard选折
				int ardData = DataPaseUtil.getDataInt(parseData, 48, 50) & 1;// 取第一位
				/*
				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);// 选项类型
				

				List<CheckItem> ardItems = new ArrayList<CheckItem>();
				CheckItem rrd1CheckItem = new CheckItem(
						context.getString(R.string.basic_check_ard_start), "01");// ard启用
				CheckItem rrd2CheckItem = new CheckItem(
						context.getString(R.string.basic_check_ard_stop), "00");// ard停用

				if (ardData == 1) {
					rrd1CheckItem.setCheck(true);
				} else if (ardData == 0) {
					rrd2CheckItem.setCheck(true);
				}
				ardItems.add(rrd1CheckItem);
				ardItems.add(rrd2CheckItem);
				item.setCheckItems(ardItems);

				*/
				
				item.setValueType(ParamsItem.VALUE_TYPE_ON_OFF);

				if (ardData == 0) {
					item.setOFF_ON(false);
					item.setItemValue("00");
				} else if (ardData == 1) {
					item.setOFF_ON(true);
					item.setItemValue("01");
				}
				item.setOnCheckedChangeListener(new ArdChooseEnableOnChangeListener(
						item));// 设置监听器
				break;

			case 16:// ARD速度
				item.setItemValue(DataPaseUtil.getDataFloat(parseData, 50, 54,
						2, 2) + "");
				item.setAccuracy(2);
				item.setUnit(UnitUtil.getUnit(context, UnitUtil.SPEED));
				item.setLimit((float) 0.05, (float) 0.30);
				break;

			case 17:// 控制方式
				String controlData = parseData.substring(54, 56);
				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);// 选项类型
				List<CheckItem> controCheckItems = new ArrayList<CheckItem>();
				CheckItem controlCheckItem1 = new CheckItem(
						context.getString(R.string.basic_check_control_type_jx),
						"00");// 集选
				CheckItem controlCheckItem2 = new CheckItem(
						context.getString(R.string.basic_check_control_type_qc),
						"01");// 群控
				CheckItem controlCheckItem3 = new CheckItem(
						context.getString(R.string.basic_check_control_type_bl),
						"02");// 并联

				if (controlData.equals("00")) {
					controlCheckItem1.setCheck(true);
				} else if (controlData.equals("01")) {
					controlCheckItem2.setCheck(true);
				} else if (controlData.equals("02")) {
					controlCheckItem3.setCheck(true);
				}
				controCheckItems.add(controlCheckItem1);
				controCheckItems.add(controlCheckItem2);
				controCheckItems.add(controlCheckItem3);
				item.setCheckItems(controCheckItems);

				break;
			case 18:// 提前开门
				int tqkmData = DataPaseUtil.getDataInt(parseData, 56, 58) & 1;// 取第一位
				/*
				item.setValueType(ParamsItem.VALUE_TYPE_CHECK);// 选项类型
				List<CheckItem> tqCheckItems = new ArrayList<CheckItem>();
				CheckItem tqCheckItem1 = new CheckItem(
						context.getString(R.string.basic_check_opendoor_start),
						"01");// 启用
				CheckItem tqCheckItem2 = new CheckItem(
						context.getString(R.string.basic_check_opendoor_stop),
						"00");// 停用
				
				if (tqkmData == 0) {
					tqCheckItem2.setCheck(true);
				} else if (tqkmData == 1) {
					tqCheckItem1.setCheck(true);
				}
				tqCheckItems.add(tqCheckItem1);
				tqCheckItems.add(tqCheckItem2);
				item.setCheckItems(tqCheckItems);
				
				*/
				item.setValueType(ParamsItem.VALUE_TYPE_ON_OFF);

				if (tqkmData == 0) {
					item.setOFF_ON(false);
					item.setItemValue("00");
				} else if (tqkmData == 1) {
					item.setOFF_ON(true);
					item.setItemValue("01");
				}
				item.setOnCheckedChangeListener(new TqOpenDoorEnableOnChangeListener(
						item));// 设置监听器
				break;

			}

			paramsItems.add(item);

		}

		return paramsItems;

	}

	/**
	 * 构建保存提交数据hex data
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

			case 0:// 系统时间
				hexData = itemValue.replace("-", "").replace(" ", "")
						.replace(":", "");
				break;
			case 1:// 总楼层
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 2:// 地下城
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;
			case 3:// 电梯额定速度
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 4:// 电梯运行速度
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;
			case 5:// 拽引轮直径
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 2);
				break;
			case 6:// 拽绳悬挂比
				hexData = paramsItem.getCheckedItem().getItemValue();
				break;
			case 7:// 减速比
				hexData = DataPaseUtil.getCompareHexStr(itemValue, 1, 1);
				break;
			case 8:// 高速计数学方向

				break;
			case 9:// 电机形式
				String binaryData = paramsItem.getCheckedItem().getItemValue()
						+ paramsItems.get(8).getCheckedItem().getItemValue();

				hexData = DataPaseUtil.binaryToHex(binaryData, 1);

				break;
			case 10:// 载波频率
				hexData = paramsItem.getCheckedItem().getItemValue();
				break;
			case 11:// 驱动控制方式
				hexData = paramsItem.getCheckedItem().getItemValue();
				break;

			case 12:// 消防车撤离曾
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;

			case 13:// 驻留层
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;

			case 14:// 返回机站层
				hexData = DataPaseUtil
						.getHexStr(Integer.parseInt(itemValue), 1);
				break;

			case 15:// ard选折
				hexData = itemValue;
				break;

			case 16:// ARD速度
				hexData = DataPaseUtil.getHexStr(itemValue, 1, 1);
				break;

			case 17:// 控制方式
				hexData = paramsItem.getCheckedItem().getItemValue();
				break;
			case 18:// 提前开门
				hexData = itemValue;
				break;
			}

			sb.append(hexData);

		}

		return sb.toString();
	}
}
