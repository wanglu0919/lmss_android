package com.challentec.lmss.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClinetAPI;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;

/**
 * 设置->自学习activity
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class AutoStudyActivity extends TabContentBaseActivity {

	private ViewPager auto_study_vp_contains;// 分页控件
	private LinearLayout auto_study_ll_draw_point;
	private List<View> pageViews;
	private List<ImageView> imageViews;

	private RelativeLayout auto_elec_rl_param;// 电机自学习参数
	private RelativeLayout well_auto_study_rl_param;//进道自学习参数
	private SynTask synUILog;
	private Button auto_study_ele_btn_start;// 电机自学习开始
	private Button auto_study_btn_well_start;// 进道子学习开始
	private ProgressDialog pd_study;// 学习进度对话框
	

	@Override
	protected void initMainView(View mainView) {
		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new AutoStudyAppMessageLinster());//注册消息接收广播
		
		auto_study_vp_contains = (ViewPager) mainView
				.findViewById(R.id.auto_study_vp_contains);
		auto_study_ll_draw_point = (LinearLayout) mainView
				.findViewById(R.id.auto_study_ll_draw_point);

		initSubView();
		initPointView();

		auto_study_vp_contains.setAdapter(new AutoPagerViewAdapter());

		auto_study_vp_contains.setCurrentItem(0);

		synUILog = new SynTask(appContext);
		synUILog.uiLog(Protocol.UI_AUTO_STUDY_ELC);// 记录UI
		pd_study = new ProgressDialog(this);
		pd_study.setMessage(getString(R.string.tip_msg_pb_auto_study));

		
	}



	@Override
	protected void addListeners() {
		super.addListeners();

		auto_study_vp_contains
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						draw_Point(position);
						if (position == 0) {
							synUILog.uiLog(Protocol.UI_AUTO_STUDY_ELC);// 记录UI
						} else if (position == 1) {
							synUILog.uiLog(Protocol.UI_AUTO_STUDY_WELL);// 记录UI
						}
					}

					@Override
					public void onPageScrolled(int position, float arg1,
							int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int state) {

					}
				});

		auto_elec_rl_param.setOnClickListener(new OnClickListener() {// 电机参数

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(AutoStudyActivity.this,
								ElecAutoStudyParamActivity.class);
						MainTabActivity.instance.addView(intent);
					}
				});

		auto_study_ele_btn_start.setOnClickListener(new OnClickListener() {// 电机自学习开始

					@Override
					public void onClick(View v) {
						elecStartStudy();
					}
				});

		auto_study_btn_well_start.setOnClickListener(new OnClickListener() {// 电机自学习开始

					@Override
					public void onClick(View v) {
						wellStartStudy();
					}
				});
		
		well_auto_study_rl_param.setOnClickListener(new OnClickListener() {//进道自学习参数
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AutoStudyActivity.this,
						WellAutoStudyParamActivity.class);
				MainTabActivity.instance.addView(intent);
			}
		});

	}

	/**
	 * 进道自学习开始
	 * 
	 * @author 泰得利通 wanglu
	 */
	protected void wellStartStudy() {
		pd_study.show();
		String apiData = ClinetAPI.getApiStr(Protocol.S_WELL_AUTO_STUDY);
		new SynTask(new SynHandler()

			, appContext).writeData(apiData);
	}

	/**
	 * 电机自学习开始
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void elecStartStudy() {

		pd_study.show();
		String apiData = ClinetAPI.getApiStr(Protocol.S_ELEC_AUTO_STUDY);
		new SynTask(new SynHandler(), appContext).writeData(apiData);

	}

	private class AutoStudyAppMessageLinster implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {
			pd_study.dismiss();// 销毁对话框
			if (responseData.getFunctionCode().equals(
					Protocol.S_ELEC_AUTO_STUDY)) {
				if (responseData.isSuccess()) {
					UIHelper.showToask(AutoStudyActivity.this, "电机学习成功");
				}
			} else if (responseData.getFunctionCode().equals(
					Protocol.S_WELL_AUTO_STUDY)) {
				if (responseData.isSuccess()) {
					UIHelper.showToask(AutoStudyActivity.this, "进道学习成功");
				}
			}

		}

	}

	public void draw_Point(int index) {
		for (int i = 0; i < imageViews.size(); i++) {

			if (index == i) {
				imageViews.get(i).setBackgroundResource(
						R.drawable.point_hightl_inght);

			} else {

				imageViews.get(i).setBackgroundResource(R.drawable.point_dark);

			}
		}

	}

	/**
	 * 初始化点视图
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initPointView() {
		imageViews = new ArrayList<ImageView>();
		ImageView imageView;
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(5, 5));
			imageView.setBackgroundResource(R.drawable.point_dark);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 20;
			layoutParams.rightMargin = 20;
			auto_study_ll_draw_point.addView(imageView, layoutParams);

			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.point_hightl_inght);
			}
			imageViews.add(imageView);

		}

	}

	/**
	 * 初始化电机自学习视图，井道自学习视图
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initSubView() {
		pageViews = new ArrayList<View>();
		View autoElecView = View.inflate(this, R.layout.auto_study_elec_layout,
				null);// 电机自学习视图
		auto_study_ele_btn_start = (Button) autoElecView
				.findViewById(R.id.auto_study_ele_btn_start);
		auto_elec_rl_param = (RelativeLayout) autoElecView
				.findViewById(R.id.auto_elec_rl_param);
		View autoWellView = View.inflate(this,
				R.layout.auto_study_well__layout, null);// 井道自学习视图
		auto_study_btn_well_start = (Button) autoWellView
				.findViewById(R.id.auto_study_btn_well_start);
		well_auto_study_rl_param=(RelativeLayout) autoWellView.findViewById(R.id.well_auto_study_rl_param);
		pageViews.add(autoElecView);
		pageViews.add(autoWellView);

	}

	@Override
	protected CharSequence getTitleText() {
		return getResources().getString(R.string.ui_title_auto_study);
	}

	@Override
	protected int getMainViewLayoutId() {
		return R.layout.activity_auto_study_layout;

	}

	class AutoPagerViewAdapter extends PagerAdapter {
		// 显示数目
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		/***
		 * 获取每一个item， 类于listview中的getview
		 */
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

	}

}
