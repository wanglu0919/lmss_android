package com.challentec.lmss.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;

/**
 * 
 * @author wanglu 泰得利通
 *下载
 */
public class DownLoadTask {

	/**
	 * 
	 *wanglu 泰得利通 
	 *下载新APK
	 * @param path
	 * @param FilePath
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public static File dowLoadNewSoft(String path, String FilePath,ProgressDialog pd)
			throws Exception {

		URL url = new URL(path);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(5000);
		httpURLConnection.setRequestMethod("GET");
		if (httpURLConnection.getResponseCode() == 200) {
			int total=httpURLConnection.getContentLength();
			pd.setMax(total);
			File file = new File(FilePath);
			FileOutputStream fos = new FileOutputStream(file);
			InputStream is = httpURLConnection.getInputStream();
			int currentLen=0;
			int len = 0;
			byte buffer[] = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				currentLen+=len;
				pd.setProgress(currentLen);
				
			}

			fos.flush();
			fos.close();
			is.close();

			return file;

		}

		return null;
	}
}
