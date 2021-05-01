package com.example.studenttutormatchapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.example.studenttutormatchapp.ListOffersAdapter;
import com.example.studenttutormatchapp.Offer;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.model.Bid;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.BidService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOffersActivity extends AppCompatActivity {

    String bidId;
    String userId;

    ListOffersAdapter adapter;
    Bid bid;
    List<Offer> offers;

    BidService APIBidInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offers);

        APIBidInterface = APIUtils.getBidService();

        Intent page = getIntent();
        Bundle bundle = page.getExtras();

        bidId = bundle.getString("bidId");
        userId = bundle.getString("userId");
        String subjectDescription = bundle.getString("subject");

        Toolbar toolbar = findViewById(R.id.OffersToolbar);
        toolbar.setTitle("Offers on "+ subjectDescription);

    }

    public void getBid(){
        Call<Bid> call = APIBidInterface.getBid(bidId);

        call.enqueue(new Callback<Bid>() {
            @Override
            public void onResponse(Call<Bid> call, Response<Bid> response) {
                if (response.isSuccessful()){
                    bid = response.body();
                    adapter = new ListOffersAdapter(bid.getType(), bidId, userId);
                    offers = bid.getAdditionalInfo().getOffers();
                    adapter.setOffers(offers);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Bid> call, Throwable t) {

            }
        });
    }
}