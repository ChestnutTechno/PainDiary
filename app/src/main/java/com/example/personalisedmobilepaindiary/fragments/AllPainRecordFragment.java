package com.example.personalisedmobilepaindiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalisedmobilepaindiary.databinding.AllPainDataRecordFragmentBinding;
import com.example.personalisedmobilepaindiary.entities.PainRecord;

import java.util.List;

public class AllPainRecordFragment extends Fragment {
    private AllPainDataRecordFragmentBinding binding;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AllPainDataRecordFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        HomeFragment.recordViewModel.getAllRecord().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                if(painRecords.size()==0){
                    Toast.makeText(view.getContext(), "no records found", Toast.LENGTH_LONG).show();
                }else {
                    layoutManager = new LinearLayoutManager(view.getContext());
                    binding.recyclerView.setLayoutManager(layoutManager);
                    adapter = new RecyclerViewAdapter(painRecords);
                    binding.recyclerView.setAdapter(adapter);
                }

            }
        });

        return view;
    }
}
