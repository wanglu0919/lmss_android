package com.challentec.lmss.ui;

import java.util.List;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.MainBoardParser;
import com.challentec.lmss.util.Protocol;
/**
 * 主板设置
 * @author 泰得利通 wanglu
 *
 */
public class MainBoardActivity extends ListParamActivity {

	private static final int DATA_LEN = 14;

	private int[] itemNameIds = { R.string.setting_mainbord_funciton_x1_x8,
			R.string.setting_mainbord_funciton_x11_x18,
			R.string.setting_mainbord_funciton_x21_x28,
			R.string.setting_mainbord_funciton_x27,
			R.string.setting_mainbord_funciton_x28,
			R.string.setting_mainbord_funciton_y7,
			R.string.setting_mainbord_funciton_y8

	};

	@Override
	protected CharSequence getTitleText() {

		return getResources().getString(
				R.string.ui_title_mainbord_input_setting);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_params_list_layout;
	}

	@Override
	protected String getUICode() {
		return Protocol.UI_MAINBOARD_INPUT_SET;
	}

	@Override
	protected String getFunctionCommand() {
		return Protocol.S_GET_MAINBOARD;
	}

	@Override
	protected List<ParamsItem> getListItems(ResponseData responseData) {
		return MainBoardParser.getParamsItems(this, responseData.getData(),
				itemNameIds);
	}

	@Override
	protected int getDataLen() {
		return DATA_LEN;
	}

	@Override
	protected String getHexSaveData() {
		
		return MainBoardParser.getSaveHexData(paramsItems);
	}

	@Override
	protected String getSaveFuncitonCode() {
		return Protocol.S_SAVE_MAINBOARD;
	}

	@Override
	protected void onEditItemSuccess(ParamsItem paramsItem) {
		
	}

	@Override
	protected void onSaveSuccess() {
		
	}

}
