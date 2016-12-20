package com.myweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.myweather.app.R;
import com.myweather.model.City;
import com.myweather.model.Country;
import com.myweather.model.MyWeatherDB;
import com.myweather.model.Province;
import com.myweather.util.HttpCallbackListener;
import com.myweather.util.HttpUtil;
import com.myweather.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	//�����Ǹ���ģ���
public static final int LEVEL_PROVINCE=0;
public static final int LEVEL_CITY=1;
public static final int LEVEL_COUNTRY=0;

private ProgressDialog progressDialog;
private TextView textView;
private ListView listView;
private ArrayAdapter<String> adapter;
private List<String> dataList=new ArrayList<String>();
private MyWeatherDB myWeatherDB;
//ʡ �� ���б�
private List<Province> provinceList;
private List<City> cityList;
private List<Country> countryList;

private Province selectedProvince;
private City selectedCity;
private int currentLevel;

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.choose_area);
	listView=(ListView) findViewById(R.id.list_view);
	textView=(TextView) findViewById(R.id.title_text);
	adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
	listView.setAdapter(adapter);
	myWeatherDB=MyWeatherDB.getInstance(this);
	Log.i("zzh", "��ʼ�����");
	listView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			Log.i("zzh", "������working");
			if(currentLevel==LEVEL_PROVINCE){
				Log.i("zzh", "������if1");
				selectedProvince=provinceList.get(position);
				queryCities();
			}else if (currentLevel==LEVEL_CITY) {
				Log.i("zzh", "������if2");
				selectedCity=cityList.get(position);
				queryCountries();
			}
		}
	});
	queryProvinces();
	}
		private void queryProvinces() {
			// TODO Auto-generated method stub
			//Log.i("zzh", "����queryProvince��");
			provinceList=myWeatherDB.loadProvinces();
			if(provinceList.size()>0){
				dataList.clear();
				for(Province province:provinceList){
					dataList.add(province.getprovinceName());
					//Log.i("zzh", "����provincelist�����provincename��datalist");
				}
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				textView.setText("China~~~-�й�");
				currentLevel=LEVEL_PROVINCE;
			}else{queryFromServer(null,"province");}
		}

		private void queryCities() {
			// 1.�������ݿ��citylist ����ɹ� ��citylist��datalist��������ɹ����������ѯ
			//Log.i("zzh", "����queryCity��");
			cityList=myWeatherDB.loadCities(selectedProvince.getId());
			if(cityList.size()>0){
				dataList.clear();
				Log.i("zzh", "datalist���");
				for (City city : cityList) {
					dataList.add(city.getCityName());
					//Log.i("zzh", "����citylist�����cityname��datalist");
				}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedProvince.getprovinceName());
			currentLevel=LEVEL_CITY;
			}else{queryFromServer(selectedProvince.getprovinceCode(), "city");}
		}
		private void queryCountries() {
			// TODO Auto-generated method stub
			//Log.i("zzh", "����queryCountry��");
			countryList=myWeatherDB.loadCountries(selectedCity.getId());
			if(countryList.size()>0){
				Log.i("zzh", "countryList****��****Ϊ��");
				dataList.clear();
				Log.i("zzh", "datalist���");
				for (Country country : countryList) {
					dataList.add(country.getCountryName());
					//Log.i("zzh", "����countrylist�����cityname��datalist");
				}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedCity.getCityName());
			currentLevel=LEVEL_COUNTRY;
			}else{
				//Log.i("zzh", "countryListΪ***��***");
				queryFromServer(selectedCity.getCityCode(), "country");}
		}

		private void queryFromServer(final String code,final String string) {
			//�����ϲ�����
			//Log.i("zzh", "queryFromServer");
			//Log.i("zzh", code);
			//Log.i("zzh", string);
			String address;
			if(!TextUtils.isEmpty(code)){
				address="http://www.weather.com.cn/data/list3/city"+code+".xml";
			}else{
				address="http://www.weather.com.cn/data/list3/city.xml";
			}
			showProgressDialog();
			HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
				@Override
				public void onFinish(String response) {
					// TODO Auto-generated method stub
					boolean result=false;
					if("province".equals(string)){
						result=Utility.handleProvinceResponse(myWeatherDB, response);
						//Log.i("zzh", "����provincehandle��");
					}else if("city".equals(string)){
						result=Utility.handleCitiesResponse(myWeatherDB, response, selectedProvince.getId());
						//Log.i("zzh", "����cityhandle��");
					}else if("country".equals(string)){
						result=Utility.handleCountriesResponse(myWeatherDB, response, selectedCity.getId());
						//Log.i("zzh", "����handlecountry��");
					}
					if(result) {
						//ͨ��runuithread
						Log.i("zzh", "resultΪtrue �жϽ��� ����ui�߳�");
						runOnUiThread(new Runnable() {
							public void run() {
								//Log.i("zzh", "���ùرնԻ���");
								closeProgressDialog();
							if("province".equals(string)){
								//Log.i("zzh", "��ѯʡ");
								queryProvinces();
							}else if ("city".equals(string)) {
								//Log.i("zzh", "��ѯ��");
								queryCities();
							}else if ("country".equals(string)) {
								//Log.i("zzh", "��ѯ��");
								queryCountries();
							}
							}
						});
					}
				}
				
				@Override
				public void onError(Exception e) {
					// TODO Auto-generated method stub
					runOnUiThread(new Runnable() {
						public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "failure in loading", Toast .LENGTH_SHORT).show();
						}
					});
				}
			});
			
		}

		private void showProgressDialog() {
			// TODO Auto-generated method stub
			if(progressDialog==null){
				progressDialog=new ProgressDialog(ChooseAreaActivity.this);
				progressDialog.setMessage("loading..Please wait a moment");
				progressDialog.setCanceledOnTouchOutside(false);
			}
			progressDialog.show();
			
		}
		private void closeProgressDialog() {
			// TODO Auto-generated method stub
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
		}
		public void onBackPressed(){
			if(currentLevel==LEVEL_COUNTRY){
				Log.i("zzh", "����city");
				queryCities();
			}else if(currentLevel==LEVEL_CITY){
				Log.i("zzh", "����province");
				queryProvinces();
			}
			else if(currentLevel==LEVEL_PROVINCE)  {
				Log.i("zzh", "�жϵ�����");
				finish();
				//��������������������������elseif���ᱻִ�С����������������������������������������޷�finish�� ��д�� ����
			}
		}

}

