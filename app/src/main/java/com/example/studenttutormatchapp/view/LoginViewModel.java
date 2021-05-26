package com.example.studenttutormatchapp.view;

import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.studenttutormatchapp.helpers.Credentials;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.model.repositories.Repository;
import com.example.studenttutormatchapp.model.repositories.UserRepository;
import com.example.studenttutormatchapp.remote.response.ApiResource;

import okhttp3.ResponseBody;

public class LoginViewModel extends ViewModel implements LifecycleObserver {
    private UserRepository repository;
    private final MutableLiveData<Credentials> loginInput = new MutableLiveData<>();

    public final LiveData<ApiResource<ResponseBody>> loginHandle =
            Transformations.switchMap(loginInput, (credentials) -> {




                return repository.loginUser(credentials);
            });


    public LoginViewModel() {
        this.repository = UserRepository.getInstance();

    }


    public void login(Credentials credentials){
        loginInput.setValue(credentials);
    }




}
