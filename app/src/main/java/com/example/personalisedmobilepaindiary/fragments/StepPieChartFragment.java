package com.example.personalisedmobilepaindiary.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalisedmobilepaindiary.databinding.StepTakenPieChartFragmentBinding;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StepPieChartFragment extends Fragment {
    private StepTakenPieChartFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = StepTakenPieChartFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        createChart();

        return view;
    }

    public void createChart() {
        Calendar today = Calendar.getInstance();
        String date = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + today.get(Calendar.DATE);
        HomeFragment.recordViewModel.searchRecordByDate(date).thenAccept(painRecord -> {
            int stepsTaken = painRecord.getStepsTaken();
            int remaining = 0;
            if (painRecord.getStepGoal() > painRecord.getStepsTaken()){
                remaining = painRecord.getStepGoal() - painRecord.getStepsTaken();
            }
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(stepsTaken, "Steps Taken"));
            entries.add(new PieEntry(remaining, "Remaining"));
            PieDataSet set = new PieDataSet(entries, "");
            PieData data = new PieData(set);
            List<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#94d137"));
            colors.add(Color.parseColor("#878787"));
            set.setColors(colors);
            set.setValueTextSize(18);
            binding.stepPieChart.setData(data);
            binding.stepPieChart.getDescription().setEnabled(false);
            binding.stepPieChart.setEntryLabelTextSize(18);
        });
    }
}
