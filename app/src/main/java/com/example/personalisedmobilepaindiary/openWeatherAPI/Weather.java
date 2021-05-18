package com.example.personalisedmobilepaindiary.openWeatherAPI;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("main")
    public String main;

    @SerializedName("icon")
    public String icon;
}
