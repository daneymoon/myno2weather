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
	//这三是干嘛的？？
public static final int LEVEL_PROVINCE=0;
public static final int LEVEL_CITY=1;
public static final int LEVEL_COUNTRY=0;

private ProgressDialog progressDialog;
private TextView textView;
private ListView listView;
private ArrayAdapter<String> adapter;
private List<String> dataList=new ArrayList<String>();
private MyWeatherDB myWeatherDB;
//省 市 县列表
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
	Log.i("zzh", "初始化完成");
	listView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			Log.i("zzh", "监听器working");
			if(currentLevel==LEVEL_PROVINCE){
				Log.i("zzh", "监听器if1");
				selectedProvince=provinceList.get(position);
				queryCities();
			}else if (currentLevel==LEVEL_CITY) {
				Log.i("zzh", "监听器if2");
				selectedCity=cityList.get(position);
				queryCountries();
			}
		}
	});
	queryProvinces();
	}
		private void queryProvinces() {
			// TODO Auto-generated method stub
			//Log.i("zzh", "调用queryProvince法");
			provinceList=myWeatherDB.loadProvinces();
			if(provinceList.size()>0){
				dataList.clear();
				for(Province province:provinceList){
					dataList.add(province.getprovinceName());
					//Log.i("zzh", "遍历provincelist并添加provincename至datalist");
				}
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				textView.setText("China~~~-中国");
				currentLevel=LEVEL_PROVINCE;
			}else{queryFromServer(null,"province");}
		}

		private void queryCities() {
			// 1.载入数据库给citylist 如果成功 把citylist给datalist，如果不成功调用网络查询
			//Log.i("zzh", "调用queryCity法");
			cityList=myWeatherDB.loadCities(selectedProvince.getId());
			if(cityList.size()>0){
				dataList.clear();
				Log.i("zzh", "datalist清空");
				for (City city : cityList) {
					dataList.add(city.getCityName());
					//Log.i("zzh", "遍历citylist并添加cityname至datalist");
				}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedProvince.getprovinceName());
			currentLevel=LEVEL_CITY;
			}else{queryFromServer(selectedProvince.getprovinceCode(), "city");}
		}
		private void queryCountries() {
			// TODO Auto-generated method stub
			//Log.i("zzh", "调用queryCountry法");
			countryList=myWeatherDB.loadCountries(selectedCity.getId());
			if(countryList.size()>0){
				Log.i("zzh", "countryList****不****为空");
				dataList.clear();
				Log.i("zzh", "datalist清空");
				for (Country country : countryList) {
					dataList.add(country.getCountryName());
					//Log.i("zzh", "遍历countrylist并添加cityname至datalist");
				}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedCity.getCityName());
			currentLevel=LEVEL_COUNTRY;
			}else{
				//Log.i("zzh", "countryList为***空***");
				queryFromServer(selectedCity.getCityCode(), "country");}
		}

		private void queryFromServer(final String code,final String string) {
			//从网上查数据
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
						//Log.i("zzh", "调用provincehandle法");
					}else if("city".equals(string)){
						result=Utility.handleCitiesResponse(myWeatherDB, response, selectedProvince.getId());
						//Log.i("zzh", "调用cityhandle法");
					}else if("country".equals(string)){
						result=Utility.handleCountriesResponse(myWeatherDB, response, selectedCity.getId());
						//Log.i("zzh", "调用handlecountry法");
					}
					if(result) {
						//通过runuithread
						Log.i("zzh", "result为true 判断结束 进入ui线程");
						runOnUiThread(new Runnable() {
							public void run() {
								//Log.i("zzh", "调用关闭对话框");
								closeProgressDialog();
							if("province".equals(string)){
								//Log.i("zzh", "查询省");
								queryProvinces();
							}else if ("city".equals(string)) {
								//Log.i("zzh", "查询市");
								queryCities();
							}else if ("country".equals(string)) {
								//Log.i("zzh", "查询区");
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
				Log.i("zzh", "返回city");
				queryCities();
			}else if(currentLevel==LEVEL_CITY){
				Log.i("zzh", "返回province");
				queryProvinces();
			}
			else if(currentLevel==LEVEL_PROVINCE)  {
				Log.i("zzh", "判断到了吗");
				finish();
				//————————————此elseif不会被执行————————————————————无法finish？ 先写吧 存疑
			}
		}

}

