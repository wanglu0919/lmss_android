package com.challentec.lmss.ui;

import java.util.List;

import android.view.View;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.FloorDisplayParser;
import com.challentec.lmss.util.Protocol;

/**
 * 设置->楼层显示设置
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class FloorDisplayActivity extends ListParamActivity {

	private int floorNum = 0;

	@Override
	protected void initMainView(View mainView) {

		super.initMainView(mainView);
		floorNum = AppConfig.getAppConfig(appContext).getSharedPreferences()
				.getInt(AppConfig.FLOOR_NUM_KEY, 0);
		setCheckLen(false);// 不检查数据长度
	}

	@Override
	protected CharSequence getTitleText() {

		return getResources()
				.getString(R.string.ui_title_floor_display_setting);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_params_list_layout;
	}

	@Override
	protected String getUICode() {
		return Protocol.UI_FLOOR_DISPLAY_SET;
	}

	@Override
	protected String getFunctionCommand() {
		return Protocol.S_GET_FLOOR_DISPALY;
	}

	@Override
	protected List<ParamsItem> getListItems(ResponseData responseData) {

		String rd = responseData.getData();

		if (rd.length() >= floorNum * 2) {// 返回数据长度大于长度
			rd = rd.substring(0, floorNum * 2);
		}

		return FloorDisplayParser.getParamsItems(this, rd);
	}

	@Override
	protected int getDataLen() {
		return floorNum * 2;
	}

	@Override
	protected String getHexSaveData() {
		return FloorDisplayParser.getHexSaveData(paramsItems);
	}

	@Override
	protected String getSaveFuncitonCode() {

		return Protocol.S_SAVE_FLOOR_DISPALY;
	}

	@Override
	protected void onEditItemSuccess(ParamsItem paramsItem) {
		
	}

	@Override
	protected void onSaveSuccess() {
		
	}

}
