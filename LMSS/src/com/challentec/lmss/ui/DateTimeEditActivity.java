package com.challentec.lmss.ui;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.challentec.lmss.adapter.NumericWheelAdapter;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.listener.OnWheelChangedListener;
import com.challentec.lmss.util.DisplayUtil;
import com.challentec.lmss.view.WheelView;

/**
 * 日期修改 activity
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class DateTimeEditActivity extends TabContentBaseActivity {

	private static int START_YEAR = 2013, END_YEAR = 2063;
	public static final String INTENT_ITEM_KEY = "intent_item_key";
	private TextView tv_date_time;

	@Override
	protected void onSave() {
		paramsItem.setItemValue(tv_date_time.getText().toString());
		Intent intent = new Intent();
		intent.setAction(ListParamActivity.ACTION_ITEM_UPDATE);
		intent.putExtra(ListParamActivity.INTENT_UPDATE_ITEM_KEY, paramsItem);
		sendBroadcast(intent);

		MainTabActivity.instance.back();

	}

	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;
	private ParamsItem paramsItem;

	@Override
	protected void initMainView(View mainView) {
		hideOrShowSaveBtn(true);
		tv_date_time = (TextView) mainView.findViewById(R.id.tv_date_time);
		initDatePicker(mainView);
		paramsItem = (ParamsItem) getIntent().getExtras().get(INTENT_ITEM_KEY);

		tv_date_time.setText(paramsItem.getItemValue());// 设置系统时间
	}

	@Override
	protected CharSequence getTitleText() {

		return paramsItem.getItemName();
	}

	/**
	 * 初始化日历控件
	 */
	private void initDatePicker(View parentView) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 年
		wv_year = (WheelView) parentView.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel(getString(R.string.unit_year));// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) parentView.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel(getString(R.string.unit_month));
		wv_month.setCurrentItem(month);

		// 日
		wv_day = (WheelView) parentView.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setLabel(getString(R.string.unit_day));
		wv_day.setCurrentItem(day - 1);

		// 时
		wv_hours = (WheelView) parentView.findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCyclic(true);
		wv_hours.setCurrentItem(hour);

		// 分
		wv_mins = (WheelView) parentView.findViewById(R.id.mins);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wv_mins.setCyclic(true);
		wv_mins.setCurrentItem(minute);

		// 秒

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}

				showTime();
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}

				showTime();
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
		wv_day.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showTime();

			}
		});

		wv_hours.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showTime();

			}
		});

		wv_mins.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showTime();

			}
		});

		// 根据屏幕密度来指定选择器字体的大小
		int textSize = 0;

		textSize = DisplayUtil.dip2px(this, 12);

		wv_day.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

	}

	private void showTime() {

		String parten = "00";
		DecimalFormat decimal = new DecimalFormat(parten);

		tv_date_time.setText((wv_year.getCurrentItem() + START_YEAR) + "-"
				+ decimal.format((wv_month.getCurrentItem() + 1)) + "-"
				+ decimal.format((wv_day.getCurrentItem() + 1)) + " "
				+ decimal.format(wv_hours.getCurrentItem()) + ":"
				+ decimal.format(wv_mins.getCurrentItem()) + ":00");

	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_datetime_edit_layout;
	}

}
