package com.example.personalisedmobilepaindiary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.personalisedmobilepaindiary.databinding.ActivityMainBinding;
import com.mapbox.mapboxsdk.maps.MapboxMap;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.appBar.toolbar);

        appBarConfig = new AppBarConfiguration.Builder(
                R.id.home_fragment,
                R.id.pain_data_entry,
                R.id.daily_record,
                R.id.report,
                R.id.map)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        //api key: AIzaSyD9GFv1atbWUfhJ2-b6_shvTRzqPP4GId8
        //cx key: e365f05963948619f
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.appBar.toolbar, navController,
                appBarConfig);
    }

    /*
    Override onBackPressed method to handle the situation
    if the drawer is opened and user pressed back and prevent
    accidentally logout.
     */
    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isOpen()){
            binding.drawerLayout.close();
        }else{
            super.onBackPressed();
        }
    }
}