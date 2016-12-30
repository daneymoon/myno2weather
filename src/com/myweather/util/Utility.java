package com.myweather.util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.myweather.model.City;
import com.myweather.model.Country;
import com.myweather.model.MyWeatherDB;
import com.myweather.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Utility {
//省级数据
	public synchronized static boolean handleProvinceResponse(MyWeatherDB myWeaherDB,String response){
		Log.i("zzh", "调用handleProvinceResponse in Utility");
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces=response.split(",");//将response按，分割，并且赋值给数组
			if(allProvinces!=null&&allProvinces.length>0){
				for (String p : allProvinces) {
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setprovinceCode(array[0]);
					province.setprovinceName(array[1]);
					//解析后的数据存储至Province表
					myWeaherDB.saveProvince(province);
				}
				Log.i("zzh", "return true");
				return true;
			}
		}
		return false;
	}
	public static boolean handleCitiesResponse(MyWeatherDB myWeatherDB,String response,int provinceId){
		Log.i("zzh", "调用handleCityResponse in Utility");
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for (String c : allCities) {
					String[] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					Log.i("zzh", "循环一次");
					myWeatherDB.saveCity(city);
				}
				Log.i("zzh", "return true");
				return true;
			}
		}
		return false;
	}
	
	public static boolean handleCountriesResponse(MyWeatherDB myWeatherDB,String response,int cityId){
		Log.i("zzh", "调用handleCountryResponse in Utility");
		if(!TextUtils.isEmpty(response)){
			String[] allCountries=response.split(",");
			if(allCountries!=null&&allCountries.length>0){
				for (String c : allCountries) {
					String[] array=c.split("\\|");
					Country country=new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);	
					//Log.i("zzh", "循环COUNTRY一次");
					myWeatherDB.saveCountry(country);
				}
				Log.i("zzh", "return true");
				for (String string : allCountries) {
					Log.i("zzh", string);
				}
				return true;
			}
		}
		return false;
	}
	//用于解析服务器返回的Json数据，并存储至本地
	public static void handleWeatherResponse(Context context,String response){
	
		try {
			JSONObject jsonObject =new JSONObject();
			JSONObject weatherInfo=jsonObject.getJSONObject("response");
			String cityName=weatherInfo.getString("cityName");
			String weatherCode=weatherInfo.getString("cityid");
			String temp1=weatherInfo.getString("temp1");
			String temp2=weatherInfo.getString("temp2");
			String weatherDesp=weatherInfo.getString("weather");
			String publishTime=weatherInfo.getString("ptime");
			saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
		// TODO Auto-generated method stub
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", simpleDateFormat.format(new Date()));
		editor.commit();
		
	}
}
