package com.weatherka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.GpsStatus;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button show = (Button) findViewById(R.id.showBtn);

        //Main activity temperature view for actual location, **to implement**
        //TextView tempAct = (TextView) findViewById(R.id.tempAct);

        show.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCityInput(v);
            }
        });
    }

    public void onCityInput(View view) {
        EditText cityInfo = (EditText) findViewById(R.id.cityNameInput);
        Intent city = new Intent(MainActivity.this, CitySpecificWeather.class);
        city.putExtra("CITY_NAME", cityInfo.getText().toString());
        startActivity(city);
    }

}