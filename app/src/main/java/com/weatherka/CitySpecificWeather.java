package com.weatherka;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class CitySpecificWeather extends AppCompatActivity {

    DecimalFormat df = new DecimalFormat("#.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        Intent city = getIntent();
        TextView cityInfo = (TextView) findViewById(R.id.cityName);
        System.out.println(cityInfo.getText().toString());

        String cityName = city.getStringExtra("CITY_NAME");
        String cityLocation = city.getStringExtra("CITY_LOCATION_DEF");
        cityInfo.setText(cityName);

        try {
            System.out.println(cityName + "GEOCODER");
            System.out.println(String.valueOf(cityInfo.getText()) + "GEOCODER");
            if (cityName.equals("miasto") || cityName.equals("")) {
                cityInfo.setText(cityLocation);
                System.out.println(cityName + "CITY NAME MIASTO");
            }
            List<Address> addresses = geocoder.getFromLocationName(cityInfo.getText().toString(), 1);
            String lat = String.valueOf(addresses.get(0).getLatitude());
            String lon = String.valueOf(addresses.get(0).getLongitude());
            resultTemp(ApiCalls.getUrlApi(lat, lon));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resultTemp(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    TextView tempCity = (TextView) findViewById(R.id.tempCity);
                    tempCity.setText(df.format(jsonObjectMain.getDouble("temp") - 273.15) + "\u2103");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
