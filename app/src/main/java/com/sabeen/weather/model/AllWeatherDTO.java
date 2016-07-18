package com.sabeen.weather.model;

import java.util.List;

/**
 * DTO class for combining Current Weather and Weather Forecast of coming 10 days
 * Created by sabeen on 6/14/16.
 */

public class AllWeatherDTO {
    private CurrentWeather currentWeather;
    private List<WeatherItems> weatherItemsList;

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public List<WeatherItems> getWeatherItemsList() {
        return weatherItemsList;
    }

    public void setWeatherItemsList(List<WeatherItems> weatherItemsList) {
        this.weatherItemsList = weatherItemsList;
    }
}
