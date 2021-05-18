package com.example.personalisedmobilepaindiary.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.personalisedmobilepaindiary.databinding.LocationPieChartFragmentBinding;
import com.example.personalisedmobilepaindiary.entities.PainRecord;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationPieChartFragment extends Fragment {
    private LocationPieChartFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LocationPieChartFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        createChart();
        return view;
    }

    public void createChart() {
        // create a hashmap to hold the data from pain records
        HashMap<String, Integer> locationMap = new HashMap<String, Integer>() {{
            put("Back", 0);
            put("Neck", 0);
            put("Head", 0);
            put("Knees", 0);
            put("Hips", 0);
            put("Abdomen", 0);
            put("Elbows", 0);
            put("Shoulders", 0);
            put("Shins", 0);
            put("Jaw", 0);
            put("Facial", 0);
        }};
        HomeFragment.recordViewModel.getAllRecord().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                for (PainRecord r : painRecords) {
                    locationMap.put(r.getLocation(), locationMap.get(r.getLocation()) + 1);
                }
                
                //Draw the chart
                List<PieEntry> entries = new ArrayList<>();
                locationMap.forEach((k, v) -> {
                    if(v != 0){
                        entries.add(new PieEntry(v, k));
                    }
                });
                PieDataSet set = new PieDataSet(entries, "");
                PieData data = new PieData(set);
                binding.locationPieChart.setData(data);
                set.setColors(ColorTemplate.PASTEL_COLORS);
                set.setValueTextColor(Color.parseColor("#333333"));
                set.setValueTextSize(18);
                binding.locationPieChart.setEntryLabelColor(Color.parseColor("#333333"));
                binding.locationPieChart.setEntryLabelTextSize(18);
                binding.locationPieChart.animateXY(500,500);
                binding.locationPieChart.getLegend().setEnabled(false);
                binding.locationPieChart.getDescription().setEnabled(false);
                binding.locationPieChart.setDrawHoleEnabled(false);
                binding.locationPieChart.invalidate();
            }
        });

    }
}
