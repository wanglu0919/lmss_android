package com.challentec.lmss.app;

import java.util.HashMap;
import java.util.Map;

/**
 * app缓存
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class AppCache {

	private static final Map<String, String> cacheMap = new HashMap<String, String>();
	public static final String CACHE_UI="chache_ui";//当前UI记录

	public static void cache(String key, String value) {
		cacheMap.put(key, value);
	}

	public static String getCache(String key) {
		return cacheMap.get(key);
	}

}
