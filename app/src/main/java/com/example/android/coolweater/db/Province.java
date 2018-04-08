package com.example.android.coolweater.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.List;

@Table(database = AppDatabase.class)
public class Province extends BaseModel implements Serializable{
    @PrimaryKey
    private int id;
    @Column
    private String provinceName;
    @Column
    private int provinceCode;

    private List<City> citys;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public List<City> getCitys() {
        if(citys == null || citys.isEmpty()){
            citys = SQLite.select()
                    .from(City.class)
                    .where(City_Table.province_id.eq(id))
                    .queryList();
        }
        return citys;
    }

    public void setCitys(List<City> citys) {
        this.citys = citys;
    }
}
