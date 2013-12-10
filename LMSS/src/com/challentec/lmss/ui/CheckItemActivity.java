package com.challentec.lmss.ui;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.challentec.lmss.adapter.CheckItemAdapter;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.CheckItem;
import com.challentec.lmss.bean.ParamsItem;

/**
 * 有选择项目的activity
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class CheckItemActivity extends TabContentBaseActivity {

	private ListView check_lv;
	private List<CheckItem> checkItems;
	public static final String INTENT_ITEM_KEY = "intent_item_key";

	private CheckItemAdapter checkAdapter;
	private ParamsItem paramsItem;

	@Override
	protected void initMainView(View mainView) {
		check_lv = (ListView) mainView.findViewById(R.id.check_lv);
		initData();

	}

	private void initData() {

		paramsItem = (ParamsItem) getIntent().getExtras().get(INTENT_ITEM_KEY);
		checkItems = paramsItem.getCheckItems();

		checkAdapter = new CheckItemAdapter(checkItems, this);
		check_lv.setAdapter(checkAdapter);

	}

	@Override
	protected CharSequence getTitleText() {

		return paramsItem.getItemName();
	}

	@Override
	protected void addListeners() {

		super.addListeners();
		check_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {

				CheckItem checkItem = checkItems.get(position);
				if (checkItem.isCheck()) {
					return;
				}

				for (int i = 0; i < checkItems.size(); i++) {
					if (i == position) {
						checkItems.get(position).setCheck(true);
					} else {
						checkItems.get(i).setCheck(false);
					}
				}

				checkAdapter.notifyDataSetChanged();

				//paramsItem.setCheckItems(checkItems);
				/*发送广播 更新数据*/
				Intent intent = new Intent();
				intent.setAction(ListParamActivity.ACTION_ITEM_UPDATE);
				intent.putExtra(ListParamActivity.INTENT_UPDATE_ITEM_KEY,
						paramsItem);
				sendBroadcast(intent);

				MainTabActivity.instance.back();

			}
		});

	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_check_layout;
	}

}
