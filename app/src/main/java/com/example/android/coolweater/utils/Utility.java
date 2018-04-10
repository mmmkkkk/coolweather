package com.example.android.coolweater.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.coolweater.db.City;
import com.example.android.coolweater.db.County;
import com.example.android.coolweater.db.Province;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.List;

public class Utility {
    private static Gson gson = new Gson();
    /*
    * 解析和处理服务器返回的省级数据
    * */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            List<Province> provinces = gson.fromJson(response,new TypeToken<List<Province>>(){}.getType());
            for(Province province : provinces){
                province.setProvinceCode(province.getId());
                province.save();
            }
            return true;
        }
        return false;
    }

    /*
    * 解析和处理服务器返回的市级数据
    * */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            List<City> cities = gson.fromJson(response,new TypeToken<List<City>>(){}.getType());
            for (City city : cities){
                city.setCityCode(city.getId());
                Province province = new Province();
                province.setId(provinceId);
                city.setProvince(province);
                city.save();
            }
            return true;
        }
        return false;
    }

    /*
    * 解析和处理服务器返回的县级数据
    * */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            List<County> counties = gson.fromJson(response,new TypeToken<List<County>>(){}.getType());
            for (County county : counties){
                City city = new City();
                city.setId(cityId);
                county.setCity(city);
                county.save();
            }
            return true;
        }
        return false;
    }
}
