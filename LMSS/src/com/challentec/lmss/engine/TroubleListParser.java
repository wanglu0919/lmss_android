package com.challentec.lmss.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.Trouble;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.DataTimeUtil;

/**
 * 故障列表解析
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class TroubleListParser {

	public static List<Trouble> getTroubles(String data, Context context) {
		String rex = "(\\w{16})";
		Pattern pattern = Pattern.compile(rex);
		Matcher matcher = pattern.matcher(data);
		List<Trouble> troubles = new ArrayList<Trouble>();
		int index = 0;
		while (matcher.find()) {

			String trData = matcher.group();

			String id = trData.substring(0, 2);// id

			String year = trData.substring(2, 6);
			String month = trData.substring(6, 8);
			String day = trData.substring(8, 10);
			String hour = trData.substring(10, 12);
			String min = trData.substring(12, 14);
			String timeStr = year + "." + month + "." + day + " " + hour + ":"
					+ min;// 时间

			String code = trData.substring(14, 16);// 代码

			Trouble trouble = new Trouble();
			int tid = DataPaseUtil.hexStrToInt(id);
			trouble.setT_id(tid + "");// id
			if (index == 0) {
				trouble.setT_order(context
						.getString(R.string.trouble_list_lable_current_trouble));// 当前故障
			} else {
				String stid = "";
				if (tid < 10) {
					stid = "0" + tid;
				} else {
					stid = "" + tid;
				}
				trouble.setT_order(context
						.getString(R.string.trouble_list_lable_front)
						+ stid
						+ context.getString(R.string.trouble_list_lable_back));// 前+N次故障
			}
			trouble.setT_time(timeStr);// 时间
			int intCode = DataPaseUtil.hexStrToInt(code);
			if (intCode < 10) {
				trouble.setT_no("0" + intCode);
			} else {
				trouble.setT_no(intCode + "");
			}

			if (!DataTimeUtil.checkDataTime(Integer.parseInt(year),
					Integer.parseInt(month), Integer.parseInt(day))) {//时间检查
				trouble.setError(true);// 标识信息错误
			}
			
			int intHour=Integer.parseInt(hour);
			int intMin=Integer.parseInt(min);
			if(intHour<0||intHour>24){//小时检查
				trouble.setError(true);// 标识信息错误
			}
			
			if(intMin<0||intMin>60){//分钟检查
				trouble.setError(true);
			}
			if (intCode < 0 || intCode > 99) {
				trouble.setError(true);// 标识信息错误
			}
			troubles.add(trouble);
			index++;

		}

		return troubles;

	}

}
