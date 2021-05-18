package com.example.personalisedmobilepaindiary.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.personalisedmobilepaindiary.entities.Users;
import com.example.personalisedmobilepaindiary.repository.UserRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepo;
    private LiveData<List<Users>> allUser;
    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepository(application);
        allUser = userRepo.getAllUsers();
    }

    public void addNewUser(Users user){
        userRepo.addNewUser(user);
    }

    public void deleteUser(Users user){
        userRepo.deleteUser(user);
    }

    public void updateUser(Users user){
        userRepo.updateUser(user);
    }

    public CompletableFuture<Users> searchUserByID(final int id){
        return userRepo.searchUserByID(id);
    }

    public CompletableFuture<Users> searchUserByEmail(final String email){
        return userRepo.searchUserByEmail(email);
    }

    public void deleteAll(){
        userRepo.deleteAll();
    }
}
