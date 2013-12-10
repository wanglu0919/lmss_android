package com.challentec.lmss.ui;

import com.challentec.lmss.app.R;

import android.view.View;
import android.webkit.WebView;

/**
 * 关于我们
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class AboutUsActivity extends TabContentBaseActivity {
	private WebView wb_about_us;
	@Override
	protected void initMainView(View mainView) {
		wb_about_us=(WebView) mainView.findViewById(R.id.wb_about_us);
		wb_about_us.loadUrl("file:///android_asset/soft_about.html");//加载关于我们html文件
	}

	@Override
	protected CharSequence getTitleText() {

		return getString(R.string.ui_title_about_us);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_about_us_layout;
	}

}
