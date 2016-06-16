package com.imobile3.weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.imobile3.weather.model.AllWeatherDTO;
import com.imobile3.weather.model.CurrentWeather;
import com.imobile3.weather.model.WeatherItems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling network call using AsyncTask
 * Created by sabeen on 6/14/16.
 */

public class WeatherAsyncCall extends AsyncTask<String, Void,AllWeatherDTO> {
    public AsyncResponseInterface asyncResponseInterface = null;
    private ProgressDialog progressDialog;
    private Context context;
    public WeatherAsyncCall(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
    }

    @Override
    protected AllWeatherDTO doInBackground(String... params) {
        List<WeatherItems> weatherItemsList = new ArrayList<>();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }
            JSONObject topLevel = new JSONObject(builder.toString());
            JSONObject currentObservation = topLevel.getJSONObject("current_observation");
            JSONObject forecastObject = topLevel.getJSONObject("forecast");
            JSONObject simpleForecastObject = forecastObject.getJSONObject("simpleforecast");
            JSONArray forecastDay = simpleForecastObject.getJSONArray("forecastday");
//          For getting list of weather forecast for comming 10 days
            for(int i =0;i<forecastDay.length();i++){
                JSONObject dateObject = forecastDay.getJSONObject(i).getJSONObject("date");
                String weekday = dateObject.getString("weekday");
                String monthName = dateObject.getString("monthname");
                String dayName = dateObject.getString("day");
                String day = weekday+" "+monthName+" "+dayName;

                JSONObject highObject = forecastDay.getJSONObject(i).getJSONObject("high");
                JSONObject lowObject = forecastDay.getJSONObject(i).getJSONObject("low");
                String highF = "HIGH : "+highObject.getString("fahrenheit")+(char) 0x00B0+"F";
                String highC = "HIGH : "+ highObject.getString("celsius")+(char) 0x00B0+"C";
                String lowF = "LOW : "+lowObject.getString("fahrenheit")+(char) 0x00B0+"F";
                String lowC = "LOW : "+ lowObject.getString("celsius")+(char) 0x00B0+"C";

                String condtion = forecastDay.getJSONObject(i).getString("conditions");

                String avehumidity = "HUMIDITY : "+forecastDay.getJSONObject(i).getString("avehumidity")+"%";

                JSONObject aveWindObj = forecastDay.getJSONObject(i).getJSONObject("avewind");
                String speed = aveWindObj.getString("mph");
                String dir = aveWindObj.getString("dir");
                String wind = "WIND : "+speed+"MPH "+dir;

                WeatherItems weatherItems = new WeatherItems();
                weatherItems.setCondition(condtion);
                weatherItems.setHumidity(avehumidity);
                weatherItems.setHighTempC(highC);
                weatherItems.setLowTempC(lowC);
                weatherItems.setHighTempF(highF);
                weatherItems.setLowTempF(lowF);
                weatherItems.setDay(day);
                weatherItems.setWind(wind);

                weatherItemsList.add(weatherItems);
            }
//          For getting Current Weather data
            JSONObject locationObject = currentObservation.getJSONObject("display_location");
            String location = locationObject.getString("full")+(" (CURRENT)");

            String condition = currentObservation.getString("weather");
            String humidity = "HUMIDITY : "+currentObservation.getString("relative_humidity");

            String windDir = currentObservation.getString("wind_dir");
            String windMph= currentObservation.getString("wind_mph");
            String wind = "WIND : "+windMph+" MPH "+windDir;

            String feelsLikeC = "FEELS LIKE : "+currentObservation.getString("feelslike_c")+(char) 0x00B0+"C";
            String feelsLikeF = "FEELS LIKE : "+currentObservation.getString("feelslike_f")+(char) 0x00B0+"F";

            String tempC = currentObservation.getString("temp_c")+(char) 0x00B0+"C";
            String tempF = currentObservation.getString("temp_f")+(char) 0x00B0+"F";

            CurrentWeather currentWeather = new CurrentWeather();
            currentWeather.setTemperatureC(tempC);
            currentWeather.setTemperatureF(tempF);
            currentWeather.setLocation(location);
            currentWeather.setWind(wind);
            currentWeather.setHumidity(humidity);
            currentWeather.setCondition(condition);
            currentWeather.setFeelsLikeC(feelsLikeC);
            currentWeather.setFeelsLikeF(feelsLikeF);

            AllWeatherDTO allWeatherDTO = new AllWeatherDTO();
            allWeatherDTO.setCurrentWeather(currentWeather);
            allWeatherDTO.setWeatherItemsList(weatherItemsList);
            return allWeatherDTO;
        } catch (Throwable e) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(AllWeatherDTO allWeatherDTO) {
        asyncResponseInterface.processFinish(allWeatherDTO);
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

}
