package com.example.personalisedmobilepaindiary.openWeatherAPI;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherResponse {
    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<Weather>();
    @SerializedName("main")
    public Main main;
    @SerializedName("name")
    public String name;
    @SerializedName("sys")
    public Sys sys;
}



