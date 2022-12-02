package com.weatherka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CitySpecificWeather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent city = getIntent();
        String cityName = city.getStringExtra(Intent.EXTRA_TEXT);
        TextView cityInfo = (TextView) findViewById(R.id.cityName);
        cityInfo.setText(cityName);
    }

}
