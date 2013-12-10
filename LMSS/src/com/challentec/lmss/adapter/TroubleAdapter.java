package com.challentec.lmss.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.Trouble;

/**
 * 故障列表数据适配器
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class TroubleAdapter extends BaseAdapter {

	private Context context;
	private List<Trouble> troubles;
	private Drawable imgMore;// 更多图标
	private Drawable imgData_Warring;// 右边数据错图标

	public TroubleAdapter(List<Trouble> troubles, Context context) {
		this.troubles = troubles;
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
		return troubles.size();
	}

	@Override
	public Object getItem(int position) {
		return troubles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView trouble_item_tv_order;
		TextView trouble_item_tv_time;
		TextView trouble_item_tv_no;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Trouble trouble = troubles.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.trouble_item_layout,
					null);

			viewHolder = new ViewHolder();

			viewHolder.trouble_item_tv_order = (TextView) convertView
					.findViewById(R.id.trouble_item_tv_order);
			viewHolder.trouble_item_tv_time = (TextView) convertView
					.findViewById(R.id.trouble_item_tv_time);
			viewHolder.trouble_item_tv_no = (TextView) convertView
					.findViewById(R.id.trouble_item_tv_no);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.trouble_item_tv_order.setText(trouble.getT_order());
		viewHolder.trouble_item_tv_time.setText(trouble.getT_time());
		viewHolder.trouble_item_tv_no.setText(trouble.getT_no());

		if (trouble.isError()) {// 检查数据是是否合法

			viewHolder.trouble_item_tv_no.setCompoundDrawables(null, null,
					imgData_Warring, null);// 显示数据错误提示信息

		} else {

			viewHolder.trouble_item_tv_no.setCompoundDrawables(null, null,
					imgMore, null);
		}

		return convertView;

	}

}
