package com.example.personalisedmobilepaindiary.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.personalisedmobilepaindiary.R;

import static android.os.Build.VERSION_CODES.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    String[] desc;
    int[] images;

    public SpinnerAdapter(@NonNull Context context, String[] desc, int[] images) {
        super(context, com.example.personalisedmobilepaindiary.R.layout.mood_spinner_layout, desc);
        this.context = context;
        this.desc = desc;
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(com.example.personalisedmobilepaindiary.R.layout.mood_spinner_layout, null);
        TextView text = row.findViewById(com.example.personalisedmobilepaindiary.R.id.mood_desc);
        ImageView img = row.findViewById(com.example.personalisedmobilepaindiary.R.id.mood_image);
        text.setText(desc[position]);
        img.setImageResource(images[position]);
        return row;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(com.example.personalisedmobilepaindiary.R.layout.mood_spinner_layout, null);
        TextView text = row.findViewById(com.example.personalisedmobilepaindiary.R.id.mood_desc);
        ImageView img = row.findViewById(com.example.personalisedmobilepaindiary.R.id.mood_image);
        text.setText(desc[position]);
        img.setImageResource(images[position]);
        return row;
    }
}
