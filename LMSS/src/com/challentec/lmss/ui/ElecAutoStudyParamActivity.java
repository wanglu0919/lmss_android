package com.challentec.lmss.ui;

import java.util.List;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.ElecAutoStudyParamsParser;
import com.challentec.lmss.util.Protocol;

/**
 * 设置->自学习->电机自学习参数
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class ElecAutoStudyParamActivity extends ListParamActivity {

	private int itemNameIds[] = {
			R.string.auto_study_elec_param_asyn_empty_load_elec,
			R.string.auto_study_elec_param_asyn_stator_resistance,
			R.string.auto_study_elec_param_asyn_rotor_resistance,
			R.string.auto_study_elec_param_asyn_leakage_oppose,
			R.string.auto_study_elec_param_asyn_mutual_oppose,
			R.string.auto_study_elec_param_syn_start_location,
			R.string.auto_study_elec_param_syn_cut_elec_location,
			R.string.auto_study_elec_param_syn_q_elec,
			R.string.auto_study_elec_param_syn_d_elec,
			R.string.auto_study_elec_param_auto_study_trouble_decode,
			R.string.auto_study_elec_param_alternate_elec_resistance, };

	@Override
	protected CharSequence getTitleText() {

		return getResources()
				.getString(R.string.ui_title_elec_auto_study_param);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_params_list_layout;
	}

	@Override
	protected String getUICode() {
		return Protocol.UI_ELE_AUTO_STUDY_P;
	}

	@Override
	protected String getFunctionCommand() {

		return Protocol.S_ELEC_STUDY_GET_P;
	}

	@Override
	protected List<ParamsItem> getListItems(ResponseData responseData) {
		return ElecAutoStudyParamsParser.getParamsItems(this,
				responseData.getData(), itemNameIds);
	}

	@Override
	protected int getDataLen() {
		return 44;
	}

	@Override
	protected String getHexSaveData() {
		return ElecAutoStudyParamsParser.getSaveHexData(paramsItems);
	}

	@Override
	protected String getSaveFuncitonCode() {
		return Protocol.S_SAVE_ELEC_STUDY_P;
	}

	@Override
	protected void onEditItemSuccess(ParamsItem paramsItem) {
		
	}

	@Override
	protected void onSaveSuccess() {
		
	}

}
