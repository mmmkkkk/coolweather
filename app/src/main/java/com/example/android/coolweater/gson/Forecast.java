package com.example.android.coolweater.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    private String date;
    @SerializedName("tmp")
    private Temperture temperture;
    @SerializedName("cond")
    private More more;
    public class Temperture{
        private String max;
        private String min;

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Temperture getTemperture() {
        return temperture;
    }

    public void setTemperture(Temperture temperture) {
        this.temperture = temperture;
    }

    public More getMore() {
        return more;
    }

    public void setMore(More more) {
        this.more = more;
    }
}
