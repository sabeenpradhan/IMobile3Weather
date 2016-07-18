package com.sabeen.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sabeen.weather.utils.Helper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Activity for getting user's zip code and temperature unit
 * Created by sabeen on 6/14/16.
 */
@SuppressLint("NewApi")
public class LocationActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private double longitude;
    private double latitude;
    private LocationManager locationManager;
    private String zip = null;
    private EditText zipEditText;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button submit;
    private String unit = "f";
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private int selectedId;
    private boolean fromWeather;
    private String regex = "^[0-9]{5}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
//      Checking if we are back to this activity from Weather Activity
        fromWeather = getIntent().getBooleanExtra("fromWeather", false);

        zipEditText = (EditText) findViewById(R.id.zip_ET);
        radioGroup = (RadioGroup) findViewById(R.id.unit_group);
        submit = (Button) findViewById(R.id.submit_button);
        prefs = getSharedPreferences("Key", MODE_PRIVATE);
        editor = prefs.edit();
        zip = prefs.getString("zip", null);
        unit = prefs.getString("unit", null);
//      Send to Weather Activity if zip already exists and if it's not coming back from Weather Activity
        if (zip != null && fromWeather == false) {
            Intent weatherIntent = new Intent(this, WeatherActivity.class);
            weatherIntent.putExtra("zip", zip);
            weatherIntent.putExtra("unit", unit);
            startActivity(weatherIntent);
            finish();
        } else {
            checkAndRequestLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }

    /**
     * Method to check if location permission is granted or not and execute other processes
     *
     * @param permission String for getting type of manifest permission
     */
    public void checkAndRequestLocationPermission(String permission) {
        if (Helper.isGreaterThanMarshmallow()) {
            int hasLocationPermission = this.checkSelfPermission(permission);
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission}, REQUEST_CODE_ASK_PERMISSIONS);
                return;
            } else {
                getPostalCode();
            }
        } else {
            getPostalCode();
        }
    }

    /**
     * Method for getting the result of users selection on permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkAndRequestLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                } else {
                    handleUserSelection();
                    Toast.makeText(this, "Location Permissions were denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Method to get postal code of user's location and process accordingly
     */
    private void getPostalCode() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = getLastKnownLocation();
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                zip = geocoder.getFromLocation(latitude, longitude, 1).get(0).getPostalCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//      if zip is discovered and if this activity was not started from  Weather Activity, start Weather Activity
        if (zip != null && fromWeather == false) {
            Intent weatherIntent = new Intent(this, WeatherActivity.class);
            weatherIntent.putExtra("zip", zip);
            weatherIntent.putExtra("unit", unit);
            editor.putString("zip", zip);
            editor.putString("unit", unit);
            editor.commit();
            startActivity(weatherIntent);
            finish();
        } else {
            handleUserSelection();
        }
    }

    /**
     * Method to find the Location object
     * @return Best Location if found else return null
     */
    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            for (String provider : providers) {
                Location location = locationManager.getLastKnownLocation(provider);
                if (location == null) {
                    continue;
                }
                if (bestLocation == null
                        || location.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = location;
                }
            }
            if (bestLocation == null) {
                return null;
            }
        }
        return bestLocation;
    }

    /**
     * Method to check whether input zip is valid or not
     * @param zip String input
     * @return true of false
     */
    public boolean isAValidZipCode(String zip) {
        return Pattern.matches(regex, zip);
    }

    /**
     * Method to handle user selection of zip and temperature unit
     */
    private void handleUserSelection(){
        if (zip == null) {
            Toast.makeText(this, "Location Not found, Please select one", Toast.LENGTH_SHORT).show();
        }
//          Listen to user's unit and zip selection and execute accordingly
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zip = zipEditText.getText().toString();
                if (Helper.isNetworkConnected(LocationActivity.this)) {
                    if (isAValidZipCode(zip)) {
                        selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(selectedId);
                        unit = radioButton.getText().toString();

                        Intent weatherIntent = new Intent(v.getContext(), WeatherActivity.class);
                        weatherIntent.putExtra("zip", zip);
                        weatherIntent.putExtra("unit", unit);
                        editor.putString("zip", zip);
                        editor.putString("unit", unit);
                        editor.commit();
                        startActivity(weatherIntent);
                        finish();
                    } else {
                        Toast.makeText(LocationActivity.this, "Please enter valid ZIP code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LocationActivity.this, "Please connect to an Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
