package com.challentec.lmss.util;

import java.util.Calendar;

/**
 * 时间操作工具类
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class DataTimeUtil {

	/**
	 * 检查时间
	 * 
	 * @author 泰得利通 wanglu
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static boolean checkDataTime(int year, int month, int day) {
		Calendar calendarStart = getCalendar(2013, 1, 1);
		Calendar calendarEnd = getCalendar(2063, 12, 31);
		Calendar calendar = getCalendar(year, month, day);

		return (calendar.getTimeInMillis() >= calendarStart.getTimeInMillis() && calendar
				.getTimeInMillis() <= calendarEnd.getTimeInMillis());

	}

	/**
	 * 时间检测
	 * 
	 * @author 泰得利通 wanglu
	 * @param timeStr
	 * @return
	 */
	public static boolean checkTime(String timeStr) {
		String timeArrary[] = timeStr.split(":");
		int hour = Integer.parseInt(timeArrary[0]);// 小时
		int min = Integer.parseInt(timeArrary[1]);// 分钟
		int secon = Integer.parseInt(timeArrary[2]);// 秒

		if ((hour < 0 || hour > 24) || (min < 0 || min > 60)
				|| (secon < 0 || secon > 60)) {
			return false;
		}

		return true;

	}

	private static Calendar getCalendar(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();

		calendar.set(year, month, day);

		return calendar;

	}

}
