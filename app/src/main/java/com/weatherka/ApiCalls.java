package com.weatherka;

public class ApiCalls {
    private static final String appid = "7c638df619549d5755e7f0b22fc35bd9";
    private static final String url = "https://api.openweathermap.org/data/2.5/weather?lat=";

    public static String getUrlApi(double lat,double lon) {
        return url+lat+"&lon="+lon+"&appid="+appid;
    }


}
