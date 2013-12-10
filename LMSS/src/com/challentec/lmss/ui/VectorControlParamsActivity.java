package com.challentec.lmss.ui;

import java.util.List;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.VecotorControlParamsParser;
import com.challentec.lmss.util.Protocol;

/**
 * 矢量控制参数
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class VectorControlParamsActivity extends ListParamActivity {

	private int itemNameIds[] = {
			R.string.test_vector_controlparams_speed_add_one,
			R.string.test_vector_controlparams_speed_score_one,
			R.string.test_vector_controlparams_speed_full_uplimt_one,
			R.string.test_vector_controlparams_switch_frequency_one,
			R.string.test_vector_controlparams_speed_add_two,
			R.string.test_vector_controlparams_speed_score_two,
			R.string.test_vector_controlparams_speed_full_uplimt_two,
			R.string.test_vector_controlparams_elec_add,
			R.string.test_vector_controlparams_elec_score,
			R.string.test_vector_controlparams_elec_full_uplimt,
			R.string.test_vector_controlparams_torque_full_uplimt,
			R.string.test_vector_controlparams_lsf_elec_coefficient,
			R.string.test_vector_controlparams_lsf_speed_kp,
			R.string.test_vector_controlparams_lsf_speed_ki,
			R.string.test_vector_controlparams_lj_add_speed_time,
			R.string.test_vector_controlparams_lj_slow_speed_time,
			R.string.test_vector_controlparams_elec_contants

	};

	private static int DATA_LEN = 64;

	@Override
	protected CharSequence getTitleText() {
		return getResources().getString(
				R.string.ui_title_device_test_vector_control_params);
	}

	@Override
	protected int getMainViewLayoutId() {
		return R.layout.activity_params_list_layout;
	}

	@Override
	protected String getUICode() {
		return Protocol.UI_VETOR_CONTROL_P;
	}

	@Override
	protected String getFunctionCommand() {
		return Protocol.T_GET_VECTOR_P;
	}

	@Override
	protected List<ParamsItem> getListItems(ResponseData responseData) {
		return VecotorControlParamsParser.getParamsItems(this,
				responseData.getData(), itemNameIds);
	}

	@Override
	protected int getDataLen() {
		return DATA_LEN;
	}

	@Override
	protected String getHexSaveData() {

		return VecotorControlParamsParser.getHexSaveData(paramsItems);
	}

	@Override
	protected String getSaveFuncitonCode() {

		return Protocol.T_SAVE_VECTOR_P;
	}

	@Override
	protected void onEditItemSuccess(ParamsItem paramsItem) {
		
	}

	@Override
	protected void onSaveSuccess() {
		
	}

}
