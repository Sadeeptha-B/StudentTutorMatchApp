package com.example.studenttutormatchapp.model.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.studenttutormatchapp.helpers.Credentials;
import com.example.studenttutormatchapp.model.pojo.User;
//import com.example.studenttutormatchapp.model.sharedpref.Preference_UserProfile;
import com.example.studenttutormatchapp.model.sharedpref.Preference_UserProfile;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.UserService;
import com.example.studenttutormatchapp.remote.response.ApiResource;
import com.example.studenttutormatchapp.remote.response.RetrofitLiveData;


import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;

@Singleton
public class UserRepository implements Repository.UserInterface {
    private UserService userService = APIUtils.getUserService();

    private static UserRepository instance;
    private final MutableLiveData<Integer> loginAttempt = new MutableLiveData<>(0);

//    private final LiveData<> test = Transformations.map()

    @Inject
    private UserRepository(Context context){

//        userSharedPreferences
        Preference_UserProfile userProfile = Preference_UserProfile.getInstance(context);
    }

    public static UserRepository getInstance(){
        if (instance == null){
            instance = new UserRepository();
        }
        return instance;
    }


    public LiveData<ApiResource<ResponseBody>> loginUser(Credentials credentials){
        loginAttempt.setValue(loginAttempt.getValue() + 1);
        return new RetrofitLiveData<>(userService.loginUser(credentials));
    }

}
