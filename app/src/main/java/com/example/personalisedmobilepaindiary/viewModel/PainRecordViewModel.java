package com.example.personalisedmobilepaindiary.viewModel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.personalisedmobilepaindiary.entities.PainRecord;
import com.example.personalisedmobilepaindiary.repository.PainRecordRepository;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PainRecordViewModel extends AndroidViewModel {
    private PainRecordRepository painRecordRepo;
    private LiveData<List<PainRecord>> allRecord;
    public PainRecordViewModel(@NonNull Application application) {
        super(application);
        painRecordRepo = new PainRecordRepository(application);
        allRecord = painRecordRepo.getAllRecord();
    }

    public void addNewRecord(PainRecord painRecord){
        painRecordRepo.addNewRecord(painRecord);
    }

    public void deleteRecord(PainRecord painRecord){
        painRecordRepo.deleteRecord(painRecord);
    }

    public void updateRecord(PainRecord painRecord){
        painRecordRepo.updateRecord(painRecord);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> searchRecordByDate(final String date){
        return painRecordRepo.searchRecordByDate(date);
    }

    public LiveData<List<PainRecord>> getAllRecord(){
        return allRecord;
    }

    public void deleteAll(){
        painRecordRepo.deleteAll();
    }
}