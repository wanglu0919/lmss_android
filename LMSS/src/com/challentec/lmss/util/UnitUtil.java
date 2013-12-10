package com.challentec.lmss.util;

import com.challentec.lmss.app.R;

import android.content.Context;

/**
 * 单位工具类
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class UnitUtil {

	public static final int FLOOR = 0x01;
	public static final int SPEED = 0x02;
	public static final int MILLIMETRE = 0x03;
	public static final int RATE_KHZ = 0x04;
	public static final int KW = 0x06;
	public static final int HZ = 0x07;
	public static final int ZF = 0x08;
	public static final int FT = 0x09;
	public static final int SECONDS = 0x10;
	public static final int AP = 0x11;
	public static final int APAIR = 0x12;
	public static final int KILLMI = 0x13;
	public static final int MILL = 0x14;
	public static final int TIME = 0x15;
	public static final int UNIT_Z = 0x16;
	public static final int HOUR = 0x17;
	public static final int WD = 0x18;// 温度
	public static final int HH = 0x19;
	public  static final int OM = 0x20;
	public static final int JD=0x21;//角度
	public static final int VECTOR = 0x22;//矢量

	/**
	 * 获取单位
	 * 
	 * @author 泰得利通 wanglu
	 * @param context
	 * @param type
	 * @return
	 */
	public static String getUnit(Context context, int type) {
		String unit = "";
		switch (type) {
		case FLOOR:// 层
			unit = context.getString(R.string.unit_floor);
			break;
		case SPEED:// 速度
			unit = context.getString(R.string.unit_speed);
			break;
		case MILLIMETRE:// 毫米
			unit = context.getString(R.string.unit_millimetre);

			break;

		case RATE_KHZ:// 频率
			unit = context.getString(R.string.unit_rate_khz);
			break;
		case KW:// 千瓦
			unit = context.getString(R.string.unit_kw);
			break;
		case HZ:// 赫兹
			unit = context.getString(R.string.unit_hz);
			break;
		case ZF:// 转/分
			unit = context.getString(R.string.unit_zf);
			break;
		case FT:// 福特
			unit = context.getString(R.string.unit_ft);
			break;
		case SECONDS:// 秒
			unit = context.getString(R.string.unit_seconds);
			break;
		case AP:// 安培
			unit = context.getString(R.string.unit_ap);
			break;
		case APAIR:// 极对
			unit = context.getString(R.string.unit_apair);
			break;

		case KILLMI:// 毫秒
			unit = context.getString(R.string.unit_killmi);
			break;
		case MILL:// 米
			unit = context.getString(R.string.unit_miil);
			break;
		case TIME:// 次
			unit = context.getString(R.string.unit_time);
			break;
		case UNIT_Z:// 转
			unit = context.getString(R.string.unit_z);
			break;
		case HOUR:// 小时
			unit = context.getString(R.string.unit_hour);
			break;
		case WD:// 温度
			unit = context.getString(R.string.unit_du);
			break;
		case HH:
			unit = context.getString(R.string.unit_hh);

			break;
		case OM:
			unit = context.getString(R.string.unit_om);
			break;
		case JD:
			unit = context.getString(R.string.unit_jdu);
			break;
		case VECTOR:
			unit = context.getString(R.string.unit_vector);
			break;

		}

		return unit;

	}
}
