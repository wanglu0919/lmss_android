package com.challentec.lmss.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.RelativeLayout;

import com.challentec.lmss.adapter.ButtonParamItemAdapter;
import com.challentec.lmss.app.AppCache;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ButtonParamItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.listener.MonitorListener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.recever.MonitorRecever;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.DisplayUtil;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;

/**
 * 端口状态
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class PortStateActivity extends TabContentBaseActivity {

	private static final int NUM_COLUMNS = 5;
	private ViewPager port_state_vp_contains;
	private LinearLayout port_sate_ll_draw_point;
	private List<View> pageViews;
	private List<ImageView> imageViews;
	private String mainBordInputNames[] = { "X1", "X2", "X3", "X4", "X5", "X6",
			"X7", "X8", "X11", "X12", "X13", "X14", "X15", "X16", "X17", "X18",
			"X21", "X22", "X23", "X24", "X25", "X26", "X27", "X28", "X31",
			"X32", "X33", "X34", "X35", "X36", "X37", "X38"

	};

	private String mainBoardOutNames[] = {

	"Y1", "Y2", "Y3", "Y4", "Y5", "Y6", "Y7", "Y8", "Y11", "Y12", "Y13", "Y14",
			"Y15", "Y16", "Y17", "Y18" };

	private String jdbInputNames[] = { "1AP", "1SK", "1SG", "2AP", "2SK",
			"2SG", "KM", "GM", "ZS", "KMY", "SJ", "DL", "XY", "CZ", "MZ" };
	private String jdbOutputNames[] = { "1KM", "1GM", "2KM", "2GM", "SDZ",
			"XDZ" };

	private GridView mainBoardInputGridView;// 主板输入
	private GridView mainBoardOutputGridView;// 主板输出
	private List<ButtonParamItem> mainBoardInputportItems;// 主板输入
	private List<ButtonParamItem> mainBoardoutportItems;// 主板输出
	private GridView jdbInputGridView;// 轿顶板输入

	private GridView jdbOutputGridView;// 轿顶板输出
	private List<ButtonParamItem> jdbInputportItems;// 轿顶板输入
	private List<ButtonParamItem> jdbOutputortItems;// 轿顶板输出

	private RelativeLayout port_state_rl_call;// 召唤
	private RelativeLayout port_state_rl_command;// 指令
	private static int PORT_STATE1_DATA_LEN = 12;// 端口数据一数据长度
	private static int PORT_STATE2_DATA_LEN = 6;// 端口数据2
	private SynTask synTask;
	private ButtonParamItemAdapter mainboardInputAdapter;// 主板输入列表适配器
	private ButtonParamItemAdapter mainBoardOutAdapter;// 主板输出列表适配器
	private ButtonParamItemAdapter jdbParamsItemAdapter;// 轿顶板输入适配器
	private ButtonParamItemAdapter jdboutButtonParamItemAdapter;// 轿顶板输出适配器
	private int viewPostion = 0;
	private MonitorRecever monitorRecever;
	public static final String UI_NAME = "PORT_STATE";

	@Override
	protected void onBack() {
		stopMonitor();// 停止当前的健康

		Intent intent = new Intent();
		intent.setAction(MonitorRecever.ACTION_MONTION);// 发送广播通知 监控页面开始监空数据
		sendBroadcast(intent);
		AppCache.cache(AppCache.CACHE_UI, MonitorActivity.UI_NAME);
		super.onBack();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppCache.cache(AppCache.CACHE_UI, UI_NAME);// 存储当前UI
		registerMonitorRecever();// 注册端口状态广播监听

	}

	@Override
	protected void initMainView(View mainView) {
		synTask = new SynTask(new SynHandler(), appContext);
		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new PortStateMessageListner());// 注册监听
		port_state_vp_contains = (ViewPager) mainView
				.findViewById(R.id.port_state_vp_contains);
		port_sate_ll_draw_point = (LinearLayout) mainView
				.findViewById(R.id.port_sate_ll_draw_point);
		port_state_rl_call = (RelativeLayout) mainView
				.findViewById(R.id.port_state_rl_call);
		port_state_rl_command = (RelativeLayout) mainView
				.findViewById(R.id.port_state_rl_command);

		initSubView();
		initPointView();

		port_state_vp_contains.setAdapter(new PortStatePagerViewAdapter());

		port_state_vp_contains.setCurrentItem(0);
	
		initData();
		startPortMonitor_1();// 开始监控端口状态1

		
		int space=DisplayUtil.dip2px(this,20);
		mainBoardInputGridView.getLayoutParams().height = getGridViewHeight(mainBoardInputGridView)+space;
		mainBoardOutputGridView.getLayoutParams().height = getGridViewHeight(mainBoardOutputGridView)+space;

		jdbInputGridView.getLayoutParams().height = getGridViewHeight(jdbInputGridView)+space;

		jdbOutputGridView.getLayoutParams().height = getGridViewHeight(jdbOutputGridView)+space;
		synTask.uiLog(Protocol.UI_PORT_STATE_1);// 记录

	}

	/**
	 * 开始监控端口状态1
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void startPortMonitor_1() {
		String apiData = ClientAPI.getApiStr(Protocol.M_START_PORT_1);
		synTask.writeData(apiData);

	}

	/**
	 * 停止端口状态1监控
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void stopPortMonitor_1() {
		String apiData = ClientAPI.getApiStr(Protocol.M_END_PORT_1);
		synTask.writeData(apiData);
	}

	/**
	 * 开始监控端口状态2
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void startPortMonitor_2() {
		String apiData = ClientAPI.getApiStr(Protocol.M_START_PORT_2);
		synTask.writeData(apiData);
	}

	/**
	 * 停止端口状态2监控
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void stopPortMonitor_2() {
		String apiData = ClientAPI.getApiStr(Protocol.M_END_PORT_2);
		synTask.writeData(apiData);
	}

	/**
	 * 消息监听
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class PortStateMessageListner implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(Protocol.M_START_PORT_1)) {// 开始端口监控

				if (responseData.isSuccess()) {

					showPortState1(responseData);//

				}

			} else if (responseData.getFunctionCode().equals(
					Protocol.M_START_PORT_2)) {

				if (responseData.isSuccess()) {
					showPoartState2(responseData);

				}
			}

		}

	}

	/**
	 * 初始化数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initData() {

		initMainBoardInputData();
		initMainBoardOutputData();
		initJdbInputData();
		initJdbOutputData();
	}

	/**
	 * 显示端口2数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param responseData
	 */
	public void showPoartState2(ResponseData responseData) {
		String portData2 = responseData.getData();
		if (portData2.length() != PORT_STATE2_DATA_LEN) {
			UIHelper.showToask(appContext, "端口2返回数据长度错误");
			return;
		}

		String binaryString = DataPaseUtil.hexString2BinaryRe(portData2);

		String jdbIputStr = binaryString.substring(0, 15);// 轿顶板输入数据
		String jdbOutStr = binaryString.substring(16, 22);// 轿顶板输出数据
		// 更新轿顶板输入界面
		for (int i = 0; i < jdbIputStr.length(); i++) {
			if (jdbIputStr.charAt(i) == '1') {
				jdbInputportItems.get(i).setHightLinght(true);// 高亮显示
			} else if (jdbIputStr.charAt(i) == '0') {
				jdbInputportItems.get(i).setHightLinght(false);

			}

		}
		jdbParamsItemAdapter.notifyDataSetChanged();// 通知数据更新

		// *更新轿顶板输出*/
		for (int i = 0; i < jdbOutStr.length(); i++) {
			if (jdbOutStr.charAt(i) == '1') {
				jdbOutputortItems.get(i).setHightLinght(true);// 高亮显示
			} else if (jdbOutStr.charAt(i) == '0') {
				jdbOutputortItems.get(i).setHightLinght(false);

			}

		}
		jdboutButtonParamItemAdapter.notifyDataSetChanged();// 通知数据更新

	}

	/**
	 * 显示端口一数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param responseData
	 */
	public void showPortState1(ResponseData responseData) {

		String portState1Data = responseData.getData();
		if (portState1Data.length() != PORT_STATE1_DATA_LEN) {// 数据长度不够，格式有误
			UIHelper.showToask(appContext, "端口1返回数据长度错误");
			return;
		}

		String binaryString = DataPaseUtil.hexString2BinaryRe(portState1Data);

		String mainBoardInputData = binaryString.substring(0, 32);// 主板输入数据
		String mainBoardOutData = binaryString.substring(32, 48);

		/**
		 * 更新主板输入状态
		 */
		for (int i = 0; i < mainBoardInputData.length(); i++) {

			if (mainBoardInputData.charAt(i) == '0') {
				mainBoardInputportItems.get(i).setHightLinght(false);
			} else if (mainBoardInputData.charAt(i) == '1') {
				mainBoardInputportItems.get(i).setHightLinght(true);// 高亮显示
			}

		}

		mainboardInputAdapter.notifyDataSetChanged();// 同通知数据改变了

		for (int i = 0; i < mainBoardOutData.length(); i++) {

			if (mainBoardOutData.charAt(i) == '0') {
				mainBoardoutportItems.get(i).setHightLinght(false);
			} else if (mainBoardOutData.charAt(i) == '1') {
				mainBoardoutportItems.get(i).setHightLinght(true);
			}

		}

		mainBoardOutAdapter.notifyDataSetChanged();// 通知数据改变

	}

	private void initJdbOutputData() {
		jdbInputportItems = new ArrayList<ButtonParamItem>();

		for (String jdbInputName : jdbInputNames) {
			ButtonParamItem portItem = new ButtonParamItem();
			portItem.setItemName(jdbInputName);

			jdbInputportItems.add(portItem);
		}

		jdboutButtonParamItemAdapter = new ButtonParamItemAdapter(
				jdbInputportItems, this);
		jdbInputGridView.setAdapter(jdboutButtonParamItemAdapter);
	}

	private class PonitClickListner implements OnClickListener {

		@Override
		public void onClick(View v) {

			int index = port_state_vp_contains.getCurrentItem();
			if (index == pageViews.size() - 1) {
				index = -1;
			}

			port_state_vp_contains.setCurrentItem(index + 1, true);
		}

	}

	/**
	 * 初始化轿顶板输入数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initJdbInputData() {

		jdbOutputortItems = new ArrayList<ButtonParamItem>();

		for (String jdbOutputName : jdbOutputNames) {
			ButtonParamItem portItem = new ButtonParamItem();
			portItem.setItemName(jdbOutputName);

			jdbOutputortItems.add(portItem);
		}

		jdbParamsItemAdapter = new ButtonParamItemAdapter(jdbOutputortItems,
				this);
		jdbOutputGridView.setAdapter(jdbParamsItemAdapter);

	}

	/**
	 * 初始化主板输入数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initMainBoardInputData() {
		mainBoardInputportItems = new ArrayList<ButtonParamItem>();

		for (String mainBoardInputName : mainBordInputNames) {
			ButtonParamItem portItem = new ButtonParamItem();
			portItem.setItemName(mainBoardInputName);

			mainBoardInputportItems.add(portItem);
		}

		mainboardInputAdapter = new ButtonParamItemAdapter(
				mainBoardInputportItems, this);

		mainBoardInputGridView.setAdapter(mainboardInputAdapter);

	}

	/**
	 * 初始化主板输出数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initMainBoardOutputData() {
		mainBoardoutportItems = new ArrayList<ButtonParamItem>();

		for (String mainBoardoutName : mainBoardOutNames) {
			ButtonParamItem portItem = new ButtonParamItem();
			portItem.setItemName(mainBoardoutName);

			mainBoardoutportItems.add(portItem);
		}
		mainBoardOutAdapter = new ButtonParamItemAdapter(mainBoardoutportItems,
				this);
		mainBoardOutputGridView.setAdapter(mainBoardOutAdapter);

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
			port_sate_ll_draw_point.addView(imageView, layoutParams);

			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.point_hightl_inght);
			}
			imageViews.add(imageView);

		}

	}

	@Override
	protected void addListeners() {

		super.addListeners();
		port_state_vp_contains
				.setOnPageChangeListener(new OnPageChangeListener() {// 滑动分页监听事件

					@Override
					public void onPageSelected(int position) {

						draw_Point(position);

						if (position == 0) {
							synTask.uiLog(Protocol.UI_PORT_STATE_1);// 记录
							viewPostion = 0;
							stopPortMonitor_2();
							startPortMonitor_1();

						} else if (position == 1) {
							synTask.uiLog(Protocol.UI_PORT_STATE_2);// 记录
							stopPortMonitor_1();
							startPortMonitor_2();
							viewPostion = 1;

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

		port_state_rl_call.setOnClickListener(new OnClickListener() {// 召唤

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(PortStateActivity.this,
								CallCommandActivity.class);
						intent.putExtra(CallCommandActivity.TYPE_KEY,
								CallCommandActivity.TYPE_CALL);
						MainTabActivity.instance.addView(intent);

						stopMonitor();// 停止端口监控

					}
				});

		port_state_rl_command.setOnClickListener(new OnClickListener() {// 指令

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(PortStateActivity.this,
								CallCommandActivity.class);
						intent.putExtra(CallCommandActivity.TYPE_KEY,
								CallCommandActivity.TYPE_COMMAND);
						MainTabActivity.instance.addView(intent);
						stopMonitor();// 停止端口监控
					}
				});

		port_sate_ll_draw_point.setOnClickListener(new PonitClickListner());

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
	 * 注册监控
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void registerMonitorRecever() {

		monitorRecever = new MonitorRecever();
		monitorRecever.setMonitorListener(new PortStateMoniListener());
		IntentFilter filter = new IntentFilter();
		filter.addAction(MonitorRecever.ACTION_PORT_STATE);
		this.registerReceiver(monitorRecever, filter);

	}

	/**
	 * 端口状态开始监听
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class PortStateMoniListener implements MonitorListener {

		@Override
		public void monitor(Intent intent) {

			if (viewPostion == 0) {
				startPortMonitor_1();
			} else if (viewPostion == 1) {
				startPortMonitor_2();
			}
		}

	}

	/**
	 * 初始化主板输入视图，机轿顶板输入输入设置视图
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initSubView() {
		pageViews = new ArrayList<View>();
		View mainBordView = View.inflate(this,
				R.layout.port_state_mainbord_layout, null);// 电机自学习视图

		View jdBordView = View.inflate(this, R.layout.port_state_jdbord_layout,
				null);// 井道自学习视图

		mainBoardInputGridView = (GridView) mainBordView
				.findViewById(R.id.port_mainboard_gv_input);
		mainBoardOutputGridView = (GridView) mainBordView
				.findViewById(R.id.port_mainboard_gv_output);

		jdbInputGridView = (GridView) jdBordView
				.findViewById(R.id.port_jdb_gv_input);
		jdbOutputGridView = (GridView) jdBordView
				.findViewById(R.id.port_jdb_gv_output);
		pageViews.add(mainBordView);
		pageViews.add(jdBordView);

	}

	@Override
	protected CharSequence getTitleText() {
		return getResources().getString(R.string.ui_title_monitor_port_state);
	}

	@Override
	protected void onDestroy() {

		unregisterReceiver(monitorRecever);// 取消广播监听
		super.onDestroy();

	}

	/**
	 * 停止端口状态监控
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void stopMonitor() {
		if (viewPostion == 0) {
			stopPortMonitor_1();// 停止端口一状态监控
		} else if (viewPostion == 1) {
			stopPortMonitor_2();// 停止端口状态二监控
		}
	}

	@Override
	protected void onPause() {

		super.onPause();

	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_port_state_layout;
	}

	class PortStatePagerViewAdapter extends PagerAdapter {
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

}
