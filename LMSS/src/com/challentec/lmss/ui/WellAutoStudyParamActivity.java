package com.challentec.lmss.ui;

import java.util.List;

import android.view.View;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.WellAutoStudyParamsParser;
import com.challentec.lmss.util.Protocol;

/**
 * 设置->自学习->电机自学习参数
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class WellAutoStudyParamActivity extends ListParamActivity {

	private int floorNum = 0;

	@Override
	protected void initMainView(View mainView) {
		super.initMainView(mainView);
		floorNum = AppConfig.getAppConfig(appContext).getSharedPreferences()
				.getInt(AppConfig.FLOOR_NUM_KEY, 0);
		setCheckLen(false);//不检查数据长度
	}

	@Override
	protected CharSequence getTitleText() {

		return getResources()
				.getString(R.string.ui_title_well_auto_study_param);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_params_list_layout;
	}

	@Override
	protected String getUICode() {
		return Protocol.UI_WELL_AUTO_STUDY_P;
	}

	@Override
	protected String getFunctionCommand() {

		return Protocol.S_WELL_STUDY_GET_P;
	}

	@Override
	protected List<ParamsItem> getListItems(ResponseData responseData) {
		
		String rd=responseData.getData();
		int len=getDataLen();
		
		if(rd.length()>=len){
			rd=rd.substring(0, len);
		}
		
		return WellAutoStudyParamsParser.getParamsItems(this,
				rd);
	}

	@Override
	protected int getDataLen() {
		return (15 + floorNum * 12) * 2;
	}

	@Override
	protected String getHexSaveData() {

		return WellAutoStudyParamsParser.getHexSaveData(paramsItems);
	}

	@Override
	protected String getSaveFuncitonCode() {

		return Protocol.S_SAVE_WELL_STUDY_P;
	}

	@Override
	protected void onEditItemSuccess(ParamsItem paramsItem) {
		
	}

	@Override
	protected void onSaveSuccess() {
		
	}

}
