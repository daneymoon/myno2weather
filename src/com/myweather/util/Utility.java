package com.myweather.util;
import com.myweather.model.City;
import com.myweather.model.Country;
import com.myweather.model.MyWeatherDB;
import com.myweather.model.Province;
import android.text.TextUtils;
import android.util.Log;

public class Utility {
//ʡ������
	public synchronized static boolean handleProvinceResponse(MyWeatherDB myWeaherDB,String response){
		Log.i("zzh", "����handleProvinceResponse in Utility");
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces=response.split(",");//��response�����ָ���Ҹ�ֵ������
			if(allProvinces!=null&&allProvinces.length>0){
				for (String p : allProvinces) {
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setprovinceCode(array[0]);
					province.setprovinceName(array[1]);
					//����������ݴ洢��Province��
					myWeaherDB.saveProvince(province);
				}
				Log.i("zzh", "return true");
				return true;
			}
		}
		return false;
	}
	public static boolean handleCitiesResponse(MyWeatherDB myWeatherDB,String response,int provinceId){
		Log.i("zzh", "����handleCityResponse in Utility");
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for (String c : allCities) {
					String[] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					Log.i("zzh", "ѭ��һ��");
					myWeatherDB.saveCity(city);
				}
				Log.i("zzh", "return true");
				return true;
			}
		}
		return false;
	}
	
	public static boolean handleCountriesResponse(MyWeatherDB myWeatherDB,String response,int cityId){
		Log.i("zzh", "����handleCountryResponse in Utility");
		if(!TextUtils.isEmpty(response)){
			String[] allCountries=response.split(",");
			if(allCountries!=null&&allCountries.length>0){
				for (String c : allCountries) {
					String[] array=c.split("\\|");
					Country country=new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);	
					Log.i("zzh", "ѭ��COUNTRYһ��");
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
}
