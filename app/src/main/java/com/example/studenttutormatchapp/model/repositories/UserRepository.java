package com.example.studenttutormatchapp.model.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;


import com.example.studenttutormatchapp.helpers.Credentials;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.UserService;
import com.example.studenttutormatchapp.remote.response.ApiResource;
import com.example.studenttutormatchapp.remote.response.RetrofitLiveData;


import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;

@Singleton
public class UserRepository implements Repository.UserInterface {
    private UserService userService = APIUtils.getUserService();
    SharedPreferences userProfile;

    @Inject
    public UserRepository(Context context){
        userProfile = context.getSharedPreferences("UserProfile", 0);
    }

    public LiveData<ApiResource<ResponseBody>> loginUser(Credentials credentials){
        return new RetrofitLiveData<>(userService.loginUser(credentials));
    }


    public void storeUserSharedPref(HashMap<String, String> userData){
        Log.d("CHECK", "storeUserSharedPref: I run");
        SharedPreferences.Editor editor = userProfile.edit();

        editor.putString("USER_ID",userData.get("id"));
        editor.putString("USER_NAME", userData.get("username"));
        editor.putString("IS_STUDENT", userData.get("isStudent"));
        editor.putString("IS_TUTOR", userData.get("isTutor"));
    }

}
