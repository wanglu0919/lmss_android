package com.challentec.lmss.ui;

import java.util.List;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.TestFunctionParamsParser;
import com.challentec.lmss.util.Protocol;

/**
 * 
 * @author 泰得利通 wanglu 测试->测试功能参数
 * 
 */
public class DeviceTestFunctionParamsActivity extends ListParamActivity {

	private int itemNameIds[] = {
			R.string.test_test_function_inner_command_regist,
			R.string.test_test_function_up_call_regist,
			R.string.test_test_function_down_call_regist,
			R.string.test_test_function_random_run_time,
			R.string.test_test_function_random_run_space,
			R.string.test_test_function_random_run_enabled,
			R.string.test_test_function_call_enabled,
			R.string.test_test_function_open_door_enabled,
			R.string.test_test_function_over_load_enabled

	};

	private static final int DATA_LEN = 14;

	@Override
	protected CharSequence getTitleText() {
		return getResources().getString(R.string.ui_title_test_function_params);
	}

	@Override
	protected int getMainViewLayoutId() {
		return R.layout.activity_params_list_layout;
	}

	@Override
	protected String getUICode() {
		return Protocol.UI_TEST_FUNCTION_P;
	}

	@Override
	protected String getFunctionCommand() {
		return Protocol.T_GET_TEST_P;
	}

	@Override
	protected List<ParamsItem> getListItems(ResponseData responseData) {
		return TestFunctionParamsParser.getParamsItems(this,
				responseData.getData(), itemNameIds);
	}

	@Override
	protected int getDataLen() {
		return DATA_LEN;
	}

	@Override
	protected String getHexSaveData() {
		return TestFunctionParamsParser.getHexSaveData(paramsItems);
	}

	@Override
	protected String getSaveFuncitonCode() {
		return Protocol.T_SAVE_TEST_P;
	}

	@Override
	protected void onEditItemSuccess(ParamsItem paramsItem) {
	
		
	}

	@Override
	protected void onSaveSuccess() {
		
		
	}

}
