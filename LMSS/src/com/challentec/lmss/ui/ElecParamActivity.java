package com.challentec.lmss.ui;

import java.util.List;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.ElecParamsParser;
import com.challentec.lmss.util.Protocol;

/**
 * 设置->电机参数
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ElecParamActivity extends ListParamActivity {

	private static final int DATA_LEN = 42;
	private int itemNameIds[] = { R.string.setting_elec_params_rate__power,
			R.string.setting_elec_params_rate__frequency,
			R.string.setting_elec_params_rate_rotate_speed,
			R.string.setting_elec_params_rate_voltage,
			R.string.setting_elec_params_rate_elec,
			R.string.setting_elec_params_double_num,
			R.string.setting_elec_params_run_dir,
			R.string.setting_elec_params_overload_ratio,
			R.string.setting_elec_params_overload_time,

			R.string.setting_elec_params_encoder_type,
			R.string.setting_elec_params_encoder_resolution,
			R.string.setting_elec_params_encoder_dir,
			R.string.setting_elec_params_encoder_test_enabled,
			R.string.setting_elec_params_encoder_test_time

	};

	@Override
	protected CharSequence getTitleText() {

		return getResources().getString(R.string.ui_title_elec_basic);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_params_list_layout;
	}

	@Override
	protected String getUICode() {
		return Protocol.UI_ELEC_P;
	}

	@Override
	protected String getFunctionCommand() {
		return Protocol.S_GET_ELEC_P;
	}

	@Override
	protected List<ParamsItem> getListItems(ResponseData responseData) {
		return ElecParamsParser.getParamsItems(this, responseData.getData(),
				itemNameIds);
	}

	@Override
	protected int getDataLen() {
		return DATA_LEN;
	}

	@Override
	protected String getHexSaveData() {

		return ElecParamsParser.getHexSaveData(paramsItems);
	}

	@Override
	protected String getSaveFuncitonCode() {
		return Protocol.S_SAVE_ELEC_P;
	}

	@Override
	protected void onEditItemSuccess(ParamsItem paramsItem) {
		
	}

	@Override
	protected void onSaveSuccess() {
		
	}

}
