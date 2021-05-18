package com.example.personalisedmobilepaindiary.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.personalisedmobilepaindiary.DAO.UserDAO;
import com.example.personalisedmobilepaindiary.database.UserDatabase;
import com.example.personalisedmobilepaindiary.entities.Users;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class UserRepository {
    private UserDAO dao;
    private LiveData<List<Users>> allUsers;

    public UserRepository(Application app) {
        UserDatabase db = UserDatabase.getInstance(app);
        dao = db.userDAO();
        allUsers = dao.getAll();
    }

    public LiveData<List<Users>> getAllUsers() {
        return allUsers;
    }

    public void addNewUser(final Users user) {
        UserDatabase.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.addNewUser(user);
            }
        });
    }

    public void updateUser(final Users user) {
        UserDatabase.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateUser(user);
            }
        });
    }

    public void deleteUser(final Users user) {
        UserDatabase.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteUser(user);
            }
        });
    }

    public void deleteAll() {
        /*
        DON'T DO THIS
         */
        UserDatabase.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    public CompletableFuture<Users> searchUserByID(final int id) {
        return CompletableFuture.supplyAsync(new Supplier<Users>() {
            @Override
            public Users get() {
                return dao.searchUserByID(id);
            }
        }, UserDatabase.executor);
    }

    public CompletableFuture<Users> searchUserByEmail(final String email){
        return CompletableFuture.supplyAsync(new Supplier<Users>() {
            @Override
            public Users get() {
                return dao.searchUserByEmail(email);
            }
        }, UserDatabase.executor);
    }
}
