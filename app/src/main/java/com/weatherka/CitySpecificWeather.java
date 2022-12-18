package com.weatherka;

import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DecimalFormat;

public class CitySpecificWeather extends AppCompatActivity{
    TextView tempCity;

    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        Intent city = getIntent();

        resultTemp(ApiCalls.getUrlApi(city.getStringExtra("lat"),city.getStringExtra("lon")));
        String cityName = city.getStringExtra("CITY_NAME");
        String temp = city.getStringExtra("temp");
        TextView cityInfo = (TextView) findViewById(R.id.cityName);
        TextView tempCity = (TextView) findViewById(R.id.tempCity);
        cityInfo.setText(cityName);
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
                    TextView tempCity = (TextView) findViewById(R.id.tempCity);
                    tempCity.setText(df.format(jsonObjectMain.getDouble("temp")-273.15)+"\u2103");

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
