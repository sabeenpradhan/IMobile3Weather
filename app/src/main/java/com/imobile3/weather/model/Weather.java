package com.imobile3.weather.model;

/**
 * Parent class of Current Weather and Weather Items
 * Created by sabeen on 6/14/16.
 */

public class Weather {
    private String condition;
    private String humidity;
    private String wind;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
