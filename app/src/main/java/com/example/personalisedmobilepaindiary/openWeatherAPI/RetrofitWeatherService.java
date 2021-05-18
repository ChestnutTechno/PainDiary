package com.example.personalisedmobilepaindiary.openWeatherAPI;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitWeatherService {
    @GET("data/2.5/weather?")
    Call<WeatherResponse> getCurrentWeatherData(@Query("units") String unit, @Query("lat") String lat, @Query("lon") String lon, @Query("appid") String app_id);
}
