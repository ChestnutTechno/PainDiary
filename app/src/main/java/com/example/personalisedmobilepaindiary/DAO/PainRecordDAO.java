package com.example.personalisedmobilepaindiary.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import com.example.personalisedmobilepaindiary.entities.PainRecord;

import java.util.List;

@Dao
public interface PainRecordDAO {

    @Query("SELECT * FROM painrecord ORDER BY id")
    LiveData<List<PainRecord>> getAll();

    @Query("SELECT * FROM painrecord WHERE recordDate like '%' || :date || '%'")
    PainRecord searchPainRecordByDate(String date);


    @Insert
    void addNewRecord(PainRecord newRecord);

    @Update
    void updateRecord(PainRecord painRecord);

    @Delete
    void deleteRecord(PainRecord painRecord);

    @Query("DELETE FROM painrecord")
    void deleteAll();

}
