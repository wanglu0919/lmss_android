package com.challentec.lmss.engine;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.challentec.lmss.bean.UpdateInfo;

/**
 * 
 * @author wanglu
 *
 */
public class UpdateInfoService {
	
	private Context context;
	
	public UpdateInfoService(Context context){
		this.context=context;
	}
	
	
	/**
	 * 
	 * @param urlId  urid
	 * @return
	 * @throws E 
	 */
	public UpdateInfo getUpdateInfo(int urlId) throws Exception{
	
		String path=context.getResources().getString(urlId);
		URL url=new URL(path);
		HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
		httpURLConnection.setConnectTimeout(5000);
		httpURLConnection.setRequestMethod("GET");
		return UpdateParser.paserUpdateInfo(httpURLConnection.getInputStream());
	}
	
	
}
