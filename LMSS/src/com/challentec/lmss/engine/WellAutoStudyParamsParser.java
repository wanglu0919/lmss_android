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
 * 进道自学习-参数解析类
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class WellAutoStudyParamsParser {

	public static List<ParamsItem> getParamsItems(Context context, String data) {
		List<ParamsItem> paramsItems = new ArrayList<ParamsItem>();

		String rex = "(\\w{6})"; // 3个字节表示一个数值
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(data);

		int i = 0;
		int floor = 1;
		while (m.find()) {
			ParamsItem paramsItem = new ParamsItem();
			paramsItem.setOrder(i);
			if (i <= 4) {
				switch (i) {
				case 0:// 总高度
					paramsItem.setItemName(context
							.getString(R.string.well_study_total_high));
					break;
				case 1:// 上强减1脉冲数
					paramsItem.setItemName(context
							.getString(R.string.well_study_up_pulse1));
					break;
				case 2:// 下强减1脉冲数
					paramsItem.setItemName(context
							.getString(R.string.well_study_down_pulse1));
					break;
				case 3:// 上强减2脉冲数
					paramsItem.setItemName(context
							.getString(R.string.well_study_up_pulse2));
					break;
				case 4:// 下强减2脉冲数
					paramsItem.setItemName(context
							.getString(R.string.well_study_down_pulse2));
					break;

				}

			} else {

				int index = ((i - 5) % 4);

				switch (index) {
				case 0:
					paramsItem
							.setItemName(floor
									+ context
											.getString(R.string.well_study_floor_positon_value));

					floor++;
					break;
				case 1:
					paramsItem.setItemName(context
							.getString(R.string.well_study_floor_space_value));
					break;
				case 2:
					paramsItem.setItemName(context
							.getString(R.string.well_study_up_syn_value));
					break;
				case 3:
					paramsItem.setItemName(context
							.getString(R.string.well_study_down_syn_value));
					break;

				}

			}

			String wellData = m.group();
			paramsItem.setItemValue(DataPaseUtil.hexStrToInt(wellData) + "");
			paramsItems.add(paramsItem);
			paramsItem.setLimit(1000, 160000);
			i++;
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
		for (int i = 0; i < paramsItems.size(); i++) {
			String itemValue = paramsItems.get(i).getItemValue();

			String hexData = DataPaseUtil.getHexStr(
					Integer.parseInt(itemValue), 3);

			sb.append(hexData);

		}

		return sb.toString();
	}

}
