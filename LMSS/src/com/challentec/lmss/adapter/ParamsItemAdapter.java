package com.challentec.lmss.adapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.util.DisplayUtil;
import com.challentec.lmss.view.SwitchButton;

/**
 * 楼层显示设置数据适配器
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ParamsItemAdapter extends BaseAdapter {

	private Context context;
	private List<ParamsItem> paramsItems;

	private Drawable imgMore;//更多图标
	private Drawable imgData_Warring;// 右边数据错图标
	private Set<Integer> checkPositionSet = new HashSet<Integer>();

	public ParamsItemAdapter(List<ParamsItem> paramsItems, Context context) {
		this.paramsItems = paramsItems;
		this.context = context;
		imgMore = context.getResources().getDrawable(R.drawable.item_more_icon);
		imgMore.setBounds(0, 0, imgMore.getMinimumWidth(),
				imgMore.getMinimumHeight());
		imgData_Warring = context.getResources().getDrawable(
				R.drawable.item_data_waring);
		imgData_Warring.setBounds(0, 0, imgData_Warring.getMinimumWidth(),
				imgData_Warring.getMinimumHeight());
	}

	@Override
	public int getCount() {
		return paramsItems.size();
	}

	@Override
	public Object getItem(int position) {
		return paramsItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView itemName;
		TextView itemValue;
		SwitchButton switchButton;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ParamsItem paramsItem = paramsItems.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.params_item_layout,
					null);

			viewHolder = new ViewHolder();

			viewHolder.itemName = (TextView) convertView
					.findViewById(R.id.params_item_name);
			viewHolder.itemValue = (TextView) convertView
					.findViewById(R.id.params_item_value);
			viewHolder.switchButton = (SwitchButton) convertView
					.findViewById(R.id.params_item_switch_button);
			// 解决SwitchButton显示bug
			if (convertView.getLayoutParams() != null) {
				convertView.getLayoutParams().height = DisplayUtil.dip2px(
						context, 45);
			} else {
				AbsListView.LayoutParams params = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.FILL_PARENT,
						DisplayUtil.dip2px(context, 45));
				convertView.setLayoutParams(params);
			}

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.itemName.setText(paramsItem.getItemName());

		int valueType = paramsItem.getValueType();
		if (valueType != ParamsItem.VALUE_TYPE_ON_OFF) {// 不是开关按钮类型
			viewHolder.itemValue.setText(paramsItem.getItemValue()
					+ paramsItem.getUnit());
			viewHolder.itemValue.setVisibility(View.VISIBLE);
			viewHolder.switchButton.setVisibility(View.GONE);
		} else {// 快关按钮
			viewHolder.itemValue.setVisibility(View.GONE);
			viewHolder.switchButton.setVisibility(View.VISIBLE);
			viewHolder.switchButton.setChecked(paramsItem.getOFF_ON());
			viewHolder.switchButton.setOnCheckedChangeListener(paramsItem
					.getOnCheckedChangeListener());// 设置开关的监听器

		}

		if (paramsItem.checkValue()) {// 检查数据是是否合法
			if(valueType==ParamsItem.VAALUE_TYPE_TROUBLE){
				viewHolder.itemValue
				.setCompoundDrawables(null, null, null, null);
			}else{
				viewHolder.itemValue
				.setCompoundDrawables(null, null, imgMore, null);
			}
			

		} else {
			viewHolder.itemValue.setCompoundDrawables(null, null,
					imgData_Warring, null);// 显示数据错误提示信息
		}

		
		
		if (checkPositionSet.contains(position)) {// 选中
			convertView.setBackgroundColor(Color.parseColor("#6699FF"));
			viewHolder.itemValue.setTextColor(Color.GREEN);
		} else {
			convertView.setBackgroundColor(Color.WHITE);
			viewHolder.itemValue.setTextColor(Color.parseColor("#44A8E1"));
		}

		return convertView;

	}

	public void addCheckPositon(Integer checkPosition) {
		this.checkPositionSet.add(checkPosition);
	}

	public void clearCheckPostion() {
		checkPositionSet.clear();
	}

}
