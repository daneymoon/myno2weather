package com.myweather.activity;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherAcitivity extends Activity {
	private LinearLayout weatherInfoLayout;

	private TextView cityNameText;
	//显示城市名
	private TextView publishText;
	//显示发布时间
	private TextView weatherDespText;
	//显示天气描述信息
	private TextView temp1Text;
	private TextView temp2Text;
	//显示最高最低温度
	private TextView currentDateText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_display);
	}
}
