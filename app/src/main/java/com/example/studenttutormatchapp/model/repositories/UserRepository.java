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
    Preference_UserProfile preference_userProfile;

    @Inject
    public UserRepository(Context context){
        preference_userProfile = Preference_UserProfile.getInstance(context);
    }

    public LiveData<ApiResource<ResponseBody>> loginUser(Credentials credentials){
        return new RetrofitLiveData<>(userService.loginUser(credentials));
    }

    public void storeUserSharedPref(HashMap<String, String> userData){
        Log.d("CHECK", "storeUserSharedPref: I run");

        preference_userProfile.putUserId(userData.get("id"));
        preference_userProfile.putUsername(userData.get("username"));
        preference_userProfile.putIsStudent(Boolean.parseBoolean(userData.get("isStudent")));
        preference_userProfile.putIsTutor(Boolean.parseBoolean(userData.get("isTutor")));
    }

    public Preference_UserProfile getUserSharedPref(){
        return preference_userProfile;
    }

}
