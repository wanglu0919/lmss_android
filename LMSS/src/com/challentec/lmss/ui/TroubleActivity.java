package com.challentec.lmss.ui;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.challentec.lmss.adapter.TroubleAdapter;
import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.bean.Trouble;
import com.challentec.lmss.engine.TroubleListParser;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.LoadProgressView;

/**
 * 故障activity
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class TroubleActivity extends TabContentBaseActivity {

	private ListView trouble_lv;// 故障列表控件
	private LoadProgressView trouble_lp_pb;
	private List<Trouble> troubles;

	@Override
	protected void initMainView(View mainView) {
		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new TroubleMessageLinterner());
		trouble_lv = (ListView) mainView.findViewById(R.id.trouble_lv);
		trouble_lp_pb = (LoadProgressView) mainView
				.findViewById(R.id.trouble_lp_pb);
		loadData();

	}

	private class TroubleMessageLinterner implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(Protocol.B_GET_TROUBLE)) {
				if (responseData.isSuccess()) {
					showData(responseData.getData());

				} else {
					UIHelper.showToask(TroubleActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}

				trouble_lp_pb.setVisibility(View.GONE);
			}

		}

	}

	/**
	 * 加载数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void loadData() {

		trouble_lp_pb.setVisibility(View.VISIBLE);
		SynTask synTask = new SynTask(new SynHandler(), appContext);

		String apiData = ClientAPI.getApiStr(Protocol.B_GET_TROUBLE);
		synTask.writeData(apiData,true);
		synTask.uiLog(Protocol.UI_TROUBLE_HOME);// 日志

	}

	/**
	 * 显示故障列表
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 */
	protected void showData(String data) {

		troubles = TroubleListParser.getTroubles(data, this);
		trouble_lv.setAdapter(new TroubleAdapter(troubles, this));
	}

	@Override
	protected void addListeners() {
		super.addListeners();
		trouble_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Trouble t = troubles.get(position);
				int t_id = Integer.parseInt(t.getT_id());
				String hexTid = Integer.toHexString(t_id);
				if (hexTid.length() < 2) {
					hexTid = "0" + hexTid;
				}
				Intent intent = new Intent(TroubleActivity.this,
						TroubleDetailActivity.class);

				intent.putExtra(TroubleDetailActivity.INTENT_KEY_TID, hexTid);
				intent.putExtra(TroubleDetailActivity.INTENT_TROUBLE_TITLE_KEY, t.getT_order());
				MainTabActivity.instance.addView(intent);
			}
		});
	}

	@Override
	protected CharSequence getTitleText() {

		return getResources().getString(R.string.ui_title_trouble);
	}

	@Override
	protected int getMainViewLayoutId() {
		return R.layout.activity_trouble_layout;
	}

}
