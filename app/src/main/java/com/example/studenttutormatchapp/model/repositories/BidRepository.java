package com.example.studenttutormatchapp.model.repositories;


import androidx.lifecycle.LiveData;

import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.BidService;
import com.example.studenttutormatchapp.remote.response.ApiResource;

import javax.inject.Inject;

public class BidRepository implements Repository.BidInterface{
    private BidService bidService = APIUtils.getBidService();


    @Inject
    public BidRepository(){

    }

//    public LiveData<ApiResource>


}
