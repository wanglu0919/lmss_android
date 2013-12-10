package com.challentec.lmss.bean;

import java.io.Serializable;
import java.util.List;

import android.widget.CompoundButton.OnCheckedChangeListener;

import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.DataTimeUtil;

/**
 * 页面参数列表项封装bean
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ParamsItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5087010432021591259L;
	public static final int VALUE_TYPE_NOMAL = 0x01;// 文本值
	public static final int VALUE_TYPE_NOMAIL_SCALE = 0x02;// 比列类型
	public static final int VALUE_TYPE_ON_OFF = 0x03;// 是开关按钮类型
	public static final int VALUE_TYPE_CHECK = 0x04;// 是选项类型的的参数
	public static final int VALUE_TYPE_DATE_TIME = 0x05;// 时间类型数据
	public static final int VALUE_TYPE_HEX = 0x06;// 16进值字符串
	public static final int VAALUE_TYPE_TROUBLE = 0x07;// 故障信息

	public static final int VALUE_INT = 0x11;// 整数型数据
	public static final int VALUE_FLOAT = 0x12;// 浮点型数据
	public static final int VALUE_HEX = 0x13;// 16进制类型
	public static final int VALUE_STING = 0x14;// 文本值
	public static final int VALUE_STING_TIME = 0x15;// 时间
	public static final int VALUE_STING_DATE = 0x16;// 时间
	public static final int VALUE_FLOOR_INT = 0x17;// 楼层相关

	private String itemName;// 参数项名称
	private String itemVlaue = "";// 参数值
	private int order;// 序号

	private int preStart;// 比前项
	private int preEnd;// 比后项
	private int backStart;// 比后项开始值
	private int backEnd;// 比后项结束值
	private int hexStart;// 16进制
	private int hexEnd;// 16进制
	private int byteLen;// 字节长度
	private int accuracy;// 小数精确度

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	private boolean checkItemError;// 表示可选项返回数据错误
	private OnCheckedChangeListener onCheckedChangeListener;// 开关按钮监听器

	public OnCheckedChangeListener getOnCheckedChangeListener() {
		return onCheckedChangeListener;
	}

	public void setOnCheckedChangeListener(
			OnCheckedChangeListener onCheckedChangeListener) {
		this.onCheckedChangeListener = onCheckedChangeListener;
	}

	public int getPreStart() {
		return preStart;
	}

	public void setPreStart(int preStart) {
		this.preStart = preStart;
	}

	public int getPreEnd() {
		return preEnd;
	}

	public void setPreEnd(int preEnd) {
		this.preEnd = preEnd;
	}

	public int getBackStart() {
		return backStart;
	}

	public void setBackStart(int backStart) {
		this.backStart = backStart;
	}

	public int getBackEnd() {
		return backEnd;
	}

	public void setBackEnd(int backEnd) {
		this.backEnd = backEnd;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	private String unit = "";// 单位
	private boolean off_on;
	private int iStart;// 整数开始
	private int iEnd;// 整数结束

	private float fStart;// 浮点型
	private float fEnd;// 浮点型结束
	private int valueType = VALUE_TYPE_NOMAL;// 默认为普通类型
	private List<CheckItem> checkItems;// 选项

	private boolean isChecked;// 是不当前选中的修改项

	private int dataType = VALUE_INT;// 数据类型

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	/**
	 * 检查数据是否合法
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 * 
	 * @return
	 */
	public boolean checkValue() {
		if ((valueType == VALUE_TYPE_NOMAL || valueType == VAALUE_TYPE_TROUBLE)
				&& !itemVlaue.equals("")) {// 有限制范围，并且是普通文本值,itemValue不为""进行数字限制范围检查

			boolean isValidate = false;
			switch (dataType) {
			case VALUE_INT:// 整形
				int iValue = Integer.parseInt(itemVlaue);
				if (iValue >= iStart && iValue <= iEnd) {
					isValidate = true;
				} else {
					isValidate = false;
				}

				break;
			case VALUE_FLOAT:// 浮点数据类型
				float fValue = Float.parseFloat(itemVlaue);
				String floatStr = itemVlaue.split("\\.")[1];
				if (floatStr.length() != this.accuracy) {
					return false;
				}
				if (fValue >= fStart && fValue <= fEnd) {
					isValidate = true;
				} else {
					isValidate = false;
				}

				break;
			case VALUE_STING:
				if (!itemVlaue.equals("Error")) {
					isValidate = true;
				}

				break;

			case VALUE_STING_TIME:// 时间
				isValidate = DataTimeUtil.checkTime(itemVlaue);
				break;
			case VALUE_STING_DATE:// 日期

				String date[] = itemVlaue.split("\\.");
				isValidate = DataTimeUtil.checkDataTime(
						Integer.parseInt(date[0]), Integer.parseInt(date[1]),
						Integer.parseInt(date[2]));
				break;

			}
			return isValidate;

		} else if (valueType == VALUE_TYPE_NOMAIL_SCALE
				&& !itemVlaue.equals("")) {// 比列类型数据检查

			String scaleData[] = itemVlaue.split(":");
			int preData = Integer.parseInt(scaleData[0]);
			int backData = Integer.parseInt(scaleData[1]);

			if ((preData >= preStart && preData <= preEnd)
					&& (backData >= backData && backStart <= backEnd)) {
				return true;
			}
			return false;

		} else if (valueType == VALUE_TYPE_CHECK) {// 可选项数据类型检查

			return !isCheckItemError();
		} else if (valueType == VALUE_TYPE_HEX) {// 16进制
			int hexData = DataPaseUtil.hexStrToInt(itemVlaue.toLowerCase());

			if (hexData >= hexStart && hexData <= hexEnd) {
				return true;
			}
			return false;

		} else if (valueType == VALUE_TYPE_DATE_TIME) {
			String Datatime = itemVlaue.split(" ")[0];
			String date[] = Datatime.split("-");
			return DataTimeUtil.checkDataTime(Integer.parseInt(date[0]),
					Integer.parseInt(date[1]), Integer.parseInt(date[2]));
		} else {
			return true;
		}

	}

	/**
	 * 检查数据外部数据受否合法
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 * @return
	 */
	public boolean checkIValue(int data) {
		if (data >= iStart && data <= iEnd) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 坚持浮点数合不合法
	 * 
	 * @author 泰得利通 wanglu
	 * @param fdata
	 * @return
	 */
	public boolean checkIValue(float fdata) {

		if (fdata >= fStart && fdata <= fEnd) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查16进制数
	 * 
	 * @author 泰得利通 wanglu
	 * @param hexData
	 * @return
	 */
	public boolean checkHexValue(String hexData) {

		if (hexData.length() > byteLen * 2) {// 字节长度检查
			return false;
		}
		int intData = DataPaseUtil.hexStrToInt(hexData.toLowerCase());
		if (intData >= hexStart && intData <= hexEnd) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 检查比例数据是否正确
	 * 
	 * @author 泰得利通 wanglu
	 * @param preData
	 * @param backData
	 * @return
	 */
	public boolean checkScaleData(int preData, int backData) {
		if ((preData >= preStart && preData <= preEnd)
				&& (backData >= backStart && backData <= backEnd)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取限制范围子复产
	 * 
	 * @author 泰得利通 wanglu
	 * @return 范围字符串
	 */
	public String getLimitString() {

		String dataStr = "";
		switch (dataType) {
		case VALUE_INT:// 整数
			dataStr = iStart + "-" + iEnd;
			break;
		case VALUE_FLOAT:// 浮点数

			dataStr = getFloatString(fStart) + "-" + getFloatString(fEnd);
			break;
		case VALUE_HEX:// 16进制数
			dataStr = DataPaseUtil.getHexStr(hexStart, 1).toUpperCase() + "-"
					+ DataPaseUtil.getHexStr(hexEnd, 1).toUpperCase();
			break;

		}

		return dataStr;
	}

	public int getValueType() {
		return valueType;
	}

	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemValue() {

		if (valueType == VALUE_TYPE_CHECK) {// 如果是可选项类型，返回可选项名称
			CheckItem checkItem = getCheckedItem();
			if (checkItem != null) {
				return checkItem.getItemName();
			} else {
				checkItemError = true;
				return "Error";
			}
		}
		return itemVlaue;
	}

	public boolean isCheckItemError() {
		return checkItemError;
	}

	public void setItemValue(String itemVlaue) {
		this.itemVlaue = itemVlaue;
	}

	public List<CheckItem> getCheckItems() {
		return checkItems;
	}

	public void setCheckItems(List<CheckItem> checkItems) {
		this.checkItems = checkItems;
	}

	/**
	 * 设置限制范围
	 * 
	 * @author 泰得利通 wanglu
	 * @param intStart
	 * @param intEnd
	 */
	public void setLimit(int iStart, int iEnd) {
		this.iStart = iStart;
		this.iEnd = iEnd;
		this.dataType = VALUE_INT;// 标识数据类型
	}

	/**
	 * 设置限制范围 浮点型
	 * 
	 * @author 泰得利通 wanglu
	 * @param fStart
	 * @param fEend
	 */
	public void setLimit(float fStart, float fEend) {
		this.fStart = fStart;
		this.fEnd = fEend;
		this.dataType = VALUE_FLOAT;// 标识数据类型
	}

	/**
	 * 16进制范围限制
	 * 
	 * @author 泰得利通 wanglu
	 * @param hexStart
	 * @param hexEnd
	 */
	public void setHexLimit(int hexStart, int hexEnd, int byteLen) {
		this.hexStart = hexStart;
		this.hexEnd = hexEnd;
		this.dataType = VALUE_HEX;// 16进制
		this.byteLen = byteLen;
	}

	/**
	 * 返回选中项
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	public CheckItem getCheckedItem() {
		if (checkItems != null && checkItems.size() > 0) {
			int i = 0;
			boolean flag = false;
			for (CheckItem checkItem : checkItems) {

				if (checkItem.isCheck()) {
					flag = true;
					break;

				}
				i++;
			}
			if (flag) {
				return checkItems.get(i);
			}

		}

		return null;
	}

	/**
	 * 设置开获关闭
	 * 
	 * @author 泰得利通 wanglu
	 * @param flag
	 */
	public void setOFF_ON(boolean flag) {
		off_on = flag;
	}

	public boolean getOFF_ON() {
		return off_on;
	}

	/**
	 * 
	 * @author 泰得利通 wanglu
	 * @param preStart
	 *            比前项开始值
	 * @param preEnd
	 *            比前项结束值
	 * @param backStart
	 *            比后项开始值
	 * @param backEnd
	 *            比后项结束值
	 */
	public void setScacleLimit(int preStart, int preEnd, int backStart,
			int backEnd) {
		this.preStart = preStart;
		this.preEnd = preEnd;
		this.backStart = backStart;
		this.backEnd = backEnd;
	}

	public int getByteLen() {
		return byteLen;
	}

	public void setByteLen(int byteLen) {
		this.byteLen = byteLen;
	}

	/**
	 * 获取浮点数字符表现形式
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 * @return
	 */
	private String getFloatString(float data) {
		String fdata = String.valueOf(data);
		String floatStr = fdata.split("\\.")[1];// 小数位
		String pre = "";
		for (int i = 0; i < accuracy - floatStr.length(); i++) {// 不够精确度，补0
			pre += "0";
		}

		return fdata + pre;

	}

}
