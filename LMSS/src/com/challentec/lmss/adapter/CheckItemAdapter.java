package com.challentec.lmss.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.CheckItem;

/**
 * 端口状态项目适配器
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class CheckItemAdapter extends BaseAdapter {

	private Context context;
	private List<CheckItem> checkItems;

	public CheckItemAdapter(List<CheckItem> checkItems, Context context) {
		this.checkItems = checkItems;
		this.context = context;
	}

	@Override
	public int getCount() {
		return checkItems.size();
	}

	@Override
	public Object getItem(int position) {
		return checkItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView itemName;
		ImageView itemIcon;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		CheckItem checkItem = checkItems.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.check_item_layout,
					null);

			viewHolder = new ViewHolder();

			viewHolder.itemName = (TextView) convertView
					.findViewById(R.id.check_tv_name);
			viewHolder.itemIcon = (ImageView) convertView
					.findViewById(R.id.check_iv_icon);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.itemName.setText(checkItem.getItemName());

		if (checkItem.isCheck()) {
			viewHolder.itemIcon.setImageResource(R.drawable.item_checked);
		} else {
			viewHolder.itemIcon.setImageResource(R.drawable.item_unchecked);
		}

		return convertView;

	}

}
