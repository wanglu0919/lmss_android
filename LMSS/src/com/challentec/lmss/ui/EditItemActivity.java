package com.challentec.lmss.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.listener.KeyBoardPressListener;
import com.challentec.lmss.util.UIHelper;
import com.challentec.lmss.view.KeyBoardInputWediget;

/**
 * 编辑输入框
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class EditItemActivity extends TabContentBaseActivity {
	private EditText edit_tv;
	private KeyBoardInputWediget keyBoard;
	private ParamsItem paramsItem;
	private TextView edit_tv_limit;// 范围
	private int dataType;
	private Drawable imgData_Warring;

	@Override
	protected void onBack() {

		super.onBack();

	}

	public static final String INTENT_ITEM_KEY = "intent_item_key";

	@Override
	protected void initMainView(View mainView) {
		hideOrShowSaveBtn(true);
		edit_tv = (EditText) mainView.findViewById(R.id.edit_tv);
		keyBoard = (KeyBoardInputWediget) mainView
				.findViewById(R.id.et_keyboard);
		edit_tv_limit = (TextView) mainView.findViewById(R.id.edit_tv_limit);

		paramsItem = (ParamsItem) getIntent().getExtras().get(INTENT_ITEM_KEY);

		imgData_Warring = this.getResources().getDrawable(
				R.drawable.item_data_waring);
		imgData_Warring.setBounds(0, 0, imgData_Warring.getMinimumWidth(),
				imgData_Warring.getMinimumHeight());
		if (paramsItem != null) {
			initData();// 初始化
		}

	}

	/**
	 * 初始化数据 wanglu 泰得利通
	 */
	private void initData() {

		edit_tv.setText(paramsItem.getItemValue());//
		edit_tv_limit.setText(getString(R.string.tip_form_range_front)
				+ paramsItem.getLimitString() + " " + paramsItem.getUnit()
				+ "," + getString(R.string.tip_form_range_back));
		dataType = paramsItem.getDataType();
		
		edit_tv.setSelection(paramsItem.getItemValue().length());
		checkInput();// 检查数据是否合法
		keyBoard.setDataType(dataType);// 设置键盘数据类型

	}

	/**
	 * 检查输入
	 * 
	 * @author 泰得利通 wanglu
	 * @return
	 */
	private boolean checkInput() {

		String textData = edit_tv.getText().toString();

		if (textData.equals("")) {

			showErrorIcon();
			return false;
		}

		if (dataType == ParamsItem.VALUE_INT) {// 整形数据

			try {
				int inData = Integer.parseInt(textData);

				if (paramsItem.checkIValue(inData)) {

					return true;
				} else {
					showErrorIcon();
					return false;
				}

			} catch (NumberFormatException e) {
				/*
				 * UIHelper.showToask(this,
				 * getString(R.string.tip_form_number_big));
				 */
				showErrorIcon();
				e.printStackTrace();
				return false;
			}

		} else if (dataType == ParamsItem.VALUE_FLOAT) {// 浮点型数据

			if (textData.indexOf(".") == -1) {
				showErrorIcon();
				return false;
			}

			if (textData.length() - 1 <= textData.indexOf(".")) {
				showErrorIcon();
				return false;
			}
			String floatStr = textData.split("\\.")[1];
			String intStr=textData.split("\\.")[0];
			if(intStr.length()>1&&intStr.startsWith("0")){//处理这种输入 010.52
				
				showErrorIcon();
				return false;
			}
			

			if (floatStr == null
					|| (floatStr != null && floatStr.length() != paramsItem
							.getAccuracy())) {// 精确度不够
				showErrorIcon();
				return false;
			}
			
			

			float fdata = Float.parseFloat(textData);

			if (paramsItem.checkIValue(fdata)) {

				return true;
			} else {
				showErrorIcon();
				return false;
			}

		} else if (dataType == ParamsItem.VALUE_HEX) {// 16进制数

			if (paramsItem.checkHexValue(textData)) {

				return true;
			} else {
				showErrorIcon();
				return false;
			}

		}

		return false;

	}

	/**
	 * 显示错误图标
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void showErrorIcon() {
		edit_tv.setCompoundDrawables(null, null, imgData_Warring, null);// 显示数据错误提示信息

	}

	@Override
	protected void addListeners() {
		super.addListeners();
		keyBoard.setKeyBoardPressListener(new EditeOnKeyBoardListener());
		edit_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showOrHideKeyBoard();

			}
		});

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

	private class EditeOnKeyBoardListener implements KeyBoardPressListener {

		@Override
		public void onPress(int keyCode) {
			edit_tv.setCompoundDrawables(null, null, null, null);// 先清空错误图标
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
				clearText();
				break;
			case KeyBoardInputWediget.KEY_CODE_OK:
				showOrHideKeyBoard();
				break;
			case KeyBoardInputWediget.KEY_CODE_DOT:
				String textData = edit_tv.getText().toString();
				if (textData.equals("") || textData.indexOf('.') != -1) {
					return;
				}

				setTextData(".");
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

		int index = edit_tv.getSelectionStart(); // 获取光标位置
		Editable editable = edit_tv.getText();
		editable.insert(index, data);

		checkInput();
	}

	/**
	 * 清楚数据
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void clearText() {

		
		int index = edit_tv.getSelectionStart();
		Editable editable = edit_tv.getText();
		if(index==0&&!editable.toString().equals("")){
			index=editable.toString().length();
		}
		if(index>=1){
			editable.delete(index - 1, index);
		}
		
		
		checkInput();// 检查数据是否合法
	}

	@Override
	protected void onSave() {

		if (checkInput()) {
			String data = edit_tv.getText().toString();
			if (dataType == ParamsItem.VALUE_FLOAT && data.indexOf(".") == -1) {// 小数处理
				data = data + ".0";
			} else if (dataType == ParamsItem.VALUE_FLOAT
					&& data.indexOf(".") != -1) {
				String floatStr = data.split("\\.")[1];
				String backStr = "";
				for (int i = 0; i < paramsItem.getAccuracy()
						- floatStr.length(); i++) {// 精确度不够，小数后面补0处理
					backStr += "0";
				}

				data = data + backStr;
			} else if (dataType == ParamsItem.VALUE_HEX
					&& data.length() < paramsItem.getByteLen() * 2) {// 字节长度处理,长度不够

				String pre = "";

				for (int i = 0; i < paramsItem.getByteLen() * 2 - data.length(); i++) {// 长度不够，高位补0
					pre += "0";
				}

				data = pre + data;

			}
			paramsItem.setItemValue(data.toUpperCase());

			/* 发送广播 更新数据 */
			Intent intent = new Intent();
			intent.setAction(ListParamActivity.ACTION_ITEM_UPDATE);
			intent.putExtra(ListParamActivity.INTENT_UPDATE_ITEM_KEY,
					paramsItem);
			sendBroadcast(intent);

			MainTabActivity.instance.back();

		} else {
			UIHelper.showToask(appContext,
					R.string.tip_msg_edit_input_data_check);
		}

	}

	@Override
	protected CharSequence getTitleText() {

		return paramsItem.getItemName();
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_edit_layout;
	}

}
