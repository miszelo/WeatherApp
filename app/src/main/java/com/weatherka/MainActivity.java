package com.weatherka;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private TextView addressText;
    private Button show;
    private Button locationButton;
    private FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = findViewById(R.id.showBtn);

        //Main activity temperature view for actual location, **to implement**
        //TextView tempAct = (TextView) findViewById(R.id.tempAct);

        show.setOnClickListener(this::onCityInput);


        locationButton = findViewById(R.id.locationButton);
        addressText = findViewById(R.id.addressText);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                //location = task.
                if (location==null) {
                    System.out.println("TESTESTES");
                }
//                System.out.println(location.getLatitude() + " " + location.getLongitude());
//                System.out.println(ApiCalls.getUrlApi(location.getLatitude(),location.getLongitude()));
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this,
                                Locale.getDefault());
                        System.out.println(location.getLatitude() + " " + location.getLongitude());
                        System.out.println(ApiCalls.getUrlApi(location.getLatitude(),location.getLongitude()));
                        List<Address> addressList = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        addressText.setText(Html.fromHtml(addressList.get(0).getCountryName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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