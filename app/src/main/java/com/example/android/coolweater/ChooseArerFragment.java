package com.example.android.coolweater;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.coolweater.db.City;
import com.example.android.coolweater.db.City_Table;
import com.example.android.coolweater.db.County;
import com.example.android.coolweater.db.County_Table;
import com.example.android.coolweater.db.Province;
import com.example.android.coolweater.utils.HttpUtils;
import com.example.android.coolweater.utils.Utility;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.bouncycastle.crypto.agreement.srp.SRP6Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseArerFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    //查找城市信息
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    //查询县级信息
                    queryCounties();
                }else if(currentLevel == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
//                    Log.d("ChooseArerFragment","weatherId : " + weatherId);
                    if(getActivity() instanceof MainActivity){
                        Intent intent = new Intent(getActivity(),WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof WeatherActivity){
//                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
//                        editor.remove("")
//                        editor.putString("we","");
//                        editor.apply();
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.getDrawerLayout().closeDrawers();
                        activity.getSwipeRefresh().setRefreshing(true);
                        activity.requsetWeather(weatherId);
                    }

                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_COUNTY){
                    //查找城市信息
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    //查询省级信息
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = SQLite.select()
                .from(County.class)
                .where(County_Table.city_id.eq(selectedCity.getId()))
                .queryList();
        if(countyList.size()>0){
            dataList.clear();
            for (County county: countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String adress = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(adress,"county");
        }
    }

    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = SQLite.select()
                .from(City.class)
                .where(City_Table.province_id.eq(selectedProvince.getId()))
                .queryList();
//        Log.d("ChooseArerFragment","cityList.size() = " + cityList.size());
        if(cityList.size()>0){
            dataList.clear();
            for (City city :cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String adress = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(adress,"city");
        }
    }

    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = SQLite.select()
                .from(Province.class)
                .queryList();
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province :provinceList){
                dataList.add(province.getProvinceName());
            }
           adapter.notifyDataSetChanged();
           listView.setSelection(0);
           currentLevel = LEVEL_PROVINCE;
        }else {
            String adress = "http://guolin.tech/api/china";
            queryFromServer(adress,"province");
        }
    }

    private void queryFromServer(String adress,final String type){
        showProgressDialog();
        HttpUtils.sentHttpRequest(adress, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"获取数据失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
//                Log.d("ChooseArerFragment","responseText = " + responseText);
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                assert !result : result;
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog != null)
            progressDialog.dismiss();
    }
}
