package com.challentec.lmss.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.challentec.lmss.adapter.ButtonParamItemAdapter;
import com.challentec.lmss.app.AppCache;
import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ButtonParamItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.listener.MonitorListener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.recever.MonitorRecever;
import com.challentec.lmss.util.ClinetAPI;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.DisplayUtil;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;

/**
 * 监控->召唤指令activity
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class CallCommandActivity extends TabContentBaseActivity {

	public static final int TYPE_COMMAND = 0x01;// 标识命令
	public static final int TYPE_CALL = 0x02;// 标识召唤
	public static final String TYPE_KEY = "type_key";
	private int type = TYPE_CALL;
	private ViewPager call_vp_contains;

	private LinearLayout call_ll_draw_point;
	private List<View> pageViews;
	private List<ImageView> imageViews;
	private GridView upCallGridView, downCallGridView;
	private GridView commandGridView;
	private List<ButtonParamItem> upCallParamItems, downCallParamItems;
	private List<ButtonParamItem> commandParamItems;
	private final int NUM_COLUMNS = 5;// 列数
	private LinearLayout call_command_rl_call;
	private LinearLayout call_command_rl_command;
	private SynTask synTask;// 异步任务
	private int viewpostion = 0;
	private ButtonParamItemAdapter upCallButtonParamItemAdapter;// 上召唤数据适配器
	private ButtonParamItemAdapter downCallButtonParamItemAdapter;// 下召唤数据适配器
	private ButtonParamItemAdapter commandButtonParamItemAdapter;// 指令数据
	private SharedPreferences sp;
	private int floorNumber;//楼层数量
	private MonitorRecever monitorRecever;

	public static final String UI_NAME = "COMMAND_CALL";//ui名称

	@Override
	protected void onBack() {

		stopCommand();
		stopDownCall();
		stopUpCall();
		AppCache.cache(AppCache.CACHE_UI, PortStateActivity.UI_NAME);
		Intent intent = new Intent();
		intent.setAction(MonitorRecever.ACTION_PORT_STATE);// 发送广播通知
															// 端口状态界面开始监空数据
		sendBroadcast(intent);
		super.onBack();
	}

	

	@Override
	protected void initMainView(View mainView) {
		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new CallAndCommandMessageListener());
		sp = appConfig.getSharedPreferences();
		floorNumber = sp.getInt(AppConfig.FLOOR_NUM_KEY, 0);
		if (floorNumber > 40 || floorNumber < 0) {
			floorNumber = 40;
		}
		synTask = new SynTask(new SynHandler(), appContext);
		type = getIntent().getExtras().getInt(TYPE_KEY);// 获取外部传入的显示参数

		head_btn_command_call.setVisibility(View.VISIBLE);// 显示召唤指令按钮
		head_btn_command_call.setText(getBtnString());// 设置按钮显示的字

		call_vp_contains = (ViewPager) mainView
				.findViewById(R.id.call_vp_contains);
		call_ll_draw_point = (LinearLayout) mainView
				.findViewById(R.id.call_ll_draw_point);
		commandGridView = (GridView) mainView.findViewById(R.id.command_gv);
		call_command_rl_call = (LinearLayout) mainView
				.findViewById(R.id.call_command_rl_call);
		call_command_rl_command = (LinearLayout) mainView
				.findViewById(R.id.call_command_rl_command);
		showLayout();

		initSubView();
		initPointView();

		call_vp_contains.setAdapter(new CallViewAdapter());

		call_vp_contains.setCurrentItem(0);

		initCallData();
		initCommandData();

		int gridViewHight = getGridViewHeight(upCallGridView);

		call_vp_contains.getLayoutParams().height = gridViewHight
				+ DisplayUtil.dip2px(this, 50);

		call_command_rl_command.getLayoutParams().height = getGridViewHeight(commandGridView)
				+ DisplayUtil.dip2px(this, 20);

		if (type == TYPE_CALL) {
			startUpCall();// 开始上召唤数据监控
			synTask.uiLog(Protocol.UI_UP_CALL);// 操作记录
		} else if (type == TYPE_COMMAND) {
			startCommand();// 开始命令
			synTask.uiLog(Protocol.UI_COMMAND);// 操作记录
		}

	}

	@Override
	protected void onDestroy() {

		unregisterReceiver(monitorRecever);// 取消指令召唤监控

		super.onDestroy();
		// 发送停止命令

	}

	/**
	 * 开始上召唤
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void startUpCall() {

		synTask.writeData(ClinetAPI.getApiStr(Protocol.M_START_UP_CALL));
	}

	/**
	 * 停止上召唤数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void stopUpCall() {
		synTask.writeData(Protocol.M_END_UP_CALL);

	}

	/**
	 * 注册监控
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void registerMonitorRecever() {

		monitorRecever = new MonitorRecever();
		monitorRecever.setMonitorListener(new CommadCallMonListener());
		IntentFilter filter = new IntentFilter();
		filter.addAction(MonitorRecever.ACTION_COMMAND_CALL);
		this.registerReceiver(monitorRecever, filter);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		AppCache.cache(AppCache.CACHE_UI, UI_NAME);// 存储当前UI
		registerMonitorRecever();
	}

	private class CommadCallMonListener implements MonitorListener {

		@Override
		public void monitor(Intent intent) {

			if (type == TYPE_CALL) {

				if (viewpostion == 0) {
					startUpCall();// 开始上召唤数据监控
				} else if (viewpostion == 1) {
					startDownCall();// 开始监控下召唤数据
				}

			} else if (type == TYPE_COMMAND) {
				startCommand();// 开始命令

			}

		}

	}

	/**
	 * 开始下召唤
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void startDownCall() {
		synTask.writeData(ClinetAPI.getApiStr(Protocol.M_START_DOWN_CALL));
	}

	/**
	 * 停止下召唤
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void stopDownCall() {
		synTask.writeData(ClinetAPI.getApiStr(Protocol.M_END_DOWN_CALL));
	}

	/**
	 * 开始指令
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void startCommand() {
		synTask.writeData(ClinetAPI.getApiStr(Protocol.M_START_COMMAND));
	}

	/**
	 * 结束指令
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void stopCommand() {
		synTask.writeData(ClinetAPI.getApiStr(Protocol.M_END_COMMAND));
	}

	/**
	 * 消息监听
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class CallAndCommandMessageListener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(Protocol.M_START_UP_CALL)) {// 开始请求上召唤数据

				if (responseData.isSuccess()) {
					showUpCallData(responseData);

				} else {
					UIHelper.showToask(appContext, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}

			} else if (responseData.getFunctionCode().equals(
					Protocol.M_END_UP_CALL)) {// 停止上召唤返回结果处理

			} else if (responseData.getFunctionCode().equals(
					Protocol.M_START_DOWN_CALL)) {// 开始下召唤数据监控消息处理

				if (responseData.isSuccess()) {//

					showDownCallData(responseData);

				} else {
					UIHelper.showToask(appContext, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}
			} else if (responseData.getFunctionCode().equals(
					Protocol.M_START_COMMAND)) {// 开始指令

				if (responseData.isSuccess()) {
					showCommandData(responseData);// 显示指令数据

				} else {
					UIHelper.showToask(appContext, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}
			}

		}

	}

	private void initCommandData() {
		commandParamItems = new ArrayList<ButtonParamItem>();
		for (int i = 1; i <= floorNumber; i++) {
			ButtonParamItem buttonParamItem = new ButtonParamItem();
			buttonParamItem.setItemName(i + "");
			commandParamItems.add(buttonParamItem);
		}

		commandButtonParamItemAdapter = new ButtonParamItemAdapter(
				commandParamItems, this);
		commandGridView.setAdapter(commandButtonParamItemAdapter);

	}

	/**
	 * 显示指令数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param responseData
	 */
	public void showCommandData(ResponseData responseData) {
		String commandData = responseData.getData();
		initCommandData();
		String binaryString = DataPaseUtil.hexString2BinaryRe(commandData);
		if (commandData.length() >= getDataLen()) {
			binaryString = binaryString.substring(0, floorNumber);
		}

		for (int i = 0; i < binaryString.length(); i++) {

			if (binaryString.charAt(i) == '1') {
				commandParamItems.get(i).setHightLinght(true);// 高亮显示
			} else if (binaryString.charAt(i) == '0') {
				commandParamItems.get(i).setHightLinght(false);
			}

		}

		for (int i = 0; i < floorNumber - binaryString.length(); i++) {
			commandParamItems.remove(commandParamItems.size() - 1);
		}

		commandButtonParamItemAdapter.notifyDataSetChanged();// 通知数据更新

		call_command_rl_command.getLayoutParams().height = getGridViewHeight(commandGridView)// 更新高度
				+ DisplayUtil.dip2px(this, 20);
	}

	/**
	 * 下召唤数据显示处理
	 * 
	 * @author 泰得利通 wanglu
	 * @param responseData
	 */
	public void showDownCallData(ResponseData responseData) {
		initDownCallData();
		String downCallData = responseData.getData();
		// if (downCallData.length() != getDataLen()) {// 下召唤数据长度不正确
		// return;
		// }

		String binaryString = DataPaseUtil.hexString2BinaryRe(downCallData);

		String downCallBinary = "";

		if (downCallData.length() >= getDataLen()) {
			downCallBinary = binaryString.substring(0, floorNumber - 1);// 0-39
		} else {
			downCallBinary = binaryString;
		}

		// 上召唤数据

		for (int i = 0; i < downCallBinary.length(); i++) {

			if (downCallBinary.charAt(i) == '1') {
				downCallParamItems.get(i).setHightLinght(true);// 高亮显示
			} else if (downCallBinary.charAt(i) == '0') {
				downCallParamItems.get(i).setHightLinght(false);
			}

		}

		for (int i = 0; i < floorNumber - downCallBinary.length() - 1; i++) {// 移除多余数据显示
			downCallParamItems.remove(downCallParamItems.size() - 1);
		}

		downCallButtonParamItemAdapter.notifyDataSetChanged();// 通知数据更新

		int gridViewHight = getGridViewHeight(downCallGridView);

		call_vp_contains.getLayoutParams().height = gridViewHight
				+ DisplayUtil.dip2px(this, 50);
	}

	/**
	 * 显示上召唤数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param responseData
	 *            返回数据
	 */
	public void showUpCallData(ResponseData responseData) {
		initUpCallData();
		String upCallData = responseData.getData();
		// if (upCallData.length() != getDataLen()) {// 上召唤数据长度不正确
		// return;
		// }

		String binaryString = DataPaseUtil.hexString2BinaryRe(upCallData);
		String upCallBinary = "";
		if (upCallData.length() >= getDataLen()) {// 服务器返回数据长度大于 按实际楼层长度数据,
			upCallBinary = binaryString.substring(0, floorNumber - 1);// 0-39
																		// 窃取数据
		} else {
			upCallBinary = binaryString;
		}

		// 上召唤数据

		for (int i = 0; i < upCallBinary.length(); i++) {

			if (upCallBinary.charAt(i) == '1') {
				upCallParamItems.get(i).setHightLinght(true);// 高亮显示
			} else if (upCallBinary.charAt(i) == '0') {
				upCallParamItems.get(i).setHightLinght(false);
			}

		}

		for (int i = 0; i < floorNumber - upCallBinary.length() - 1; i++) {// 移除多余数据显示
			upCallParamItems.remove(upCallParamItems.size() - 1);
		}

		upCallButtonParamItemAdapter.notifyDataSetChanged();// 通知数据更新

		int gridViewHight = getGridViewHeight(upCallGridView);

		call_vp_contains.getLayoutParams().height = gridViewHight
				+ DisplayUtil.dip2px(this, 50);

	}

	private void showLayout() {

		if (type == TYPE_CALL) {
			call_command_rl_call.setVisibility(View.VISIBLE);
			call_command_rl_command.setVisibility(View.GONE);
		} else {
			call_command_rl_call.setVisibility(View.GONE);
			call_command_rl_command.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 计算GridView高度
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

	@Override
	protected void addListeners() {

		super.addListeners();

		call_vp_contains.setOnPageChangeListener(new OnPageChangeListener() {// 滑动分页监听事件

					@Override
					public void onPageSelected(int position) {
						draw_Point(position);

						if (position == 0) {// 开始上召唤，停止下召唤
							stopDownCall();
							startUpCall();
							synTask.uiLog(Protocol.UI_UP_CALL);
							viewpostion = 0;

						}
						if (position == 1) {// 停止上召唤开启下召唤
							stopUpCall();
							startDownCall();
							synTask.uiLog(Protocol.UI_DOWN_CALL);
							viewpostion = 1;
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

		head_btn_command_call.setOnClickListener(new OnClickListener() {// 指令召唤

					@Override
					public void onClick(View v) {
						switch (type) {
						case TYPE_CALL:// 召唤切换为指令
							type = TYPE_COMMAND;
							stopDownCall();
							stopUpCall();// 停止上下召唤监控
							startCommand();// 开始命令监控
							synTask.uiLog(Protocol.UI_COMMAND);
							break;
						case TYPE_COMMAND:// 指令切换为召唤
							type = TYPE_CALL;
							stopCommand();
							if (viewpostion == 0) {
								startUpCall();
								synTask.uiLog(Protocol.UI_UP_CALL);
							}
							if (viewpostion == 1) {
								startDownCall();
								synTask.uiLog(Protocol.UI_DOWN_CALL);
							}

							break;

						}

						head_btn_command_call.setText(getBtnString());// 设置按钮显示的字
						head_tv_title.setText(getTitleString());// 设置标题文字
						showLayout();// 显示或隐藏不仅
					}
				});

		call_ll_draw_point.setOnClickListener(new PonitClickListner());

	}

	private class PonitClickListner implements OnClickListener {

		@Override
		public void onClick(View v) {

			int index = call_vp_contains.getCurrentItem();
			if (index == pageViews.size() - 1) {
				index = -1;
			}

			call_vp_contains.setCurrentItem(index + 1, true);
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
	 * 初始化召唤数据化数据
	 * 
	 * @author 泰得利通 wanglu
	 */

	private void initCallData() {

		initUpCallData();
		initDownCallData();
	}

	/**
	 * 初始化下召唤数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initDownCallData() {

		downCallParamItems = new ArrayList<ButtonParamItem>();

		for (int i = 2; i <= floorNumber; i++) {

			ButtonParamItem buttonParamItem = new ButtonParamItem();
			buttonParamItem.setItemName(i + "");
			downCallParamItems.add(buttonParamItem);

		}

		downCallButtonParamItemAdapter = new ButtonParamItemAdapter(
				downCallParamItems, this);
		downCallGridView.setAdapter(downCallButtonParamItemAdapter);
	}

	/**
	 * 初始化上召唤数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initUpCallData() {
		upCallParamItems = new ArrayList<ButtonParamItem>();

		for (int i = 1; i <= floorNumber - 1; i++) {

			ButtonParamItem buttonParamItem = new ButtonParamItem();
			buttonParamItem.setItemName(i + "");
			upCallParamItems.add(buttonParamItem);

		}

		upCallButtonParamItemAdapter = new ButtonParamItemAdapter(
				upCallParamItems, this);
		upCallGridView.setAdapter(upCallButtonParamItemAdapter);

	}

	private String getBtnString() {

		if (type == TYPE_CALL) {
			return getResources().getString(R.string.monitor_btn_command);
		} else {
			return getResources().getString(R.string.monitor_btn_call);
		}
	}

	/**
	 * 初始化上召唤和下召唤试图
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initSubView() {
		pageViews = new ArrayList<View>();
		View upCallView = View.inflate(this, R.layout.up_call_layout, null);// 上召唤
		View downCallView = View.inflate(this, R.layout.down_call_layout, null);// 下召唤
		upCallGridView = (GridView) upCallView.findViewById(R.id.up_call_gv);
		downCallGridView = (GridView) downCallView
				.findViewById(R.id.down_call_gv);

		pageViews.add(upCallView);
		pageViews.add(downCallView);

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
			call_ll_draw_point.addView(imageView, layoutParams);

			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.point_hightl_inght);
			}
			imageViews.add(imageView);

		}

	}

	@Override
	protected CharSequence getTitleText() {

		return getTitleString();

	}

	private String getTitleString() {
		if (type == TYPE_CALL) {
			return getResources().getString(R.string.ui_title_call);
		} else {
			return getResources().getString(R.string.ui_title_command);
		}
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_monitor_call_command_layout;
	}

	class CallViewAdapter extends PagerAdapter {
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

	/**
	 * 根据楼层计算数据长度
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	private int getDataLen() {

		int byteLen = 0;

		if (floorNumber % 8 == 0) {
			byteLen = floorNumber / 8;
		} else {
			byteLen = floorNumber / 8 + 1;
		}

		return byteLen * 2;

	}

}
