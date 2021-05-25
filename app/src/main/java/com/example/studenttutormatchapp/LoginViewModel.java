package com.example.studenttutormatchapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.studenttutormatchapp.helpers.Credentials;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.model.repositories.Repository;
import com.example.studenttutormatchapp.model.repositories.UserRepository;
import com.example.studenttutormatchapp.remote.response.ApiResource;

import okhttp3.ResponseBody;

public class LoginViewModel extends ViewModel {
    private UserRepository repository;

    public LoginViewModel() {
        this.repository = UserRepository.getInstance();
    }

    public LiveData<ApiResource<ResponseBody>> loginUser(Credentials credentials){
        return repository.loginUser(credentials);
    }
}
