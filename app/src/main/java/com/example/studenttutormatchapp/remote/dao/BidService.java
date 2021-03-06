package com.example.studenttutormatchapp.remote.dao;

import com.example.studenttutormatchapp.helpers.BidAdditionalInfoWrapper;
import com.example.studenttutormatchapp.helpers.DateClosedDownWrapper;
import com.example.studenttutormatchapp.model.pojo.Bid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BidService {

    @GET("bid/")
    Call<List<Bid>> getBids();

    @POST("bid/")
    Call<Bid> createBid(@Body Bid bid);

    @GET("bid/{bidId}")
    Call<Bid> getBid(@Path("bidId") String id);

    @GET("bid/{bidId}?fields=messages")
    Call<Bid> getBidWithMessages(@Path("bidId") String id);

    @PATCH("bid/{bidId}/")
    Call<Bid> updateBid(@Path("bidId") String id,@Body BidAdditionalInfoWrapper additionalInfo);

    @DELETE("bid/{bidID}")
    Call deleteBid(@Path("bidId") String id);

    @POST("bid/{bidId}/close-down")
    Call<Void> closeDownBid(@Path("bidId") String id, @Body DateClosedDownWrapper dateClosedDown);
}
