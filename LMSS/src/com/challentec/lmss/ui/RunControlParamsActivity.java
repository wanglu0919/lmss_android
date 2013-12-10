package com.challentec.lmss.ui;

import java.util.List;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.RunControlParamsParser;
import com.challentec.lmss.util.Protocol;

/**
 * 
 * @author 泰得利通 wanglu 测试->运行控制参数
 * 
 */
public class RunControlParamsActivity extends ListParamActivity {

	private int itemNameIds[] = {

	R.string.test_run_control_start_speed,
			R.string.test_run_control_start_speed_keep_time,
			R.string.test_run_control_start_zero_speed_time,
			R.string.test_run_control_curve_give_delayed,
			R.string.test_run_control_accelerated_speed_slope,
			R.string.test_run_control_inflection_point_accelerated_speed_1,
			R.string.test_run_control_inflection_point_accelerated_speed_2,
			R.string.test_run_control_slowdown_speed_slope,
			R.string.test_run_control_inflection_point_slowdown_speed_1,
			R.string.test_run_control_inflection_point_slowdown_speed_2,
			R.string.test_run_control_special_slowdown_speed,
			R.string.test_run_control_park_distance,
			R.string.test_run_control_again_speed,
			R.string.test_run_control_repair,
			R.string.test_run_control_reverse_again_speed,
			R.string.test_run_control_again_adjust };

	private static final int DATA_LEN = 62;

	@Override
	protected CharSequence getTitleText() {
		return getResources().getString(R.string.ui_title_run_control_params);
	}

	@Override
	protected int getMainViewLayoutId() {
		return R.layout.activity_params_list_layout;
	}

	@Override
	protected String getUICode() {
		return Protocol.UI_RUN_CONTROL_P;
	}

	@Override
	protected String getFunctionCommand() {
		return Protocol.T_GET_RUN_P;
	}

	@Override
	protected List<ParamsItem> getListItems(ResponseData responseData) {
		return RunControlParamsParser.getParamsItems(this,
				responseData.getData(), itemNameIds);
	}

	@Override
	protected int getDataLen() {
		return DATA_LEN;
	}

	@Override
	protected String getHexSaveData() {
		
		return RunControlParamsParser.getHexSaveData(paramsItems);
	}

	@Override
	protected String getSaveFuncitonCode() {
		
		return Protocol.T_SAVE_RUN_P;
	}

	@Override
	protected void onEditItemSuccess(ParamsItem paramsItem) {
		
	}

	@Override
	protected void onSaveSuccess() {
		
	}

}
