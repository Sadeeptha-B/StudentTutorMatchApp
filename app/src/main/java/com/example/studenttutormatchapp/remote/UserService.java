package com.example.studenttutormatchapp.remote;

import com.example.studenttutormatchapp.Credentials;
import com.example.studenttutormatchapp.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("user/")
    Call<List<User>> getUsers();

    @POST("add/")
    Call<User> addUser(@Body User user);

    @PUT("update/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);

    @DELETE("delete/{id}")
    Call<User> deleteUser(@Path("id") int id);

//    @POST("user/login")
//    Call<ResponseBody> loginUser(@Header("Authorization") String key, @Body Credentials user);

    @POST("user/login")
    Call<ResponseBody> loginUser(@Body Credentials user);

}