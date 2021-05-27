package com.example.studenttutormatchapp.remote.dao;


import com.example.studenttutormatchapp.model.pojo.Competency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CompetencyService {

    @GET("competency")
    Call<List<Competency>> getCompetencies();

    @GET("competency/{competencyId}")
    Call<Competency> getCompetency(@Path("competencyId") String id);

}
