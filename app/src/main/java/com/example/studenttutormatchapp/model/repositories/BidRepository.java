package com.example.studenttutormatchapp.model.repositories;


import androidx.lifecycle.LiveData;

import com.example.studenttutormatchapp.model.pojo.Bid;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.BidService;
import com.example.studenttutormatchapp.remote.response.ApiResource;
import com.example.studenttutormatchapp.remote.response.CallAdapter;

import javax.inject.Inject;

public class BidRepository implements Repository.BidInterface{
    private BidService bidService = APIUtils.getBidService();


    @Inject
    public BidRepository(){

    }

    public LiveData<ApiResource<Bid>> createBid(Bid bid){
        return new CallAdapter<>(bidService.createBid(bid)).getLiveData();
    }

}
