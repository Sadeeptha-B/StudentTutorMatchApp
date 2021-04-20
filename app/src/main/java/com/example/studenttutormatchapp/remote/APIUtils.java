package com.example.studenttutormatchapp.remote;

public class APIUtils {

    private APIUtils(){

    };

    public static final String API_URL = "";

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

}
