package com.example.personalisedmobilepaindiary.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.personalisedmobilepaindiary.fragments.LocationPieChartFragment;
import com.example.personalisedmobilepaindiary.fragments.StepPieChartFragment;
import com.example.personalisedmobilepaindiary.fragments.WeatherPainLineChartFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                LocationPieChartFragment locationPieChartFragment = new LocationPieChartFragment();
                return locationPieChartFragment;
            case 1:
                StepPieChartFragment stepPieChartFragment = new StepPieChartFragment();
                return stepPieChartFragment;
            case 2:
                WeatherPainLineChartFragment weatherPainLineChartFragment = new WeatherPainLineChartFragment();
                return weatherPainLineChartFragment;
            default:
                LocationPieChartFragment locationPieChartFragment3 = new LocationPieChartFragment();
                return locationPieChartFragment3;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
