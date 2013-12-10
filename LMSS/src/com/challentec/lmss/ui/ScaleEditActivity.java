package com.challentec.lmss.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.challentec.lmss.adapter.NumericWheelAdapter;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.listener.OnWheelChangedListener;
import com.challentec.lmss.util.DisplayUtil;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.WheelView;

/**
 * 比例修改 Activity
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ScaleEditActivity extends TabContentBaseActivity {

	public static final String INTENT_ITEM_KEY = "intent_item_key";
	private TextView tv_data_pre;
	private TextView tv_data_back;
	private ParamsItem paramsItem;
	private WheelView wl_pre_scale;// 前项比
	private WheelView wl_back_scale;// 后项比

	@Override
	protected void onSave() {

		int preData = Integer.parseInt(tv_data_pre.getText().toString());
		int backData = Integer.parseInt(tv_data_back.getText().toString());
		if (paramsItem.checkScaleData(preData, backData)) {
			paramsItem.setItemValue(preData + ":" + backData);
			Intent intent = new Intent();
			intent.setAction(ListParamActivity.ACTION_ITEM_UPDATE);
			intent.putExtra(ListParamActivity.INTENT_UPDATE_ITEM_KEY,
					paramsItem);
			sendBroadcast(intent);

			MainTabActivity.instance.back();

		} else {
			UIHelper.showToask(appContext,
					R.string.tip_msg_edit_scal_data_error);
		}

	}

	@Override
	protected void initMainView(View mainView) {
		hideOrShowSaveBtn(true);

		tv_data_pre = (TextView) mainView.findViewById(R.id.tv_data_pre);
		tv_data_back = (TextView) mainView.findViewById(R.id.tv_data_back);
		paramsItem = (ParamsItem) getIntent().getExtras().get(INTENT_ITEM_KEY);
		initData();

		initDatePicker(mainView);

	}

	/**
	 * 初始数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void initData() {

		String scaleData[] = paramsItem.getItemValue().split(":");
		tv_data_pre.setText(scaleData[0]);
		tv_data_back.setText(scaleData[1]);

	}

	@Override
	protected CharSequence getTitleText() {

		return paramsItem.getItemName();
	}

	/**
	 * 初始化日历控件
	 */
	private void initDatePicker(View parentView) {

		// 年
		wl_pre_scale = (WheelView) parentView.findViewById(R.id.wl_pre_scale);
		wl_pre_scale.setAdapter(new NumericWheelAdapter(paramsItem
				.getPreStart(), paramsItem.getPreEnd()));
		wl_pre_scale.setCyclic(true);// 可循环滚动

		wl_pre_scale.setCurrentItem(0);// 初始化时显示的数据

		// 月
		wl_back_scale = (WheelView) parentView.findViewById(R.id.wl_back_scale);
		wl_back_scale.setAdapter(new NumericWheelAdapter(paramsItem
				.getBackStart(), paramsItem.getBackEnd()));
		wl_back_scale.setCyclic(true);

		wl_back_scale.setCurrentItem(0);

		// 秒

		// 添加"年"监听
		OnWheelChangedListener wheelListener_preScale = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(newValue==0){
					newValue=paramsItem.getPreEnd();
				}
				tv_data_pre.setText(newValue + "");
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_backScale = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(newValue==0){
					newValue=paramsItem.getBackEnd();
				}
				tv_data_back.setText(newValue + "");
			}
		};

		wl_pre_scale.addChangingListener(wheelListener_preScale);
		wl_back_scale.addChangingListener(wheelListener_backScale);

		// 根据屏幕密度来指定选择器字体的大小
		int textSize = 0;

		textSize = DisplayUtil.dip2px(this, 12);

		wl_pre_scale.TEXT_SIZE = textSize;
		wl_back_scale.TEXT_SIZE = textSize;

	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_scale_edit_layout;
	}

}
