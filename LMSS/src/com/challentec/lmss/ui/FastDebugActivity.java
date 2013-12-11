package com.challentec.lmss.ui;

import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.listener.AppMessageLinstener;
import com.challentec.lmss.listener.KeyBoardPressListener;
import com.challentec.lmss.net.SynHandler;
import com.challentec.lmss.net.SynTask;
import com.challentec.lmss.util.ClientAPI;
import com.challentec.lmss.util.DataPaseUtil;
import com.challentec.lmss.util.Protocol;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.KeyBoardInputWediget;

/**
 * 快速调试
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class FastDebugActivity extends TabContentBaseActivity {

	private KeyBoardInputWediget keyBoard;

	private Drawable imgData_Warring;
	private Button main_btn_send;
	private EditText edit_back_tv, edit_tv_send;
	private SynTask synTask;

	@Override
	protected void onBack() {

		super.onBack();

	}

	@Override
	protected void initMainView(View mainView) {

		keyBoard = (KeyBoardInputWediget) mainView
				.findViewById(R.id.et_keyboard);

		imgData_Warring = this.getResources().getDrawable(
				R.drawable.item_data_waring);
		imgData_Warring.setBounds(0, 0, imgData_Warring.getMinimumWidth(),
				imgData_Warring.getMinimumHeight());

		edit_back_tv = (EditText) mainView.findViewById(R.id.edit_back_tv);
		edit_tv_send = (EditText) mainView.findViewById(R.id.edit_tv_send);
		main_btn_send = (Button) mainView.findViewById(R.id.main_btn_send);
		edit_back_tv.setInputType(InputType.TYPE_NULL);
		edit_tv_send.setInputType(InputType.TYPE_NULL);

		keyBoard.setDataType(ParamsItem.VALUE_INT);// 设置键盘数据类型

		synTask = new SynTask(new SynHandler(){

			@Override
			public void onSendSuccess() {
				UIHelper.showToask(appContext, "发送成功");
				
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
					
					edit_back_tv.setText(DataPaseUtil.hexStrToInt(responseData.getData())+"");// 服务器返回数据
				}
			}

		}

	}

	@Override
	protected void addListeners() {
		super.addListeners();
		keyBoard.setKeyBoardPressListener(new FastOnKeyBoardListener());

		edit_tv_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showOrHideKeyBoard();
			}
		});
		main_btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkInput()) {
					sendData();
				}
			}
		});
	}

	/**
	 * 发送数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	protected void sendData() {

		if (checkInput()) {
			String hexData = DataPaseUtil.getHexStr(
					Integer.parseInt(edit_tv_send.getText().toString()), 1);
			String apiData = ClientAPI.getHexApiStr(Protocol.A_TEST_SEND,
					hexData);
			synTask.writeData(apiData);
			
		}
	}

	/**
	 * 显示或隐藏键盘
	 * 
	 * @author 泰得利通 wanglu
	 */
	protected void showOrHideKeyBoard() {
		if (keyBoard.getVisibility() == View.GONE) {
			keyBoard.setVisibility(View.VISIBLE);
		} else {
			keyBoard.setVisibility(View.GONE);
		}

	}

	private class FastOnKeyBoardListener implements KeyBoardPressListener {

		@Override
		public void onPress(int keyCode) {
			edit_tv_send.setCompoundDrawables(null, null, null, null);// 先清空错误图标
			switch (keyCode) {

			case KeyBoardInputWediget.KEY_CODE_A:

				setTextData("A");
				break;

			case KeyBoardInputWediget.KEY_CODE_B:
				setTextData("B");
				break;
			case KeyBoardInputWediget.KEY_CODE_C:
				setTextData("C");
				break;
			case KeyBoardInputWediget.KEY_CODE_D:
				setTextData("D");
				break;
			case KeyBoardInputWediget.KEY_CODE_E:
				setTextData("E");
				break;
			case KeyBoardInputWediget.KEY_CODE_F:
				setTextData("F");
				break;
			case KeyBoardInputWediget.KEY_CODE_CANCLE:
				edit_tv_send.setText("");
				break;
			case KeyBoardInputWediget.KEY_CODE_OK:
				showOrHideKeyBoard();
				break;
			case KeyBoardInputWediget.KEY_CODE_DOT:

				break;
			case KeyBoardInputWediget.KEY_CODE_ZERON:
				setTextData("0");
				break;
			case KeyBoardInputWediget.KEY_CODE_ONE:
				setTextData("1");
				break;
			case KeyBoardInputWediget.KEY_CODE_TWO:
				setTextData("2");
				break;
			case KeyBoardInputWediget.KEY_CODE_THREE:
				setTextData("3");
				break;
			case KeyBoardInputWediget.KEY_CODE_FOUR:
				setTextData("4");
				break;
			case KeyBoardInputWediget.KEY_CODE_FIVE:
				setTextData("5");
				break;
			case KeyBoardInputWediget.KEY_CODE_SIX:
				setTextData("6");
				break;
			case KeyBoardInputWediget.KEY_CODE_SEVEN:
				setTextData("7");
				break;
			case KeyBoardInputWediget.KEY_CODE_EIGHT:
				setTextData("8");
				break;
			case KeyBoardInputWediget.KEY_CODE_NINE:
				setTextData("9");
				break;

			}
		}

	}

	/**
	 * 设置数据
	 * 
	 * @author 泰得利通 wanglu
	 * @param data
	 */
	private void setTextData(String data) {
		edit_tv_send.append(data);
		checkInput();
	}

	private boolean checkInput() {

		String textData = edit_tv_send.getText().toString();

		if (textData.equals("")) {
			UIHelper.showToask(this, R.string.tip_msg_edit_input_empty);
			return false;
		}

		try {
			int inData = Integer.parseInt(textData);

			if (inData >= 0 && inData <= 255) {

				return true;
			} else {
				edit_tv_send.setCompoundDrawables(null, null, imgData_Warring,
						null);// 显示数据错误提示信息
				return false;
			}

		} catch (NumberFormatException e) {
			UIHelper.showToask(this, getString(R.string.tip_form_number_big));
			edit_tv_send
					.setCompoundDrawables(null, null, imgData_Warring, null);// 显示数据错误提示信息
			e.printStackTrace();
			return false;
		}

	}

	@Override
	protected CharSequence getTitleText() {

		return "测试";
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_fast_debug_layout;
	}

}
