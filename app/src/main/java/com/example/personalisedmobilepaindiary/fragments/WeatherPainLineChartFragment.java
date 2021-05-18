package com.example.personalisedmobilepaindiary.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.personalisedmobilepaindiary.databinding.WeatherPainLineChartBinding;
import com.example.personalisedmobilepaindiary.entities.PainRecord;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class WeatherPainLineChartFragment extends Fragment {
    private WeatherPainLineChartBinding binding;
    private List<PainRecord> recordList;
    private Date start;
    private Date end;
    private List<Double> weatherValues;
    private List<Double> painLevels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WeatherPainLineChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        binding.startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickDialog(binding.startDate);
            }
        });

        binding.endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickDialog(binding.endDate);

            }
        });
        try {
            end = sdf.parse(binding.endDate.getText().toString());
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        binding.generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.startDate.getText().toString().length() == 0
                        || binding.endDate.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Empty date field!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        start = sdf.parse(binding.startDate.getText().toString());
                        end = sdf.parse(binding.endDate.getText().toString());
                        Log.d("start date", start.toString());
                        Log.d("end date", end.toString());
                    } catch (ParseException e) {
                        Log.e("Start and end date", e.getMessage());

                    } catch (NullPointerException npe) {
                        Log.e("NPE", npe.getMessage());
                    }
                    Date current = new Date();
                    if (start.after(end) || start.after(current) || end.after(current)) {
                        Toast.makeText(getContext(), "Invalid date range", Toast.LENGTH_SHORT).show();
                    } else {
                        getRecordList();
                        createChart(binding.weatherVarSpinner.getSelectedItem().toString());
                    }
                }
            }
        });
        binding.correlationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordList != null && recordList.size() >= 3){
                    double[] res = correlationTest();
                    Log.d("R value", Double.toString(res[0]));
                    Log.d("P", Double.toString(res[1]));
                    binding.rVal.setText(Double.toString(res[0]));
                    binding.sigVal.setText(Double.toString(res[1]));
                }else{
                    Toast.makeText(getContext(), "Unable to perform correlation test", Toast.LENGTH_SHORT).show();
                    binding.rVal.setText("0.00");
                    binding.sigVal.setText("0.00");
                }
            }
        });

        return view;
    }

    public void createDatePickDialog(TextView textView) {
        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), 0, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, dayOfMonth);
        dialog.show();
    }

    public void getRecordList() {
        recordList = new ArrayList<>();
        HomeFragment.recordViewModel.getAllRecord().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                painRecords.forEach(painRecord -> {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date recordDate = sdf.parse(painRecord.getRecordDate());
                        if ((recordDate.equals(start) || recordDate.after(start)) && (recordDate.before(end) || recordDate.equals(end))) {
                            recordList.add(painRecord);
                        }
                    } catch (ParseException e) {
                        Log.e("Filtering records", e.getMessage());
                    }
                });
            }
        });

    }

    public void createChart(String variableName) {
        LineChart chart = binding.painLineChart;
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.parseColor("#f0f1f2"));
        chart.setDrawGridBackground(false);

        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

        YAxis right = chart.getAxisRight();
        right.setTextColor(Color.parseColor("#333333"));
        right.setDrawGridLines(false);
        right.setTextSize(10f);

        YAxis left = chart.getAxisLeft();
        left.setTextColor(Color.parseColor("#333333"));
        left.setDrawGridLines(false);
        left.setTextSize(10f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                return sdf.format(new Date((long) value));
            }
        });
        xAxis.setLabelRotationAngle(90f);


        LineDataSet painIntensity = new LineDataSet(generateLineEntries(), "Pain Intensity");
        painIntensity.setLineWidth(2f);
        painIntensity.setColor(Color.parseColor("#346b21"));
        painIntensity.setAxisDependency(YAxis.AxisDependency.LEFT);
        painIntensity.setCircleColor(Color.parseColor("#346b21"));

        LineDataSet weatherVar = new LineDataSet(generateLineEntries(variableName), variableName);
        weatherVar.setLineWidth(2);
        weatherVar.setColor(Color.parseColor("#94d137"));
        weatherVar.setAxisDependency(YAxis.AxisDependency.RIGHT);
        weatherVar.setCircleColor(Color.parseColor("#94d137"));

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(painIntensity);
        dataSets.add(weatherVar);
        LineData data = new LineData(dataSets);

        chart.setData(data);
        chart.invalidate();
    }

    public List<Entry> generateLineEntries(String variableName) {
        LineData d = new LineData();
        weatherValues = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Entry> entries = new ArrayList<>();
        switch (variableName) {
            case "Temperature":
                recordList.forEach(painRecord -> {
                    try {
                        Date date = sdf.parse(painRecord.getRecordDate());
                        float dt = (float) date.getTime();
                        float temp = painRecord.getTemp();
                        weatherValues.add((double) temp);
                        entries.add(new Entry(dt, temp));
                    } catch (ParseException e) {
                        Log.e("Retrieve line data", e.getMessage());
                    }
                });
                break;
            case "Humidity":
                recordList.forEach(painRecord -> {
                    try {
                        Date date = sdf.parse(painRecord.getRecordDate());
                        float dt = (float) date.getTime();
                        float humidity = painRecord.getHumidity();
                        weatherValues.add((double) humidity);
                        entries.add(new Entry(dt, humidity));
                    } catch (ParseException e) {
                        Log.e("Retrieve line data", e.getMessage());
                    }
                });
                break;
            case "Pressure":
                recordList.forEach(painRecord -> {
                    try {
                        Date date = sdf.parse(painRecord.getRecordDate());
                        float dt = (float) date.getTime();
                        float pressure = painRecord.getPressure();
                        weatherValues.add((double) pressure);
                        entries.add(new Entry(dt, pressure));
                    } catch (ParseException e) {
                        Log.e("Retrieve line data", e.getMessage());
                    }
                });
                break;
        }
        return entries;
    }

    public List<Entry> generateLineEntries() {
        LineData d = new LineData();
        painLevels = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Entry> entries = new ArrayList<>();
        recordList.forEach(painRecord -> {
            try {
                Date date = sdf.parse(painRecord.getRecordDate());
                float dt = (float) date.getTime();
                float intensity = painRecord.getIntensityLevel();
                painLevels.add((double) intensity);
                entries.add(new Entry(dt, intensity));
            } catch (ParseException e) {
                Log.e("Retrieve line data", e.getMessage());
            }
        });
        return entries;
    }

    public double[] correlationTest(){
        double[][] data = new double[painLevels.size()][2];

        double[] res = new double[2];

        for (int i = 0; i < painLevels.size(); i++) {
            data[i][0] = painLevels.get(i);
            data[i][1] = weatherValues.get(i);
        }

        RealMatrix m = MatrixUtils.createRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(m);
        RealMatrix corM = pc.getCorrelationMatrix();
        RealMatrix pM = pc.getCorrelationPValues();

        res[0] = corM.getEntry(0,1);
        res[1] = pM.getEntry(0,1);

        return res;
    }

}
