package com.challentec.lmss.ui;

import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClinetAPI;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.Protocol;

/**
 * 快速调试
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class FastDebug2Activity extends TabContentBaseActivity {

	private SynTask synTask;

	private Button test_btn_up;
	private Button test_btn_down;
	private Button test_btn_stop;
	private EditText test_et_one, test_et_two, test_et_three, test_et_four;

	@Override
	protected void onBack() {

		super.onBack();

	}

	@Override
	protected void initMainView(View mainView) {

		test_btn_up = (Button) mainView.findViewById(R.id.test_btn_up);
		test_btn_down = (Button) mainView.findViewById(R.id.test_btn_down);
		test_btn_stop = (Button) mainView.findViewById(R.id.test_btn_stop);

		test_et_one = (EditText) mainView.findViewById(R.id.test_et_one);
		test_et_two = (EditText) mainView.findViewById(R.id.test_et_two);

		test_et_three = (EditText) mainView.findViewById(R.id.test_et_three);
		test_et_four = (EditText) mainView.findViewById(R.id.test_et_four);

		test_et_one.setInputType(InputType.TYPE_NULL);
		test_et_two.setInputType(InputType.TYPE_NULL);
		test_et_three.setInputType(InputType.TYPE_NULL);
		test_et_four.setInputType(InputType.TYPE_NULL);

		synTask = new SynTask(new SynHandler() {

			@Override
			public void onSendSuccess() {
				// UIHelper.showToask(appContext, "发送成功");

			}

		}, appContext);

		appManager.registerAppMessageRecever(this).setAppMessageLinstener(
				new FastMessageListener());
	}

	private class FastMessageListener implements AppMessageLinstener {

		@Override
		public void onRespnseDataReceve(ResponseData responseData) {

			if (responseData.getFunctionCode().equals(Protocol.A_TEST_RECEIVE)) {

				if (responseData.isSuccess()) {
					showRecevieData(responseData.getData());
				}
			}

		}

	}

	@Override
	protected void addListeners() {
		super.addListeners();

		test_btn_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String apiData = ClinetAPI.getHexApiStr(Protocol.A_TEST_SEND,
						"01");
				synTask.writeData(apiData);
			}
		});

		test_btn_down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String apiData = ClinetAPI.getHexApiStr(Protocol.A_TEST_SEND,
						"03");
				synTask.writeData(apiData);
			}
		});

		test_btn_stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String apiData = ClinetAPI.getHexApiStr(Protocol.A_TEST_SEND,
						"02");
				synTask.writeData(apiData);

			}
		});
	}

	/**
	 * 显示数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 */
	public void showRecevieData(String data) {
		
		if(data.length()==8){
			int dataOne = DataPaseUtil.hexStrToInt(data.substring(0, 2));
			int dataTwo = DataPaseUtil.hexStrToInt(data.substring(2, 4));
			int dataThree = DataPaseUtil.hexStrToInt(data.substring(4, 6));
			int dataFour = DataPaseUtil.hexStrToInt(data.substring(6, 8));
			test_et_one.setText(dataOne + "");
			test_et_two.setText(dataTwo + "");
			test_et_three.setText(dataThree + "");
			test_et_four.setText(dataFour + "");
		}
		
	}

	@Override
	protected CharSequence getTitleText() {

		return "测试";
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_fast_debug_layout2;
	}

}
