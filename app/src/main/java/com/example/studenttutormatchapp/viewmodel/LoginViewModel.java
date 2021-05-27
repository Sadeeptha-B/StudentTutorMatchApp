package com.example.studenttutormatchapp.viewmodel;

import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.studenttutormatchapp.MyApplication;
import com.example.studenttutormatchapp.helpers.Credentials;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.model.repositories.Repository;
import com.example.studenttutormatchapp.model.repositories.UserRepository;
import com.example.studenttutormatchapp.remote.response.ApiResource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;

public class LoginViewModel extends ViewModel implements LifecycleObserver {
    private UserRepository repository;

    private final MutableLiveData<Credentials> loginInput = new MutableLiveData<>();

    public final LiveData<ApiResource<ResponseBody>> loginHandle =
            Transformations.switchMap(loginInput, (credentials) -> {
                return repository.loginUser(credentials);
            });

    @Inject
    public LoginViewModel(UserRepository repository) {
        this.repository = repository;
    }


    public void login(Credentials credentials){
        loginInput.setValue(credentials);
    }


    public void onLoginSuccess(ResponseBody response){
        try{
            JSONObject loginResponse = new JSONObject(response.string());
            String[] jwt = loginResponse.getString("jwt").split("\\.");
            byte[] decodedBytes = Base64.decode(jwt[1], Base64.URL_SAFE);
            String body = new String(decodedBytes, "UTF-8");
            JSONObject jwtObject = new JSONObject(body);

            HashMap<String, String> userData = new HashMap<>();

            userData.put("id", jwtObject.getString("sub"));
            userData.put("username", jwtObject.getString("username"));
            userData.put("IS_STUDENT", jwtObject.getString("isStudent"));
            userData.put("IS_TUTOR", jwtObject.getString("isTutor"));

            repository.storeUserSharedPref(userData);
        } catch(IOException | JSONException e){
            e.printStackTrace();
        }
    }


}
