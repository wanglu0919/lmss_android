package com.challentec.lmss.engine;

import java.util.List;

import com.challentec.lmss.bean.ButtonParamItem;
import com.challentec.lmss.util.DataPaseUtil;

/**
 * 前后门设置
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class FloorDoorSetParser {

	/**
	 * 获取楼层数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param buttonParamItems
	 * @return
	 */
	public static String getHexSaveData(List<ButtonParamItem> buttonParamItems) {

		StringBuffer sb = new StringBuffer();
		StringBuffer tempBinary =new StringBuffer("");
		for (int i = 0; i < buttonParamItems.size(); i++) {

			ButtonParamItem buttonParamItem = buttonParamItems.get(i);

			if (buttonParamItem.isHightLinght()) {// 高亮
				tempBinary.append("1");
			} else {
				tempBinary.append("0");
			}

			if ((i + 1) % 8 == 0) {// 8个一个字节一组

				sb.append(DataPaseUtil.binaryToHex(tempBinary.reverse().toString(), 1));
				tempBinary= new StringBuffer("");
			}

		}

		if (!tempBinary.toString().equals("")) {
			sb.append(DataPaseUtil.binaryToHex(tempBinary.reverse().toString(), 1));
		}

		return sb.toString();
	}
}
