package com.challentec.lmss.util;

import android.content.Context;
import android.widget.Toast;

/**
 * UI基本操作工具类封装
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class UIHelper {

	public static void showToask(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		

	}

	public static void showToask(Context context, int stringId) {
		Toast.makeText(context, context.getString(stringId), Toast.LENGTH_SHORT).show();
		
	}

}
