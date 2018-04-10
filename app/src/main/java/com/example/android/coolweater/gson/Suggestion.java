package com.example.android.coolweater.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {
    @SerializedName("comf")
    private Confort confort;
    @SerializedName("cw")
    private CarWash carWash;
    private Sport sport;

    public class Confort{
        @SerializedName("txt")
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
    public class CarWash{
        @SerializedName("txt")
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
    public class Sport{
        @SerializedName("txt")
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public Confort getConfort() {
        return confort;
    }

    public void setConfort(Confort confort) {
        this.confort = confort;
    }

    public CarWash getCarWash() {
        return carWash;
    }

    public void setCarWash(CarWash carWash) {
        this.carWash = carWash;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
}
