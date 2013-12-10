package com.challentec.lmss.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.challentec.lmss.adapter.ButtonParamItemAdapter;
import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ButtonParamItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.FloorDoorSetParser;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClinetAPI;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.DisplayUtil;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;

/**
 * 设置-〉前后门设置
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class FloorDoorSetActivity extends TabContentBaseActivity {

	private ViewPager floor_door_vp_contains;
	private LinearLayout floor_door_ll_draw_point;
	private List<View> pageViews;
	private List<ImageView> imageViews;
	private GridView frontGridView;
	private GridView backGridView;
	private List<ButtonParamItem> frontButtonParamItems;
	private List<ButtonParamItem> backButtonParamItems;
	private View frontdoorView;
	private final int NUM_COLUMNS = 5;// 列数
	private int floorNum = 0;
	private SynTask synTask;// 异步任务
	private ButtonParamItemAdapter frontButtonParamItemAdapter;// 前门数据适配器
	private ButtonParamItemAdapter backButtonParamItemAdapter;
	private static final int TYPE_FRONT_SET = 0x01;// 前门设置
	private static final int TYPE_BACK_SET = 0x02;// 后门设置
	private int set_type = TYPE_FRONT_SET;
	private ProgressDialog pd_save;

	@Override
	protected void initMainView(View mainView) {
		hideOrShowSaveBtn(true);
		synTask = new SynTask(new SynHandler(), appContext);
		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new FloorDoorSetMessageListener());
		floorNum = AppConfig.getAppConfig(appContext).getSharedPreferences()
				.getInt(AppConfig.FLOOR_NUM_KEY, 0);// 获取保存的楼层数量

		if (floorNum > 40 || floorNum < 0) {
			floorNum = 40;
		}
		floor_door_vp_contains = (ViewPager) mainView
				.findViewById(R.id.floor_door_vp_contains);
		floor_door_ll_draw_point = (LinearLayout) mainView
				.findViewById(R.id.floor_door_ll_draw_point);
		pd_save = new ProgressDialog(this);

		initSubView();
		initPointView();

		floor_door_vp_contains.setAdapter(new FloorDoorSetPagerViewAdapter());

		floor_door_vp_contains.setCurrentItem(0);

		initData();
		int gridViewHight = getGridViewHeight(frontGridView);

		floor_door_vp_contains.getLayoutParams().height = gridViewHight
				+ DisplayUtil.dip2px(this, 50);
		getFrontDoorData();
		getBackDoorData();
		synTask.uiLog(Protocol.UI_FLOOR_SET_FRONT);// 前门设置
	}

	/**
	 * 获取楼层前门数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void getFrontDoorData() {
		synTask.writeData(ClinetAPI.getApiStr(Protocol.S_GET_FLOOR_SET_FRONT));
	}

	/**
	 * 获取后门数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void getBackDoorData() {
		synTask.writeData(ClinetAPI.getApiStr(Protocol.S_GET_FLOOR_SET_BACK));
	}

	private class FloorDoorSetMessageListener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(
					Protocol.S_GET_FLOOR_SET_FRONT)) {// 获取楼层前门数据

				if (responseData.isSuccess()) {

					showFrontDoorData(responseData);

				}

			} else if (responseData.getFunctionCode().equals(// 获取后门数据
					Protocol.S_GET_FLOOR_SET_BACK)) {

				if (responseData.isSuccess()) {
					showBacDoorData(responseData);
				}
			} else if (responseData.getFunctionCode().equals(
					Protocol.S_SAVE_FLOOR_SET_FRONT)) {// 保存前门设置数据

				if (responseData.isSuccess()) {// 保存成功

					UIHelper.showToask(appContext,
							R.string.tip_msg_save_front_success);

				} else {
					UIHelper.showToask(FloorDoorSetActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}

				pd_save.dismiss();

			} else if (responseData.getFunctionCode().equals(
					Protocol.S_SAVE_FLOOR_SET_BACK)) {// 保存后门设置数据

				if (responseData.isSuccess()) {// 保存成功

					UIHelper.showToask(appContext,
							R.string.tip_msg_save_back_success);
				} else {
					UIHelper.showToask(FloorDoorSetActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));

				}
				pd_save.dismiss();
			}

		}

	}

	/**
	 * 计算gridView高度
	 * 
	 * @author 泰得利通 wanglu
	 * @param gridView
	 * @return
	 */
	private int getGridViewHeight(GridView gridView) {

		if (gridView == null) {
			return 0;
		}
		ButtonParamItemAdapter gridAdapter = (ButtonParamItemAdapter) gridView
				.getAdapter();

		int totalHeight = 0;

		if (gridAdapter != null) {
			View listItem = gridAdapter.getView(0, null, gridView);
			listItem.measure(0, 0);
			int itemHeight = listItem.getMeasuredHeight();
			int totalItemCount = gridAdapter.getCount();
			int rowCount = 0;
			if (totalItemCount % NUM_COLUMNS == 0) {
				rowCount = totalItemCount / NUM_COLUMNS;
			} else {
				rowCount = totalItemCount / NUM_COLUMNS + 1;
			}
			int verticalSpacing = (rowCount - 1)
					* (DisplayUtil.dip2px(this, 3));
			totalHeight = rowCount * itemHeight + verticalSpacing;

		}

		return totalHeight;

	}

	/**
	 * 显示后门数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param responseData
	 */
	public void showBacDoorData(ResponseData responseData) {
		initBackDoorData();
		String backData = responseData.getData();
		String binaryString = DataPaseUtil.hexString2BinaryRe(backData);
		if (binaryString.length() >= floorNum) {// 窃取多余长度
			binaryString = binaryString.substring(0, floorNum);
		}

		for (int i = 0; i < binaryString.length(); i++) {

			if (binaryString.charAt(i) == '1') {
				backButtonParamItems.get(i).setHightLinght(true);// 高亮

			} else if (binaryString.charAt(i) == '0') {
				backButtonParamItems.get(i).setHightLinght(false);// 高亮
			}

		}

		for (int i = 0; i < floorNum - binaryString.length(); i++) {// 移除多余数据显示
			backButtonParamItems.remove(backButtonParamItems.size() - 1);
		}

		int gridViewHight = getGridViewHeight(backGridView);

		floor_door_vp_contains.getLayoutParams().height = gridViewHight
				+ DisplayUtil.dip2px(this, 50);

		backButtonParamItemAdapter.notifyDataSetChanged();// 通知数据更新

	}

	/**
	 * 显示前门数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param responseData
	 */
	public void showFrontDoorData(ResponseData responseData) {
		initFrontDoorData();
		String frontData = responseData.getData();
		String binaryString = DataPaseUtil.hexString2BinaryRe(frontData);

		if (binaryString.length() >= floorNum) {
			binaryString = binaryString.substring(0, floorNum);
		}
		for (int i = 0; i < binaryString.length(); i++) {

			if (binaryString.charAt(i) == '1') {
				frontButtonParamItems.get(i).setHightLinght(true);// 高亮

			} else if (binaryString.charAt(i) == '0') {
				frontButtonParamItems.get(i).setHightLinght(false);// 高亮
			}

		}

		for (int i = 0; i < floorNum - binaryString.length(); i++) {// 移除多余数据显示
			frontButtonParamItems.remove(frontButtonParamItems.size() - 1);
		}

		int gridViewHight = getGridViewHeight(frontGridView);

		floor_door_vp_contains.getLayoutParams().height = gridViewHight
				+ DisplayUtil.dip2px(this, 50);

		frontButtonParamItemAdapter.notifyDataSetChanged();// 通知数据更新

	}

	@Override
	protected void addListeners() {

		super.addListeners();

		floor_door_vp_contains
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						draw_Point(position);
						if (position == 0) {
							set_type = TYPE_FRONT_SET;// 标识前门设置
							synTask.uiLog(Protocol.UI_FLOOR_SET_FRONT);// 前门设置
						} else {
							set_type = TYPE_BACK_SET;// 标识后门设置
							synTask.uiLog(Protocol.UI_FLOOR_SET_BACK);// 后门设置

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

		frontGridView.setOnItemClickListener(new FrontItemClickListener());// 前门
		backGridView.setOnItemClickListener(new BackItemClickListener());// 后门
		floor_door_ll_draw_point.setOnClickListener(new PonitClickListner());
	}

	/**
	 * 前门设置点击监听
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class FrontItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {

			ButtonParamItem buttonParamItem = frontButtonParamItems
					.get(position);
			buttonParamItem.setHightLinght(!buttonParamItem.isHightLinght());
			frontButtonParamItemAdapter.notifyDataSetChanged();

		}

	}

	/**
	 * 后门设置点击监听
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class BackItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {

			ButtonParamItem buttonParamItem = backButtonParamItems
					.get(position);
			buttonParamItem.setHightLinght(!buttonParamItem.isHightLinght());
			backButtonParamItemAdapter.notifyDataSetChanged();

		}

	}

	/**
	 * 初始化数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initData() {

		initFrontDoorData();
		initBackDoorData();
	}

	/**
	 * 初始化后门数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initBackDoorData() {
		backButtonParamItems = new ArrayList<ButtonParamItem>();

		for (int i = 1; i <= floorNum; i++) {
			ButtonParamItem buttonParamItem = new ButtonParamItem();
			buttonParamItem.setItemName(i + "");
			backButtonParamItems.add(buttonParamItem);

		}
		backButtonParamItemAdapter = new ButtonParamItemAdapter(
				backButtonParamItems, this);

		if (backButtonParamItems.size() > 0) {
			backGridView.setAdapter(backButtonParamItemAdapter);

		}

	}

	/**
	 * 初始化前门数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initFrontDoorData() {

		frontButtonParamItems = new ArrayList<ButtonParamItem>();

		for (int i = 1; i <= floorNum; i++) {
			ButtonParamItem buttonParamItem = new ButtonParamItem();
			buttonParamItem.setItemName(i + "");
			frontButtonParamItems.add(buttonParamItem);

		}

		frontButtonParamItemAdapter = new ButtonParamItemAdapter(
				frontButtonParamItems, this);

		frontGridView.setAdapter(frontButtonParamItemAdapter);

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
	 * 初始化分页视图
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initSubView() {
		pageViews = new ArrayList<View>();
		frontdoorView = View.inflate(this,
				R.layout.floor_door_front_gate_set_layout, null);// 前门设置

		View backdoorView = View.inflate(this,
				R.layout.floor_door_back_gate_set_layout, null);// 后门设置

		frontGridView = (GridView) frontdoorView
				.findViewById(R.id.floor_door_set_gv_front);
		backGridView = (GridView) backdoorView
				.findViewById(R.id.floor_door_set_gv_back);

		pageViews.add(frontdoorView);
		pageViews.add(backdoorView);

	}

	private class PonitClickListner implements OnClickListener {

		@Override
		public void onClick(View v) {

			int index = floor_door_vp_contains.getCurrentItem();
			if (index == pageViews.size() - 1) {
				index = -1;
			}

			floor_door_vp_contains.setCurrentItem(index + 1, true);
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
			imageView.setLayoutParams(new LayoutParams(10, 10));
			imageView.setBackgroundResource(R.drawable.point_dark);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 20;
			layoutParams.rightMargin = 20;
			floor_door_ll_draw_point.addView(imageView, layoutParams);

			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.point_hightl_inght);
			}

			imageViews.add(imageView);

		}

	}

	@Override
	protected CharSequence getTitleText() {

		return getResources().getString(R.string.ui_title_floor_door_setting);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_floor_door_set_layout;
	}

	class FloorDoorSetPagerViewAdapter extends PagerAdapter {
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

	@Override
	protected void onSave() {
		switch (set_type) {
		case TYPE_FRONT_SET:
			saveFrontSetData();// 保存前门设置数据
			break;
		case TYPE_BACK_SET:
			saveBackSetData();// 保存后门设置数据
			break;
		}

	}

	/**
	 * 保存后门设置
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void saveBackSetData() {
		pd_save.setMessage(getString(R.string.tip_msg_save_back_save_data));
		pd_save.show();
		String apiData = ClinetAPI.getHexApiStr(Protocol.S_SAVE_FLOOR_SET_BACK,
				getBackSaveData());
		synTask.writeData(apiData);
	}

	/**
	 * 后门设置数据
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	private String getBackSaveData() {

		return FloorDoorSetParser.getHexSaveData(backButtonParamItems);
	}

	/**
	 * 获取前门设置数据
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	private String getFrontSaveData() {
		return FloorDoorSetParser.getHexSaveData(frontButtonParamItems);
	}

	/**
	 * 保存前门设置数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void saveFrontSetData() {
		pd_save.setMessage(getString(R.string.tip_msg_save_front_save_data));
		pd_save.show();

		String apiData = ClinetAPI.getHexApiStr(
				Protocol.S_SAVE_FLOOR_SET_FRONT, getFrontSaveData());
		synTask.writeData(apiData);

	}

}
