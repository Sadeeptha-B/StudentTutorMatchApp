package com.example.studenttutormatchapp.view;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.Adapters.MonitorOffersAdapter;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.Offer;
import com.example.studenttutormatchapp.model.pojo.Bid;
import com.google.gson.Gson;

import java.util.List;

public class MonitorOffersActivity extends AppCompatActivity {

    Bid monitoringBid;
    List<Offer> offers;
    String userId;

    RecyclerView recyclerView;
    MonitorOffersAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monitored_offers);

        Gson gson = new Gson();
        String bidString = getIntent().getExtras().getString("bid");
        monitoringBid = gson.fromJson(bidString, Bid.class);
        userId = getIntent().getExtras().getString("userId");

        offers = monitoringBid.getAdditionalInfo().getOffers();

        recyclerView = findViewById(R.id.monitoredOffersRecycler);
        LinearLayoutManager bidLayoutManager =  new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(bidLayoutManager);

        adapter = new MonitorOffersAdapter();
        adapter.setUserId(userId);
        recyclerView.setAdapter(adapter);
        adapter.setBid(monitoringBid);
        Log.d("OFFER", String.valueOf(offers.size()));
        adapter.notifyDataSetChanged();

    }
}
