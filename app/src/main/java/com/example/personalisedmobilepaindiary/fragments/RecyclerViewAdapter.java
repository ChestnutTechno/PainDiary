package com.example.personalisedmobilepaindiary.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalisedmobilepaindiary.databinding.RvLayoutBinding;
import com.example.personalisedmobilepaindiary.entities.PainRecord;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<PainRecord> recordList;

    public RecyclerViewAdapter(List<PainRecord> recordList) {
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RvLayoutBinding binding = RvLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        final PainRecord record = recordList.get(position);
        holder.binding.recordDate.setText(record.getRecordDate());
        holder.binding.recordIntensity.setText("Intensity level: "+ Integer.toString(record.getIntensityLevel()));
        holder.binding.recordLocation.setText("Location: " + record.getLocation());
        holder.binding.recordMood.setText("Mood: "+ record.getMood());
        holder.binding.recordStep.setText("Steps: "+ Integer.toString(record.getStepsTaken()));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private RvLayoutBinding binding;
        public ViewHolder(RvLayoutBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
