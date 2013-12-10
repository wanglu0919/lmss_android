package com.challentec.lmss.ui;

import java.util.List;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.TimeControlParamsParser;
import com.challentec.lmss.util.Protocol;

/**
 * 
 * @author 泰得利通 wanglu 时间控制参数
 * 
 */
public class TimeControlParamsActivity extends ListParamActivity {

	private int itemNameIds[] = {
			R.string.test_time_control_one_way_run_time_limit,
			R.string.test_time_control_start_prevent_jump_delay,
			R.string.test_time_control_curve_give_delay,
			R.string.test_time_control_open_switch_delay,
			R.string.test_time_control_free_switch_delay,
			R.string.test_time_control_dir_backout_delay,
			R.string.test_time_control_flat_sensor_delay,
			R.string.test_time_control_repair_park_delay,
			R.string.test_time_control_auto_open_door_time,
			R.string.test_time_control_back_base_station_delay,
			R.string.test_time_control_save_energy_control_time,
			R.string.test_time_control_auto_switch_time,
			R.string.test_time_control_ups_run_protected_time,
			R.string.test_time_control_arrive_station_keep_time,
			R.string.test_time_control_firemen_run_delay,

	};

	private static final int DATA_LEN = 52;

	@Override
	protected CharSequence getTitleText() {
		return getResources().getString(R.string.ui_title_time_control_params);
	}

	@Override
	protected int getMainViewLayoutId() {
		return R.layout.activity_params_list_layout;
	}

	@Override
	protected String getUICode() {
		return Protocol.UI_TIME_CONTROL_P;
	}

	@Override
	protected String getFunctionCommand() {
		return Protocol.T_GET_TIME_P;
	}

	@Override
	protected List<ParamsItem> getListItems(ResponseData responseData) {
		return TimeControlParamsParser.getParamsItems(this,
				responseData.getData(), itemNameIds);
	}

	@Override
	protected int getDataLen() {
		return DATA_LEN;
	}

	@Override
	protected String getHexSaveData() {
		return TimeControlParamsParser.getTimeHexData(paramsItems);
	}

	@Override
	protected String getSaveFuncitonCode() {
		return Protocol.T_SAVE_TIME_P;
	}

	@Override
	protected void onEditItemSuccess(ParamsItem paramsItem) {
		
	}

	@Override
	protected void onSaveSuccess() {
		
	}

}
