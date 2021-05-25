package com.example.studenttutormatchapp.model.repositories;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.example.studenttutormatchapp.helpers.Credentials;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.UserService;
import com.example.studenttutormatchapp.remote.response.ApiResource;
import com.example.studenttutormatchapp.remote.response.RetrofitLiveData;


import okhttp3.ResponseBody;
import retrofit2.Call;

public class UserRepository implements Repository.UserInterface {
    private UserService userService = APIUtils.getUserService();
    private SharedPreferences userSharedPreferences;

    private static UserRepository instance;

    private UserRepository(){
//        userSharedPreferences
    }

    public static UserRepository getInstance(){
        if (instance == null){
            instance = new UserRepository();
        }
        return instance;
    }


    public LiveData<ApiResource<ResponseBody>> loginUser(Credentials credentials){
        return new RetrofitLiveData<>(userService.loginUser(credentials));
    }

}
