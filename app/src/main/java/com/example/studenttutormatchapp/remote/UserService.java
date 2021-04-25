package com.example.studenttutormatchapp.remote;

import com.example.studenttutormatchapp.Credentials;
import com.example.studenttutormatchapp.model.Competency;
import com.example.studenttutormatchapp.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("user/")
    Call<List<User>> getUsers();

    @GET("user/{id}")
    Call<User> getUser(@Path("id") String id);

    @POST("add/")
    Call<User> addUser(@Body User user);

    @PUT("update/{id}")
    Call<User> updateUser(@Path("id") String id, @Body User user);

    @DELETE("delete/{id}")
    Call<User> deleteUser(@Path("id") String id);

    @POST("user/login?jwt=true")
    Call<ResponseBody> loginUser(@Body Credentials user);

    @GET("user/{id}?fields=competencies.subject")
    Call<User> getStudentSubject(@Path("id") String id);

}