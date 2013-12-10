package com.challentec.lmss.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ButtonParamItem;

/**
 * 端口状态项目适配器
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ButtonParamItemAdapter extends BaseAdapter {

	private Context context;
	private List<ButtonParamItem> buttonParamItems;

	public ButtonParamItemAdapter(List<ButtonParamItem> portItems,
			Context context) {
		this.buttonParamItems = portItems;
		this.context = context;
	}

	@Override
	public int getCount() {
		return buttonParamItems.size();
	}

	@Override
	public Object getItem(int position) {
		return buttonParamItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView itemName;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ButtonParamItem port = buttonParamItems.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.poart_state_mainboard_item, null);

			viewHolder = new ViewHolder();

			viewHolder.itemName = (TextView) convertView;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (port.isHightLinght()) {// 高亮
			viewHolder.itemName.setBackgroundResource(R.drawable.item_btn_bg_hight);
		} else {
			viewHolder.itemName.setBackgroundResource(R.drawable.item_btn_bg);
		}
		viewHolder.itemName.setText(port.getItemName());

		return convertView;

	}

}
