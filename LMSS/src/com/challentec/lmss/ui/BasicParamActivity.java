package com.challentec.lmss.ui;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.challentec.lmss.app.AppConfig;
import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.bean.ResponseData;
import com.challentec.lmss.engine.BasicParamsParser;
import com.challentec.lmss.util.Protocol;

/**
 * 设置 ->基本参数activity
 * 
 * @author 泰得利通 wanglu
 * 
 */
public class BasicParamActivity extends ListParamActivity {

	private static final int DATA_LEN = 58;
	private int itemNameIds[] = new int[] {
			R.string.setting_basicparams_tv_system_time,
			R.string.setting_basicparams_tv_total_floor,
			R.string.setting_basicparams_tv_under_floor,
			R.string.setting_basicparams_tv_floor_fixed_speed,
			R.string.setting_basicparams_tv_floor_run_speed,

			R.string.setting_basicparams_tv_drag_wheel_dia,
			R.string.setting_basicparams_tv_drag_rope_scale,
			R.string.setting_basicparams_tv_speed_cut_scale,
			R.string.setting_basicparams_tv_hight_speed_count_direction,
			R.string.setting_basicparams_tv_elec_model,
			R.string.setting_basicparams_tv_signal_carrier_rate,
			R.string.setting_basicparams_tv_drive_control_pattern,
			R.string.setting_basicparams_tv_fire_protection_leave_floor,
			R.string.setting_basicparams_tv_parking_floor,

			R.string.setting_basicparams_tv_back_station_floor,
			R.string.setting_basicparams_tv_ard_choice,
			R.string.setting_basicparams_tv_ard_speed,
			R.string.setting_basicparams_tv_control_type,
			R.string.setting_basicparams_tv_open_door_front

	};

	@Override
	protected CharSequence getTitleText() {

		return getResources().getString(R.string.ui_title_setting_basic);
	}

	@Override
	protected int getMainViewLayoutId() {

		return R.layout.activity_params_list_layout;
	}

	@Override
	protected String getUICode() {

		return Protocol.UI_BASIC_P;
	}

	@Override
	protected String getFunctionCommand() {
		return Protocol.S_GET_DEVICE_BASE_P;
	}

	@Override
	protected List<ParamsItem> getListItems(ResponseData responseData) {
		return BasicParamsParser.getParamsItems(this, responseData.getData(),
				itemNameIds);

	}

	@Override
	protected int getDataLen() {
		return DATA_LEN;
	}

	@Override
	protected String getHexSaveData() {

		return BasicParamsParser.getSaveHexData(paramsItems);
	}

	@Override
	protected String getSaveFuncitonCode() {

		return Protocol.S_SAVE_DEVICE_BASE_P;
	}

	@Override
	protected void onEditItemSuccess(ParamsItem paramsItem) {

		if (paramsItem.getOrder() == 1) {// 总楼层修改了
			int totalFloorTemp = Integer.parseInt(paramsItem.getItemValue());
			paramsItems.get(12).setLimit(1, totalFloorTemp);// 更新// 消防车撤离曾 驻留层
															// 返回机站层限制范围
			paramsItems.get(13).setLimit(1, totalFloorTemp);
			paramsItems.get(14).setLimit(1, totalFloorTemp);
		}

	}

	@Override
	protected void onSaveSuccess() {
		int totalFloor = Integer.parseInt(paramsItems.get(1).getItemValue());// 保存后的总楼层
		SharedPreferences sp = AppConfig.getAppConfig(appContext)
				.getSharedPreferences();
		Editor ed = sp.edit();
		ed.putInt(AppConfig.FLOOR_NUM_KEY, totalFloor);// 保存修改提交后的楼层
		ed.commit();

		Intent intent = new Intent();// 通知楼层数量更新
		intent.setAction(HomeActivity.ACTION_UPDATE_FLOOR_NUMBER);

		sendBroadcast(intent);// 通知广播
	}

}
