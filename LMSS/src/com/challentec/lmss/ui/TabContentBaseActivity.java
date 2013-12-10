package com.challentec.lmss.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.AppContext;
import com.challentec.lmss.app.AppManager;
import com.challentec.lmss.app.R;

/**
 * tab 内容界面基类
 * 
 * @author 泰得利通 wanglu
 * 
 */
public abstract class TabContentBaseActivity extends Activity {

	private Button head_btn_back;// 回退
	private Button head_btn_save;// 保存
	protected Button head_btn_command_call;// 召唤命令按钮
	protected TextView head_tv_title;
	private FrameLayout base_main_llview;
	protected View mainView;

	protected AppManager appManager;
	protected AppContext appContext;
	protected AppConfig appConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_layout);
		appManager = AppManager.getManager(getApplication());
		appContext = (AppContext) getApplication();
		appConfig=AppConfig.getAppConfig(appContext);
		findViews();
		initUI();
		addListeners();

	}

	/**
	 * 初始化控件
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initUI() {

		if (mainView != null) {
			base_main_llview.addView(mainView);
			initMainView(mainView);
		}

		head_tv_title.setText(getTitleText());
	}

	/**
	 * 
	 * @author 泰得利通 wanglu
	 * @param mainView
	 */
	protected abstract void initMainView(View mainView);

	/**
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	protected abstract CharSequence getTitleText();

	/**
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	protected abstract int getMainViewLayoutId();

	protected void addListeners() {
		head_btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				onBack();
			}
		});

		head_btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSave();

			}
		});

	}

	/**
	 * 保存方法，子类继承是实现
	 * 
	 * @author 泰得利通 wanglu
	 */
	protected void onSave() {

	}

	private void findViews() {
		head_btn_back = (Button) findViewById(R.id.head_btn_back);
		head_btn_save = (Button) findViewById(R.id.head_btn_save);
		head_tv_title = (TextView) findViewById(R.id.head_tv_title);
		base_main_llview = (FrameLayout) findViewById(R.id.base_main_llview);
		head_btn_command_call = (Button) findViewById(R.id.head_btn_command_call);

		mainView = View.inflate(this, getMainViewLayoutId(), null);

	}

	/**
	 * 显示或隐藏save按钮
	 * 
	 * @author 泰得利通 wanglu
	 * @param show
	 */
	protected void hideOrShowSaveBtn(boolean show) {
		if (show) {
			head_btn_save.setVisibility(Button.VISIBLE);
		} else {
			head_btn_save.setVisibility(Button.GONE);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_UP) {

				onBack();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 回退
	 * 
	 * @author 泰得利通 wanglu
	 */
	protected void onBack() {
		MainTabActivity.instance.back();

	}

}
