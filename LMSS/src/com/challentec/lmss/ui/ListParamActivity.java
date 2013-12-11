package com.challentec.lmss.ui;

import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.challentec.lmss.adapter.ParamsItemAdapter;
import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.LoadProgressView;

/**
 * 参数列表类的父类抽象 *
 * 
 * @author 泰得利通 wanglu
 * 
 */
public abstract class ListParamActivity extends TabContentBaseActivity {

	protected List<ParamsItem> paramsItems;

	protected LoadProgressView params_pb;

	protected ListView params_lv;
	protected ParamsItemAdapter paramsItemAdapter;
	public static final String ACTION_ITEM_UPDATE = "action_item_update";
	public static final String INTENT_UPDATE_ITEM_KEY = "intent_update_item";
	private SynTask sysTask;// 异步任务
	private ProgressDialog pd_save;
	private EditRecever editRecever;
	private boolean checkLen = true;

	@Override
	protected void onSave() {

		if (checkItems()) {
			saveData();

		} else {

			UIHelper.showToask(appContext,
					R.string.tip_msg_list_save_data_error);

		}

	}

	/**
	 * 是否检查数据长度
	 * 
	 * @author 泰得利通 wanglu
	 * @param isCheckLen
	 */
	public void setCheckLen(boolean isCheckLen) {
		checkLen = isCheckLen;
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		unregisterReceiver(editRecever);// 移除广播
	}

	/**
	 * 保存数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void saveData() {
		pd_save.show();// 显示进度对话框
		String apiData = ClientAPI.getHexApiStr(getSaveFuncitonCode(),
				getHexSaveData());
		sysTask.writeData(apiData);
	}

	/**
	 * 页面数据部分
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	protected abstract String getHexSaveData();

	/**
	 * 保存数据命令 code
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	protected abstract String getSaveFuncitonCode();

	/**
	 * 检查输入项十分否合法
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	private boolean checkItems() {
		boolean enable = true;
		for (ParamsItem paramsItem : paramsItems) {

			if (!paramsItem.checkValue()) {// 检查未通过
				enable = false;
				break;
			}

		}

		return enable;
	}

	@Override
	protected void addListeners() {

		super.addListeners();

		/**
		 * 列表点击事件处理
		 */
		params_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				ParamsItem paramsItem = paramsItems.get(position);
				Intent intent = null;

				int valueType = paramsItem.getValueType();

				switch (valueType) {
				case ParamsItem.VALUE_TYPE_CHECK:// 可选项数据
					// if (!paramsItem.isCheckItemError()) {// 检查可选项数据是否有误
					intent = new Intent(ListParamActivity.this,
							CheckItemActivity.class);
					intent.putExtra(CheckItemActivity.INTENT_ITEM_KEY,
							paramsItem);
					// }

					break;
				case ParamsItem.VALUE_TYPE_NOMAIL_SCALE:// 比列数据
					intent = new Intent(ListParamActivity.this,
							ScaleEditActivity.class);
					intent.putExtra(ScaleEditActivity.INTENT_ITEM_KEY,
							paramsItem);
					break;
				case ParamsItem.VALUE_TYPE_DATE_TIME:// 时间类型数据
					intent = new Intent(ListParamActivity.this,
							DateTimeEditActivity.class);
					intent.putExtra(DateTimeEditActivity.INTENT_ITEM_KEY,
							paramsItem);
					break;
				case ParamsItem.VALUE_TYPE_HEX:// 16进值类型数据

					intent = new Intent(ListParamActivity.this,
							EditItemActivity.class);
					intent.putExtra(EditItemActivity.INTENT_ITEM_KEY,
							paramsItem);
					break;
				case ParamsItem.VALUE_TYPE_NOMAL:// 普通类型文本数据
					intent = new Intent(ListParamActivity.this,
							EditItemActivity.class);
					intent.putExtra(EditItemActivity.INTENT_ITEM_KEY,
							paramsItem);
					break;

				}

				paramsItemAdapter.notifyDataSetChanged();

				if (intent != null) {

					MainTabActivity.instance.addView(intent);
				}

			}
		});

	}

	@Override
	protected void initMainView(View mainView) {
		pd_save = new ProgressDialog(this);
		pd_save.setMessage(getString(R.string.tip_msg_list_pb_save_data));
		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new ListMessageLinstener());
		params_lv = (ListView) mainView.findViewById(R.id.params_lv);
		params_pb = (LoadProgressView) mainView.findViewById(R.id.params_pb);
		hideOrShowSaveBtn(true);// 显示保存按钮

		loadData();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		registerEditeRecever();
	}

	/**
	 * 注册编辑更改接受者
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void registerEditeRecever() {

		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_ITEM_UPDATE);
		editRecever = new EditRecever();
		registerReceiver(editRecever, filter);

	}

	private class ListMessageLinstener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {
			if (responseData.getFunctionCode().equals(getFunctionCommand())) {// 获取页面参数功能
				if (responseData.isSuccess()) {

					showData(responseData);

				} else {
					UIHelper.showToask(ListParamActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}
				params_pb.setVisibility(View.GONE);

			} else if (responseData.getFunctionCode().equals(
					getSaveFuncitonCode())) {// 保存页面参数数据功能

				if (responseData.isSuccess()) {

					UIHelper.showToask(appContext,
							R.string.tip_msg_list_save_data_success);
					paramsItemAdapter.clearCheckPostion();
					paramsItemAdapter.notifyDataSetChanged();
					
					onSaveSuccess();
				} else {
					UIHelper.showToask(ListParamActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}

				pd_save.dismiss();
			}

		}

	}
	
	/**
	 * 保存数据成功
	 *@author 泰得利通 wanglu
	 */
	protected abstract void onSaveSuccess();

	/**
	 * 加载数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void loadData() {
		params_pb.setVisibility(View.VISIBLE);
		sysTask = new SynTask(new SynHandler(), appContext);

		String apiData = ClientAPI.getApiStr(getFunctionCommand());
		sysTask.writeData(apiData);
		sysTask.uiLog(getUICode());// 日志

	}

	/**
	 * UI日志记录命令
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	protected abstract String getUICode();

	/**
	 * 获取数据功能命令
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	protected abstract String getFunctionCommand();

	/**
	 * 显示数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param responseData
	 *            返回的数据
	 */
	protected void showData(ResponseData responseData) {

		if (responseData.getData().length() != getDataLen() && checkLen) {// 检查数据长度
			UIHelper.showToask(this, AppTipMessage
					.getResouceStringId(AppTipMessage.DATA_FORMAT_ERROR));// 格式有误
			return;

		}

		paramsItems = getListItems(responseData);

		paramsItemAdapter = new ParamsItemAdapter(paramsItems, this);
		params_lv.setAdapter(paramsItemAdapter);

	}

	/**
	 * 获取列表数据内容 子类实现
	 * 
	 * @author 泰得利通 wanglu
	 * @param responseData
	 *            返回数据包装
	 * @return
	 */
	protected abstract List<ParamsItem> getListItems(ResponseData responseData);

	/**
	 * 数据的长度
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	protected abstract int getDataLen();

	/**
	 * 编辑框项更改事件
	 * 
	 * @author 泰得利通 wanglu
	 * 
	 */
	private class EditRecever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			ParamsItem paramsItem = (ParamsItem) intent.getExtras().get(
					INTENT_UPDATE_ITEM_KEY);

			if (paramsItem != null) {

				paramsItems.set(paramsItem.getOrder(), paramsItem);
				paramsItemAdapter.addCheckPositon(paramsItem.getOrder());
				paramsItemAdapter.notifyDataSetChanged();
				onEditItemSuccess(paramsItem);
			}

		}

	}
	
	/**
	 * 修改项成功
	 *@author 泰得利通 wanglu
	 * @param paramsItem
	 */
	protected abstract void onEditItemSuccess(ParamsItem paramsItem);

}
