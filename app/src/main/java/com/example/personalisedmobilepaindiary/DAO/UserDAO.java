package com.example.personalisedmobilepaindiary.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.personalisedmobilepaindiary.entities.Users;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM users ORDER BY uid ASC")
    LiveData<List<Users>> getAll();

    @Query("SELECT * FROM users WHERE uid = :uid")
    Users searchUserByID(int uid);

    @Query("SELECT * FROM users WHERE email = :email")
    Users searchUserByEmail(String email);

    @Insert
    void addNewUser(Users newUser);

    @Update
    void updateUser(Users user);

    @Delete
    void deleteUser(Users user);

    @Query("DELETE FROM users")
    void deleteAll();

}
