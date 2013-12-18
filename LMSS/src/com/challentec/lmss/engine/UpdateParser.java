package com.challentec.lmss.engine;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.challentec.lmss.bean.UpdateInfo;
/**
 * apk更新信息检测
 * @author Administrator
 *
 */
public class UpdateParser {

	public static UpdateInfo paserUpdateInfo(InputStream is) throws Exception{
		XmlPullParser paser=Xml.newPullParser();
		UpdateInfo update=new UpdateInfo();
		paser.setInput(is, "utf-8");
		int type=paser.getEventType();
		
		while(type!=XmlPullParser.END_DOCUMENT){
			switch(type){
			case XmlPullParser.START_TAG:
				
				if("version".equals(paser.getName())){
					update.setVersion(paser.nextText());
				}else if("description".equals(paser.getName())){
					update.setDescription(paser.nextText());
				}else if("url".equals(paser.getName())){
					update.setUrl(paser.nextText());
				}
				break;
			
			}
			
			type=paser.next();
			
		}
		
		return update;
	}
}
