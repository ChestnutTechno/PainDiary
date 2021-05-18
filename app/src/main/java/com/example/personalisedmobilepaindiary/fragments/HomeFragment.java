package com.example.personalisedmobilepaindiary.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.personalisedmobilepaindiary.R;
import com.example.personalisedmobilepaindiary.databinding.HomeFragmentBinding;
import com.example.personalisedmobilepaindiary.openWeatherAPI.Main;
import com.example.personalisedmobilepaindiary.openWeatherAPI.RetrofitWeatherService;
import com.example.personalisedmobilepaindiary.openWeatherAPI.WeatherResponse;
import com.example.personalisedmobilepaindiary.viewModel.PainRecordViewModel;
import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements LocationListener {
    private HomeFragmentBinding binding;

    private final String app_id = "4970a005f2d7455d73185c748aa6e424";
    private final String base_url = "https://api.openweathermap.org/";
    private final String units = "metric";
    private static Location location;
    private LocationManager locationManager;
    private RetrofitWeatherService weatherService;
    public static Main main;
    public static PainRecordViewModel recordViewModel;
    private String longitude;
    private String latitude;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        getCurrentLocation();
        getWeather(longitude, latitude);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherService = retrofit.create(RetrofitWeatherService.class);
        recordViewModel = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication()).create(PainRecordViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        String lon = Double.toString(location.getLongitude());
        String lat = Double.toString(location.getLatitude());
        Log.d("location", "location changed");
        getWeather(lon, lat);
    }

    public void getWeather(String lon, String lat){
        Call<WeatherResponse> call = weatherService.getCurrentWeatherData(units, lat,lon,app_id);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                Log.d(response.message(), response.toString());
                if (response.isSuccessful()){
                    WeatherResponse weatherResponse = response.body();
                    String weather = weatherResponse.weather.get(0).main;
                    main = weatherResponse.main;
                    float currentTemp = main.temp;
                    float feelsLike = main.feels_like;
                    float pressure = main.pressure;
                    float humidity = main.humidity;
                    String cityName = weatherResponse.name;
                    String countryCode = weatherResponse.sys.countryCode;

                    DecimalFormat df = new DecimalFormat("#");
                    if(countryCode == null || cityName == null){
                        binding.weatherCity.setText("Unknown area");
                    }else{
                        binding.weatherCity.setText(cityName + ", " + countryCode);
                    }
                    binding.weatherStatus.setText(weather);
                    binding.weatherTemp.setText(df.format(currentTemp));
                    binding.weatherPres.setText("Air pressure: " + df.format(pressure) + " Pa");
                    binding.feelsLike.setText("Feels like " + df.format(feelsLike) + " °C");
                    binding.humidity.setText("Humidity: " + df.format(humidity) + "%");

                    updateWeatherWidgetBg();

                    String url = "http://openweathermap.org/img/wn/";
                    String iconUrl = weatherResponse.weather.get(0).icon + ".png";
                    Log.d(TAG, url+iconUrl);
                    Picasso.get().setLoggingEnabled(true);
                    Picasso.get().load(url+iconUrl).into(binding.weatherIcon);
                }else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, t.getMessage());
            }
        });
    }

    public void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    101);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 10, this);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null){
            longitude = Double.toString(location.getLongitude());
            latitude = Double.toString(location.getLatitude());
        }else {
            Toast.makeText(getContext(), "Error in retrieving location", Toast.LENGTH_SHORT);
            Log.d("locationRetrieving", "Location retrieving failed");
        }
    }

    public static Location getLocation(){
        return location;
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void updateWeatherWidgetBg(){
        Log.d("weather_widget", "change backgroud");
        ArrayList<String> foggyWeather = new ArrayList<String>(Arrays.asList("Mist", "Smoke", "Haze", "Dust", "Fog", "Ash"));
        if (!foggyWeather.contains(binding.weatherStatus.getText())){
            if (binding.weatherStatus.getText().equals("Thunderstorm")){
                binding.weatherWidget.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.thunderstorm));
                setWeatherWidgetTextWhite();
            }
            if (binding.weatherStatus.getText().equals("Clear")){
                binding.weatherWidget.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.clear));
                setWeatherWidgetTextBlack();
            }
            if (binding.weatherStatus.getText().equals("Drizzle")){
                binding.weatherWidget.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.drizzle));
                setWeatherWidgetTextWhite();
            }
            if (binding.weatherStatus.getText().equals("Rain")){
                binding.weatherWidget.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rain));
                setWeatherWidgetTextWhite();
            }
            if (binding.weatherStatus.getText().equals("Sand")){
                binding.weatherWidget.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.sand));
                setWeatherWidgetTextBlack();
            }
            if (binding.weatherStatus.getText().equals("Snow")){
                binding.weatherWidget.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.snow));
                setWeatherWidgetTextBlack();
            }
            if (binding.weatherStatus.getText().equals("Tornado") || binding.weatherStatus.equals("Squall")){
                binding.weatherWidget.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tornado));
                setWeatherWidgetTextBlack();
            }
            if (binding.weatherStatus.getText().equals("Clouds")){
                binding.weatherWidget.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.cloud));
                setWeatherWidgetTextBlack();

            }
        }else{
            binding.weatherWidget.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.mist_fog_dust));
            setWeatherWidgetTextWhite();
        }
    }

    public void setWeatherWidgetTextBlack(){
        binding.weatherStatus.setTextColor(Color.BLACK);
        binding.weatherCity.setTextColor(Color.BLACK);
        binding.feelsLike.setTextColor(Color.BLACK);
        binding.weatherPres.setTextColor(Color.BLACK);
        binding.humidity.setTextColor(Color.BLACK);
        binding.weatherTemp.setTextColor(Color.BLACK);
        binding.celciusSymbol.setTextColor(Color.BLACK);
    }

    public void setWeatherWidgetTextWhite(){
        binding.weatherStatus.setTextColor(Color.WHITE);
        binding.weatherCity.setTextColor(Color.WHITE);
        binding.feelsLike.setTextColor(Color.WHITE);
        binding.weatherPres.setTextColor(Color.WHITE);
        binding.humidity.setTextColor(Color.WHITE);
        binding.weatherTemp.setTextColor(Color.WHITE);
        binding.celciusSymbol.setTextColor(Color.WHITE);
    }

    public RetrofitWeatherService getWeatherService() {
        return weatherService;
    }

    public void setWeatherService(RetrofitWeatherService weatherService) {
        this.weatherService = weatherService;
    }
}
