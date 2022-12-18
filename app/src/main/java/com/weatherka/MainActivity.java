package com.weatherka;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationListenerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListenerCompat {

    private TextView addressText;
    private Button showButton;
    private LocationManager locationManager;
    private EditText cityNameInput;
    private TextView tempAct;
    public String lat;
    public String lon;
    DecimalFormat df = new DecimalFormat("#.#");
//    public String temp;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(), isGranted -> {
                        if (isGranted) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.permissionGranted, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    R.string.noPermission, Toast.LENGTH_LONG).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showButton = findViewById(R.id.showBtn);
        cityNameInput = findViewById(R.id.cityNameInput);
        showButton.setOnClickListener(this::onCityInput);

        addressText = findViewById(R.id.addressText);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch((Manifest.permission.ACCESS_FINE_LOCATION));
        }
    }

    public void onCityInput(View view) {



        Intent city = new Intent(MainActivity.this, CitySpecificWeather.class);
        city.putExtra("CITY_NAME", cityNameInput.getText().toString());
        System.out.println(cityNameInput.getText().toString());
        city.putExtra("lon",lon);
        city.putExtra("lat",lat);
        city.putExtra("CITY_LOCATION_DEF",addressText.getText().toString());
        System.out.println(addressText.getText().toString());
        String url = ApiCalls.getUrlApi(lat,lon);
        //ApiCalls apiCalls = new ApiCalls();
//        city.putExtra("temp", resultTemp(url));
//        System.out.println(resultTemp(url));
        city.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(city);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            addressText.setText(addresses.get(0).getLocality());
            //cityInfo.setText(addresses.get(0).getLocality());
            lat = String.valueOf(addresses.get(0).getLatitude());
            lon = String.valueOf(addresses.get(0).getLongitude());
            resultTemp(ApiCalls.getUrlApi(lat,lon));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.noPermission, Toast.LENGTH_LONG).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500L, 20.f, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void resultTemp(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    System.out.println(df.format(jsonObjectMain.getDouble("temp")-273.15));
                    TextView tempAct = (TextView) findViewById(R.id.tempAct);
                    tempAct.setText(df.format(jsonObjectMain.getDouble("temp")-273.15)+"\u2103");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
