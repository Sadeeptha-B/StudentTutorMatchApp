package com.example.studenttutormatchapp.remote;


import com.example.studenttutormatchapp.model.Subject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SubjectService {
    @GET("subject/")
    Call<List<Subject>> getSubjects();

    @POST("subject/")
    Call<Subject> createSubject(@Body Subject subject);

    @GET("subject/{subjectId}")
    Call<Subject> getSubject(@Path("subjectId") String id);

    @PATCH("subject/{subjectId}")
    Call<Subject> updateSubject(@Path("subjectId") String id, Subject subject);

    @DELETE("subject/{subjectId}")
    void deleteSubject(@Path("subjectId") String id);
}
