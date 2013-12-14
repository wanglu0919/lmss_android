package com.challentec.lmss.ui;

import android.view.View;
import android.widget.Button;

import com.challentec.lmss.app.AppTipMessage;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.LoadProgressView;

/**
 * 设置->设备更换
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ChangeDeviceActivity extends TabContentBaseActivity {
	private LoadProgressView change_lp;
	private Button change_dev_btn_change;// 设备更换按钮
	private SynTask synTask;
	@Override
	protected void addListeners() {

		super.addListeners();
		change_dev_btn_change.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeDevice();//
			}
		});
	}

	@Override
	protected void initMainView(View mainView) {
		 synTask = new SynTask(new SynHandler()

		, appContext);
		change_lp = (LoadProgressView) mainView.findViewById(R.id.change_lp);
		change_dev_btn_change = (Button) mainView
				.findViewById(R.id.change_dev_btn_change);
		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new ChangeDeviceLinseter());
		synTask.uiLog(Protocol.UI_CHANGE_DEVICE);

	}

	private class ChangeDeviceLinseter implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(Protocol.S_CHANGE_DEVICE)) {// 处理设备更换消息

				if (responseData.isSuccess()) {
					UIHelper.showToask(ChangeDeviceActivity.this,
							R.string.tip_msg_changedevice_success);
				} else {
					UIHelper.showToask(ChangeDeviceActivity.this, AppTipMessage
							.getResouceStringId(responseData.getErrorCode()));
				}

				change_lp.setVisibility(View.GONE);
			}

		}

	}

	/**
	 * 设备更换
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void changeDevice() {
		change_lp.setVisibility(View.VISIBLE);
		
		

		String apiData = ClientAPI.getApiStr(Protocol.S_CHANGE_DEVICE);

		synTask.writeData(apiData,true);

	}

	@Override
	protected CharSequence getTitleText() {

		return getString(R.string.ui_title_change_device);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_change_device_layout;
	}

}
