package com.weatherka;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class ApiCalls extends AppCompatActivity {
    private static final String appid = "7c638df619549d5755e7f0b22fc35bd9";
    private static final String url = "https://api.openweathermap.org/data/2.5/weather?lat=";

    public static String getUrlApi(String lat,String lon) {
        return url+lat+"&lon="+lon+"&appid="+appid;
    }

    public static String getUrlApiNextDays(String lat, String lon) {
        return "https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid="+appid;
    }




}