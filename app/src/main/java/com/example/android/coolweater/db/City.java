package com.example.android.coolweater.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.List;

@Table(database = AppDatabase.class)
public class City extends BaseModel implements Serializable {
    @PrimaryKey
    private int id;
    @Column
    private String cityName;
    @Column
    private int cityCode;
    @ForeignKey(stubbedRelationship = true)
    private Province province;

    private List<County> counties;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public List<County> getCounties() {
        if(counties == null || counties.isEmpty()){
            counties = SQLite.select()
                    .from(County.class)
                    .where(County_Table.city_id.eq(id))
                    .queryList();
        }
        return counties;
    }

    public void setCounties(List<County> counties) {
        this.counties = counties;
    }
}
