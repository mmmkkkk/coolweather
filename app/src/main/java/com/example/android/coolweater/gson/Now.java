package com.example.android.coolweater.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    @SerializedName("tmp")
    private String temperature;
    @SerializedName("cond")
    private  More more;
    public class More{
        @SerializedName("txt")
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public More getMore() {
        return more;
    }

    public void setMore(More more) {
        this.more = more;
    }
}
