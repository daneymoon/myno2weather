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
	//��ʾ������
	private TextView publishText;
	//��ʾ����ʱ��
	private TextView weatherDespText;
	//��ʾ����������Ϣ
	private TextView temp1Text;
	private TextView temp2Text;
	//��ʾ�������¶�
	private TextView currentDateText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_display);
	}
}
