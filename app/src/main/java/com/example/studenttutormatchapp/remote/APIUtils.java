package com.example.studenttutormatchapp.remote;

import com.example.studenttutormatchapp.BuildConfig;

public class APIUtils {


    private APIUtils(){

    };
    String API_KEY = BuildConfig.FIT3077_API_KEY;

    public static final String API_URL = "https://fit3077.com/api/v1";

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

}
