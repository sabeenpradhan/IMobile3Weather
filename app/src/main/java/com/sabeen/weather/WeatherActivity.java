package com.sabeen.weather;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sabeen.weather.adapter.WeatherAdapter;
import com.sabeen.weather.model.AllWeatherDTO;
import com.sabeen.weather.model.WeatherItems;
import com.sabeen.weather.utils.Helper;

import java.util.List;


/**
 * Activity for loading Current Weather and Weather Forecast of coming 10 days
 * Uses WeatherAdapter to load the data
 * Created by sabeen on 6/14/16.
 */
public class WeatherActivity extends AppCompatActivity implements AsyncResponseInterface {
    private List<WeatherItems> weatherItemsList;
    private WeatherAdapter weatherAdapter;
    private RecyclerView recyclerView;
    private String url;
    private Dialog dialog;
    WeatherAsyncCall weatherAsyncCall = new WeatherAsyncCall(this);
    private Intent locationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        locationIntent = new Intent(this, LocationActivity.class);
        locationIntent.putExtra("fromWeather", true);

//      Custom Dialog for about me
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.about_me);

        SharedPreferences sharedPreferences = getSharedPreferences("Key", MODE_PRIVATE);
        String baseUrl = sharedPreferences.getString("base_url", null);
        String zip = getIntent().getStringExtra("zip");
        String url = baseUrl + zip + ".json";

        if(Helper.isNetworkConnected(this)) {
            weatherAsyncCall.asyncResponseInterface = this;
            weatherAsyncCall.execute(url);
        }else {
            Toast.makeText(this, "Please connect to an Internet", Toast.LENGTH_SHORT).show();
            startActivity(locationIntent);
            finish();
        }
    }

    /**
     * Method of AsyncResponseInterface to get the onPostExecute result of WeatherAsyncCall
     * @param allWeatherDTO
     */
    @Override
    public void processFinish(AllWeatherDTO allWeatherDTO) {
        if (allWeatherDTO != null) {
            weatherAdapter = new WeatherAdapter(allWeatherDTO.getWeatherItemsList(), allWeatherDTO.getCurrentWeather(), this);
            recyclerView = (RecyclerView) findViewById(R.id.weather_RV);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(weatherAdapter);
        } else {
            locationIntent.putExtra("fromWeather", true);
            startActivity(locationIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.imobile3_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//       Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent locationIntent = new Intent(this, LocationActivity.class);
                locationIntent.putExtra("fromWeather", true);
                startActivity(locationIntent);
                return true;
            case R.id.about:
                dialog.show();
                ImageView close = (ImageView) dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
