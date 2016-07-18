package com.sabeen.weather;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Application Class for storing our base url
 * Created by sabeen on 6/15/16.
 */

public class WeatherApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = getSharedPreferences("Key",MODE_PRIVATE);
//      Runs only once when Installed
        if(!prefs.getBoolean("firstTime", false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("base_url", "http://api.wunderground.com/api/5639914b261896d9/forecast10day/conditions/q/");
            editor.putString("unit","c");
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
    }
}
