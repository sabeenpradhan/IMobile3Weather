package com.sabeen.weather.model;

/**
 * Class for weather forecast of coming 10 days
 * Created by sabeen on 6/14/16.
 */

public class WeatherItems extends Weather{
    private String day;
    private String highTempC;
    private String lowTempC;
    private String highTempF;
    private String lowTempF;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHighTempC() {
        return highTempC;
    }

    public void setHighTempC(String highTempC) {
        this.highTempC = highTempC;
    }

    public String getLowTempC() {
        return lowTempC;
    }

    public void setLowTempC(String lowTempC) {
        this.lowTempC = lowTempC;
    }

    public String getHighTempF() {
        return highTempF;
    }

    public void setHighTempF(String highTempF) {
        this.highTempF = highTempF;
    }

    public String getLowTempF() {
        return lowTempF;
    }

    public void setLowTempF(String lowTempF) {
        this.lowTempF = lowTempF;
    }
}
