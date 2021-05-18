package com.example.personalisedmobilepaindiary.fragments;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalisedmobilepaindiary.LoginActivity;
import com.example.personalisedmobilepaindiary.R;
import com.example.personalisedmobilepaindiary.databinding.PainDataEntryFragmentBinding;
import com.example.personalisedmobilepaindiary.entities.PainRecord;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PainDataEntry extends Fragment {
    private PainDataEntryFragmentBinding binding;
    String[] desc = {"Very Good", "Good", "Average", "Low", "Very Low"};
    int[] images = {R.drawable.ic_very_good, R.drawable.ic_good, R.drawable.ic_average, R.drawable.ic_low, R.drawable.ic_very_low};
    private AlarmManager alarmManager;
    private PendingIntent sender;

    public PainDataEntry() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PainDataEntryFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        sender = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), desc, images);
        binding.moodSpinner.setAdapter(adapter);

        binding.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        binding.cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
        int day = now.get(Calendar.DATE);
        String date = year + "-" + month + "-" + day;
        HomeFragment.recordViewModel.searchRecordByDate(date).thenAccept(painRecord -> {
            if (painRecord != null) {
                binding.saveButton.setEnabled(false);
            }
        });


        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.stepTaken.getText().toString().length() == 0 || binding.stepGoal.getText().toString().length() == 0) {
                    Toast.makeText(view.getContext(), "Step goal or steps taken field cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    saveRecord();
                }
            }
        });

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.saveButton.isEnabled())
                    binding.saveButton.setEnabled(true);
            }
        });

        return view;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    }

    public void saveRecord() {
        Toast.makeText(getContext(), "Saving", Toast.LENGTH_SHORT);
        int intensityLevel = (int) binding.painRange.getValue();
        String location = binding.locSpinner.getSelectedItem().toString();
        String mood = binding.moodSpinner.getSelectedItem().toString();
        int stepsTaken = Integer.parseInt(binding.stepTaken.getText().toString());
        int stepGoal = Integer.parseInt(binding.stepGoal.getText().toString());
        float temp = HomeFragment.main.temp;
        float humidity = HomeFragment.main.humidity;
        float pressure = HomeFragment.main.pressure;
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
        int day = now.get(Calendar.DATE);
        String date = year + "-" + month + "-" + day;
        String email = LoginActivity.USER_EMAIL;
        HomeFragment.recordViewModel.searchRecordByDate(date).thenAccept(painRecord -> {
            if (painRecord == null) {
                PainRecord record = new PainRecord(date, email, intensityLevel, location, mood, stepsTaken, stepGoal, temp, humidity, pressure);
                HomeFragment.recordViewModel.addNewRecord(record);
                Snackbar.make(getView(), "Pain Record created", Snackbar.LENGTH_SHORT).show();
                binding.saveButton.setEnabled(false);
            }else{
                painRecord.setIntensityLevel(intensityLevel);
                painRecord.setLocation(location);
                painRecord.setMood(mood);
                painRecord.setStepsTaken(stepsTaken);
                painRecord.setStepGoal(stepGoal);
                painRecord.setTemp(temp);
                painRecord.setHumidity(humidity);
                painRecord.setPressure(pressure);
                HomeFragment.recordViewModel.updateRecord(painRecord);
                Snackbar.make(getView(), "Update successful", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelAlarm() {
        alarmManager.cancel(sender);
        binding.alarmText.setText(("No alarm set"));
    }

    public void setAlarm() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog picker = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar current = Calendar.getInstance();
                        Log.d("Alarm timezone", current.getTimeZone().getDisplayName());
                        Calendar alarmSet = (Calendar) current.clone();

                        alarmSet.set(Calendar.SECOND, 0);
                        alarmSet.set(Calendar.MILLISECOND, 0);
                        alarmSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        alarmSet.set(Calendar.MINUTE, minute);

                        if (alarmSet.compareTo(current) <= 0) {
                            alarmSet.add(Calendar.DAY_OF_MONTH, 1);
                        }


                        Calendar notificationTime = Calendar.getInstance();
                        notificationTime.setTimeInMillis(alarmSet.getTimeInMillis() - (1000 * 60 * 2));
                        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), sender);

                        binding.alarmText.setText("Alarm set to: "
                                + alarmSet.get(Calendar.HOUR_OF_DAY) + ":"
                                + alarmSet.get(Calendar.MINUTE));

                        Log.d("Alarm set", (notificationTime.get(Calendar.MONTH) + 1)
                                + "-" + notificationTime.get(Calendar.DAY_OF_MONTH)
                                + " " + notificationTime.get(Calendar.HOUR_OF_DAY)
                                + ":" + notificationTime.get(Calendar.MINUTE));
                    }
                }, hour, minute, true);
        picker.setTitle("Set Alarm Time");
        picker.show();
    }
}
