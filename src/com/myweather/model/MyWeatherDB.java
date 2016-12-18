package com.myweather.model;
//用于封装数据库操作

import java.util.ArrayList;
import java.util.List;

import com.myweather.db.MyWeatherOpenHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyWeatherDB {
	
//名字
	
public static final String DB_NAME="cool_weather";

//版本
private static final int VERSION=1;
private static MyWeatherDB myWeatherDB;
private SQLiteDatabase db;

//构造方法私有化 将openhelper帮助创建的数据库 给到db
private MyWeatherDB(Context context){
	MyWeatherOpenHelper dbHelper=new MyWeatherOpenHelper(context, DB_NAME, null, VERSION);
	db=dbHelper.getWritableDatabase();
}
//获取myweatherdb实例  是单例类 单例就是为了防止反复去New出myWeathedDB类型的对象
public synchronized static MyWeatherDB getInstance(Context context){
	if(myWeatherDB==null){
		myWeatherDB=new MyWeatherDB(context);
	}
	return myWeatherDB;
}


//将province实例存储至数据库
public void saveProvince (Province province){
	if(province != null){
		/*
		 * ContentValues 和HashTable类似都是一种存储的机制 但是两者最大的区别就在于，contenvalues只能存储基本类型的数据，
		 * 像string，int之类的，不能存储对象这种东西，而HashTable却可以存储对象。在忘数据库中插入数据的时候，首先应该有一个Co
		 * ntentValues的对象所以：
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
	//从数据库读取***************************************省份信息 
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
	
	//将city实例存储至数据库
	public void saveCity(City city){
		if(city!=null){
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("city", null, values);
		}
	}
	//从数据库读取***************************************城市信息 
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
	//实例化country
	public void saveCountry(Country country){
		if(country!=null){
		ContentValues values=new ContentValues();
		values.put("country_name", country.getCountryName());
		values.put("country_code", country.getCountryCode());
		values.put("city_id", country.getCityId());
		db.insert("Country", null, values);
		}
	}
	//数据库读取country信息
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
