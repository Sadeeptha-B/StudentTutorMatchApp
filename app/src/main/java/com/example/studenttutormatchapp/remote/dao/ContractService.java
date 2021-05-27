package com.example.studenttutormatchapp.remote.dao;

import com.example.studenttutormatchapp.helpers.DateSignedWrapper;
import com.example.studenttutormatchapp.model.pojo.Contract;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ContractService {

    @GET("contract/")
    Call<List<Contract>> getContracts();

    @POST("contract/")
    Call<Contract> createContract(@Body Contract contract);

    @GET("contract/{contractId}")
    Call<Contract> getContract(@Path("contractId") String id);

    @PATCH("contract/{contractId}")
    Call<Contract> updateContract(@Path("contractId") String id, @Body Contract contract);

    @DELETE("contract/{contractId}")
    Call deleteContract(@Path("contractId") String id);

    @POST("contract/{contractId}/sign")
    Call<Void> signContract(@Path("contractId") String id, @Body DateSignedWrapper dateSigned);

    @POST("contract/{contractId}/terminate")
    Call<Void> terminateContract(@Path("contractId") String id);


}
