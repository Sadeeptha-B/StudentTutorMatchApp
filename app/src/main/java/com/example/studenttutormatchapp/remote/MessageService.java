package com.example.studenttutormatchapp.remote;

import com.example.studenttutormatchapp.model.Bid;
import com.example.studenttutormatchapp.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {

    @GET("message/")
    Call<List<Message>> getMessages();

    @POST("message/")
    Call<Message> createMessage(@Body Message message);

    @GET("message/{MessageId}")
    Call<Message> getMessage(@Path("messageId") String id);

    @PATCH("message/{MessageId}")
    Call<Message> updateMessage(@Path("messageId") String id, Message message);

    @DELETE("message/{messageId}")
    void deleteMessage(@Path("messageId") String id);
}
