package com.example.personalisedmobilepaindiary.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class PainRecord {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    @ColumnInfo
    private String recordDate;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "intensity_level")
    @NonNull
    private int intensityLevel;

    @ColumnInfo(name = "location")
    @NonNull
    private String location;

    @ColumnInfo(name = "mood")
    @NonNull
    private String mood;

    @ColumnInfo(name = "step_taken")
    @NonNull
    private int stepsTaken;

    @ColumnInfo(name = "step_goal")
    @NonNull
    private int stepGoal;

    @ColumnInfo(name = "temp")
    @NonNull
    private float temp;

    @ColumnInfo(name = "humidity")
    @NonNull
    private float humidity;

    @ColumnInfo(name = "pressure")
    @NonNull
    private float pressure;

    @Ignore
    public PainRecord() {
    }

    public PainRecord(String recordDate, String email, int intensityLevel, @NonNull String location, @NonNull String mood, int stepsTaken, int stepGoal, float temp, float humidity, float pressure) {
        this.recordDate = recordDate;
        this.email = email;
        this.intensityLevel = intensityLevel;
        this.location = location;
        this.mood = mood;
        this.stepsTaken = stepsTaken;
        this.stepGoal = stepGoal;
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIntensityLevel() {
        return intensityLevel;
    }

    public void setIntensityLevel(int intensityLevel) {
        this.intensityLevel = intensityLevel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    public int getStepGoal() {
        return stepGoal;
    }

    public void setStepGoal(int stepGoal) {
        this.stepGoal = stepGoal;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }
}