package com.challentec.lmss.ui;

import java.util.List;

import com.challentec.lmss.adapter.ParamsItemAdapter;
import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.TroublieDetailParser;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.LoadProgressView;

import android.view.View;
import android.widget.ListView;

/**
 * 故障详细信息
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class TroubleDetailActivity extends TabContentBaseActivity {

	public static final String INTENT_KEY_TID = "intent_key_tid";
	public static final String INTENT_TROUBLE_TITLE_KEY = "intent_trouble_key_title";
	private String tid;
	private int itemNameIds[] = {

	R.string.trouble_detail_tv_datetime, R.string.trouble_detail_tv_time,
			R.string.trouble_detail_tv_no,
			R.string.trouble_detail_tv_current_location,
			R.string.trouble_detail_tv_current_floor,
			R.string.trouble_detail_tv_input_info,
			R.string.trouble_detail_tv_out_info,
			R.string.trouble_detail_tv_curve_info,
			R.string.trouble_detail_tv_give_speed,
			R.string.trouble_detail_tv_feedback_speed,
			R.string.trouble_detail_tv_parent_voltage,
			R.string.trouble_detail_tv_out_elec,
			R.string.trouble_detail_tv_out_rate,
			R.string.trouble_deatil_tv_zj_elec

	};

	private static final int DATA_LEN = 64;
	private ListView params_lv;
	private LoadProgressView params_pb;
	private List<ParamsItem> paramsItems;
	private String title;

	@Override
	protected void initMainView(View mainView) {
		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new TroubleDetailMessageListener());
		params_lv = (ListView) mainView.findViewById(R.id.params_lv);
		params_pb = (LoadProgressView) mainView.findViewById(R.id.params_pb);
		tid = getIntent().getExtras().getString(INTENT_KEY_TID);
		title = getIntent().getExtras().getString(INTENT_TROUBLE_TITLE_KEY);

		if (tid != null) {
			loadData();
		}

	}

	private class TroubleDetailMessageListener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(Protocol.B_GET_TROUBLE_D)) {// 详细信息
				if (responseData.isSuccess()) {
					showData(responseData);

				} else {
					UIHelper.showToask(TroubleDetailActivity.this,
							AppTipMessage.getResouceStringId(responseData
									.getErrorCode()));
				}

				params_pb.setVisibility(View.GONE);
			}
		}

	}

	/**
	 * 加载数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void loadData() {
		params_pb.setVisibility(View.VISIBLE);
		SynTask synTask = new SynTask(new SynHandler(), appContext);

		String apiData = ClientAPI.getHexApiStr(Protocol.B_GET_TROUBLE_D, tid);
		synTask.writeData(apiData);
		synTask.uiLog(Protocol.UI_TROUBLE_DETAIL);
	}

	/**
	 * 显示故障详细信息
	 *@author 泰得利通 wanglu
	 * @param responseData
	 */
	protected void showData(ResponseData responseData) {
		if (responseData.getData().length() != DATA_LEN) {
			UIHelper.showToask(this, AppTipMessage
					.getResouceStringId(AppTipMessage.DATA_FORMAT_ERROR));// 格
			return;
		}
		paramsItems = TroublieDetailParser.getParamsItems(this,
				responseData.getData(), itemNameIds);
		params_lv.setAdapter(new ParamsItemAdapter(paramsItems, this));

	}

	@Override
	protected CharSequence getTitleText() {

		return title;
	}

	@Override
	protected int getMainViewLayoutId() {
		return R.layout.activity_params_list_layout;
	}

}
