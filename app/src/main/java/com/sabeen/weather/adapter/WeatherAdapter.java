package com.sabeen.weather.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sabeen.weather.R;
import com.sabeen.weather.model.CurrentWeather;
import com.sabeen.weather.model.WeatherItems;

import java.util.List;

/**
 * Created by sabeen on 6/15/16.
 * Adapter for loading current weather and weather forecast for a coming 10 days
 * Header as a current weather and other items as weather forecast
 */

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<WeatherItems> weatherItemsList;
    private CurrentWeather currentWeather;
    private Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    /**
     * Recycle View Holder for Weather Forecast of coming 10 days
     */
    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        private TextView day, low, high, condtion, humidity, wind;

        public WeatherViewHolder(View view) {
            super(view);
            day = (TextView) view.findViewById(R.id.day_TV);
            low = (TextView) view.findViewById(R.id.low_TV);
            high = (TextView) view.findViewById(R.id.high_TV);
            condtion = (TextView) view.findViewById(R.id.condition_today_TV);
            humidity = (TextView) view.findViewById(R.id.humidity_today_TV);
            wind = (TextView) view.findViewById(R.id.wind_TV);
        }

    }

    /**
     * Recycle View Holder for Current Weather
     */
    public class CurrentWeatherViewHolder extends RecyclerView.ViewHolder {
        private TextView condition, humidity, wind, location, temp, feelsLike;

        public CurrentWeatherViewHolder(View view) {
            super(view);
            condition = (TextView) view.findViewById(R.id.condition_today_TV);
            wind = (TextView) view.findViewById(R.id.wind_today_TV);
            humidity = (TextView) view.findViewById(R.id.humidity_today_TV);
            temp = (TextView) view.findViewById(R.id.temp_TV);
            feelsLike = (TextView) view.findViewById(R.id.feels_TV);
            location = (TextView) view.findViewById(R.id.cityTV);
        }
    }

    /**
     * Method to inflate views
     *
     * @param parent   viewgroup
     * @param viewType to differentiate between header and item
     * @return RecyclerView.ViewHolder depends on whether it is header ot item
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.weather_today, parent, false);
            return new CurrentWeatherViewHolder(itemView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.weather_items, parent, false);
            return new WeatherViewHolder(itemView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType);
    }

    /**
     * Method for binding items with View Holder
     *
     * @param holder   for getting inlfated views
     * @param position of items
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SharedPreferences prefs = context.getSharedPreferences("Key", context.MODE_PRIVATE);
        String unit = prefs.getString("unit", null);

        if (holder instanceof CurrentWeatherViewHolder) {
            CurrentWeatherViewHolder currentWeatherViewHolder = (CurrentWeatherViewHolder) holder;
            currentWeatherViewHolder.condition.setText(currentWeather.getCondition());
            currentWeatherViewHolder.wind.setText(currentWeather.getWind());
//          Checking whether temperature unit is set to Centigrade or Fahrenheit
            if (unit.toLowerCase().contains("c")) {
                currentWeatherViewHolder.feelsLike.setText(currentWeather.getFeelsLikeC());
                currentWeatherViewHolder.temp.setText(currentWeather.getTemperatureC());
            } else {
                currentWeatherViewHolder.feelsLike.setText(currentWeather.getFeelsLikeF());
                currentWeatherViewHolder.temp.setText(currentWeather.getTemperatureF());
            }
            currentWeatherViewHolder.humidity.setText(currentWeather.getHumidity());
            currentWeatherViewHolder.location.setText(currentWeather.getLocation());
        } else if (holder instanceof WeatherViewHolder) {
            WeatherItems weatherItems = weatherItemsList.get(position - 1);
            WeatherViewHolder weatherViewHolder = (WeatherViewHolder) holder;
            weatherViewHolder.condtion.setText(weatherItems.getCondition());
            weatherViewHolder.day.setText(weatherItems.getDay());
            weatherViewHolder.wind.setText(weatherItems.getWind());
//          Checking whether temperature unit is set to Centigrade or Fahrenheit
            if (unit.toLowerCase().contains("c")) {
                weatherViewHolder.high.setText(weatherItems.getHighTempC());
                weatherViewHolder.low.setText(weatherItems.getLowTempC());
            } else {
                weatherViewHolder.high.setText(weatherItems.getHighTempF());
                weatherViewHolder.low.setText(weatherItems.getLowTempF());
            }
            weatherViewHolder.humidity.setText(weatherItems.getHumidity());
        }
    }


    public WeatherAdapter(List<WeatherItems> weatherItemsList, CurrentWeather currentWeather, Context context) {
        this.weatherItemsList = weatherItemsList;
        this.currentWeather = currentWeather;
        this.context = context;
    }

    /**
     * @param position position of items
     * @return int header or item
     */
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return weatherItemsList.size() + 1;
    }
}