package com.example.personalisedmobilepaindiary.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.personalisedmobilepaindiary.DAO.PainRecordDAO;
import com.example.personalisedmobilepaindiary.database.PainRecordDatabase;
import com.example.personalisedmobilepaindiary.entities.PainRecord;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class PainRecordRepository {
    private PainRecordDAO dao;
    private LiveData<List<PainRecord>> allRecord;

    public PainRecordRepository(Application app) {
        PainRecordDatabase db = PainRecordDatabase.getInstance(app);
        dao = db.painRecordDAO();
        allRecord = dao.getAll();
    }

    public LiveData<List<PainRecord>> getAllRecord() {
        return allRecord;
    }

    public void addNewRecord(final PainRecord painRecord) {
        PainRecordDatabase.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.addNewRecord(painRecord);
            }
        });
    }

    public void updateRecord(final PainRecord painRecord) {
        PainRecordDatabase.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateRecord(painRecord);
            }
        });
    }

    public void deleteRecord(final PainRecord painRecord) {
        PainRecordDatabase.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteRecord(painRecord);
            }
        });
    }

    public void deleteAll() {
        /*
        DON'T DO THIS
         */
        PainRecordDatabase.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    public CompletableFuture<PainRecord> searchRecordByDate(final String date) {
        return CompletableFuture.supplyAsync(new Supplier<PainRecord>() {
            @Override
            public PainRecord get() {
                return dao.searchPainRecordByDate(date);
            }
        }, PainRecordDatabase.executor);
    }
}
