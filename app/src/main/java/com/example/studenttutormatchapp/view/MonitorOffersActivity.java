package com.example.studenttutormatchapp.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.Adapters.MonitorOffersAdapter;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.Offer;
import com.example.studenttutormatchapp.model.pojo.Bid;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitorOffersActivity extends AppCompatActivity {

    Bid monitoringBid;
    List<Offer> offers;
    String userId;

    RecyclerView recyclerView;
    MonitorOffersAdapter adapter;

    private Context context;
    public Handler handler;
    final int delay = 10000; // 10 seconds
    private boolean killThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monitored_offers);

        killThread = false;
        context = this;

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
        adapter.notifyDataSetChanged();
        refreshBidOffers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        killThread = true;
    }

    public void refreshBidOffers(){

        handler = new Handler(Looper.myLooper()){};
        handler.postDelayed(
               new Thread (getBid()
        ), delay);
    }

    public Runnable getBid() {
        return new Runnable() {
            @Override
            public void run() {
                if (!killThread) {
                    Call<Bid> call = APIUtils.getBidService().getBid(monitoringBid.getId());
                    call.enqueue(new retrofit2.Callback<Bid>() {
                        @Override
                        public void onResponse(Call<Bid> call, Response<Bid> response) {
                            monitoringBid = response.body();
                            adapter.setBid(monitoringBid);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(context, "Refreshed Offers", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Bid> call, Throwable t) {

                        }
                    });

                    refreshBidOffers();
                }
            }

        };
    }
}
