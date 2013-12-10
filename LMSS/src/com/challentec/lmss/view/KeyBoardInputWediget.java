package com.challentec.lmss.view;

import com.challentec.lmss.app.R;
import com.challentec.lmss.bean.ParamsItem;
import com.challentec.lmss.listener.KeyBoardPressListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class KeyBoardInputWediget extends LinearLayout implements
		OnClickListener {

	/* 键盘码定义 */
	public static final int KEY_CODE_ZERON = 0x00;
	public static final int KEY_CODE_ONE = 0x01;
	public static final int KEY_CODE_TWO = 0x02;
	public static final int KEY_CODE_THREE = 0x03;
	public static final int KEY_CODE_FOUR = 0x04;
	public static final int KEY_CODE_FIVE = 0x05;
	public static final int KEY_CODE_SIX = 0x06;
	public static final int KEY_CODE_SEVEN = 0x07;
	public static final int KEY_CODE_EIGHT = 0x08;
	public static final int KEY_CODE_NINE = 0x09;

	public static final int KEY_CODE_A = 0x0a;
	public static final int KEY_CODE_B = 0x0b;
	public static final int KEY_CODE_C = 0x0c;
	public static final int KEY_CODE_D = 0x0d;
	public static final int KEY_CODE_E = 0x0e;
	public static final int KEY_CODE_F = 0x0f;

	public static final int KEY_CODE_DOT = 0x11;
	public static final int KEY_CODE_CANCLE = 0x12;
	public static final int KEY_CODE_OK = 0x13;
	private KeyBoardPressListener keyBoardPressListener;

	private int dataType=ParamsItem.VALUE_INT;// 数据类型

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	private Button key_a, key_b, key_c, key_d, key_e, key_f, key_zero, key_one,
			key_two, key_three, key_four, key_five, key_six, key_seven,
			key_eight, key_nine, key_ok, key_dot, key_clear;

	public void setKeyBoardPressListener(
			KeyBoardPressListener keyBoardPressListener) {
		this.keyBoardPressListener = keyBoardPressListener;
	}

	public KeyBoardInputWediget(Context context) {
		super(context);
	}

	public KeyBoardInputWediget(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		inflater.inflate(R.layout.pop_key_input_layout, this);
		findKeyButtons();

	}

	/**
	 * 查找初始化键盘按钮
	 * 
	 * @author 泰得利通 wanglu
	 */
	private void findKeyButtons() {

		key_a = (Button) findViewById(R.id.key_a);
		key_b = (Button) findViewById(R.id.key_b);
		key_c = (Button) findViewById(R.id.key_c);
		key_d = (Button) findViewById(R.id.key_d);
		key_e = (Button) findViewById(R.id.key_e);
		key_f = (Button) findViewById(R.id.key_f);
		key_zero = (Button) findViewById(R.id.key_zero);
		key_one = (Button) findViewById(R.id.key_one);
		key_two = (Button) findViewById(R.id.key_two);
		key_three = (Button) findViewById(R.id.key_three);
		key_four = (Button) findViewById(R.id.key_four);
		key_five = (Button) findViewById(R.id.key_five);
		key_six = (Button) findViewById(R.id.key_six);
		key_seven = (Button) findViewById(R.id.key_seven);
		key_eight = (Button) findViewById(R.id.key_eight);
		key_nine = (Button) findViewById(R.id.key_nine);
		key_dot = (Button) findViewById(R.id.key_dot);
		key_clear = (Button) findViewById(R.id.key_clear);
		key_ok = (Button) findViewById(R.id.key_ok);

		key_a.setOnClickListener(this);
		key_b.setOnClickListener(this);
		key_c.setOnClickListener(this);
		key_d.setOnClickListener(this);
		key_e.setOnClickListener(this);
		key_f.setOnClickListener(this);
		key_zero.setOnClickListener(this);
		key_one.setOnClickListener(this);
		key_two.setOnClickListener(this);
		key_three.setOnClickListener(this);
		key_four.setOnClickListener(this);
		key_five.setOnClickListener(this);
		key_six.setOnClickListener(this);
		key_seven.setOnClickListener(this);
		key_eight.setOnClickListener(this);
		key_nine.setOnClickListener(this);
		key_dot.setOnClickListener(this);
		key_clear.setOnClickListener(this);
		key_ok.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		int keyCode = -1;

		switch (view.getId()) {

		case R.id.key_a:
			if (dataType == ParamsItem.VALUE_HEX) {
				keyCode = KEY_CODE_A;
			}

			break;
		case R.id.key_b:
			if (dataType == ParamsItem.VALUE_HEX) {
				keyCode = KEY_CODE_B;
			}
			break;
		case R.id.key_c:
			if (dataType == ParamsItem.VALUE_HEX) {
				keyCode = KEY_CODE_C;
			}
			break;
		case R.id.key_d:
			if (dataType == ParamsItem.VALUE_HEX) {
				keyCode = KEY_CODE_D;
			}
			break;
		case R.id.key_e:
			if (dataType == ParamsItem.VALUE_HEX) {
				keyCode = KEY_CODE_E;
			}
			break;
		case R.id.key_f:
			if (dataType == ParamsItem.VALUE_HEX) {
				keyCode = KEY_CODE_F;
			}
			break;
		case R.id.key_zero:

			keyCode = KEY_CODE_ZERON;
			break;
		case R.id.key_one:
			keyCode = KEY_CODE_ONE;
			break;
		case R.id.key_two:
			keyCode = KEY_CODE_TWO;
			break;
		case R.id.key_three:
			keyCode = KEY_CODE_THREE;
			break;
		case R.id.key_four:
			keyCode = KEY_CODE_FOUR;
			break;
		case R.id.key_five:
			keyCode = KEY_CODE_FIVE;
			break;
		case R.id.key_six:
			keyCode = KEY_CODE_SIX;
			break;
		case R.id.key_seven:
			keyCode = KEY_CODE_SEVEN;
			break;
		case R.id.key_eight:
			keyCode = KEY_CODE_EIGHT;
			break;
		case R.id.key_nine:
			keyCode = KEY_CODE_NINE;
			break;
		case R.id.key_clear:
			keyCode = KEY_CODE_CANCLE;
			break;
		case R.id.key_dot:
			if (dataType == ParamsItem.VALUE_FLOAT) {// 浮点型数据才可以使用.
				keyCode = KEY_CODE_DOT;
			}
			break;
		case R.id.key_ok:
			keyCode = KEY_CODE_OK;
			break;

		}

		if (this.keyBoardPressListener != null && keyCode != -1) {
			keyBoardPressListener.onPress(keyCode);
		}

	}

}
