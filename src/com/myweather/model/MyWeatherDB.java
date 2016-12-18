package com.myweather.model;
//���ڷ�װ���ݿ����

import java.util.ArrayList;
import java.util.List;

import com.myweather.db.MyWeatherOpenHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyWeatherDB {
	
//����
	
public static final String DB_NAME="cool_weather";

//�汾
private static final int VERSION=1;
private static MyWeatherDB myWeatherDB;
private SQLiteDatabase db;

//���췽��˽�л� ��openhelper�������������ݿ� ����db
private MyWeatherDB(Context context){
	MyWeatherOpenHelper dbHelper=new MyWeatherOpenHelper(context, DB_NAME, null, VERSION);
	db=dbHelper.getWritableDatabase();
}
//��ȡmyweatherdbʵ��  �ǵ����� ��������Ϊ�˷�ֹ����ȥNew��myWeathedDB���͵Ķ���
public synchronized static MyWeatherDB getInstance(Context context){
	if(myWeatherDB==null){
		myWeatherDB=new MyWeatherDB(context);
	}
	return myWeatherDB;
}


//��provinceʵ���洢�����ݿ�
public void saveProvince (Province province){
	if(province != null){
		/*
		 * ContentValues ��HashTable���ƶ���һ�ִ洢�Ļ��� ��������������������ڣ�contenvaluesֻ�ܴ洢�������͵����ݣ�
		 * ��string��int֮��ģ����ܴ洢�������ֶ�������HashTableȴ���Դ洢�����������ݿ��в������ݵ�ʱ������Ӧ����һ��Co
		 * ntentValues�Ķ������ԣ�
		 * ContentValues initialValues = new ContentValues();
			initialValues.put(key,values);
			SQLiteDataBase sdb ;
			sdb.insert(database_name,null,initialValues);
		 */
		ContentValues values= new ContentValues();
		values.put("province_name",province.getprovinceName());
		values.put("province_code", province.getprovinceCode());
		db.insert("Province", null, values);
	}
}
	//�����ݿ��ȡ***************************************ʡ����Ϣ 
	public List<Province> loadProvinces(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor =db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
			Province province=new Province();
			province.setId(cursor.getInt(cursor.getColumnIndex("id")));
			province.setprovinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			province.setprovinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;		
	}
	
	//��cityʵ���洢�����ݿ�
	public void saveCity(City city){
		if(city!=null){
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("city", null, values);
		}
	}
	//�����ݿ��ȡ***************************************������Ϣ 
	public List<City> loadCities(int provinceId){
		List<City> list=new ArrayList<>();
		Cursor cursor=db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
			City city=new City();
			city.setId(cursor.getInt(cursor.getColumnIndex("id")));
			city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
			city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
			city.setProvinceId(provinceId);
			list.add(city);
			}while(cursor.moveToNext());
			if(cursor!=null){
				cursor.close();
			}
		}
		return list;
	}
	//ʵ����country
	public void saveCountry(Country country){
		if(country!=null){
		ContentValues values=new ContentValues();
		values.put("country_name", country.getCountryName());
		values.put("country_code", country.getCountryCode());
		values.put("city_id", country.getCityId());
		db.insert("Country", null, values);
		}
	}
	//���ݿ��ȡcountry��Ϣ
	public List<Country> loadCountries(int cityId){
		List<Country> list=new ArrayList<>();
		Cursor cursor=db.query("Country", null, "city_id=?",new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Country country=new Country();
				country.setId(cursor.getInt(cursor.getColumnIndex("country_id")));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCityId(cityId);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
}
