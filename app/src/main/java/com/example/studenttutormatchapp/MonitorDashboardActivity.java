package com.example.studenttutormatchapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.studenttutormatchapp.Adapters.MonitoringBidsAdapter;
import com.example.studenttutormatchapp.model.Bid;
import com.example.studenttutormatchapp.model.User;
import com.example.studenttutormatchapp.remote.APIUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitorDashboardActivity extends AppCompatActivity {

    String userId;
    RecyclerView recyclerView;
    MonitoringBidsAdapter monitoringBidsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_monitor_dashboard);

        recyclerView = findViewById(R.id.MonitoringList);
        LinearLayoutManager bidLayoutManager =  new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(bidLayoutManager);
        monitoringBidsAdapter = new MonitoringBidsAdapter();
        recyclerView.setAdapter(monitoringBidsAdapter);

        userId = getIntent().getExtras().getString("userId");

        getUserAdditionalInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getUserAdditionalInfo(){
        Call<User> userCall = APIUtils.getUserService().getUser(userId);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    getAllBids(response.body().getAdditionalInfo().getSubscribedBid());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }

        });
    }

    public void getAllBids(List<String> bidIds){
        for (int i = 0; i < bidIds.size(); i++){
            Call<Bid> call = APIUtils.getBidService().getBid(bidIds.get(i));

            call.enqueue(new Callback<Bid>() {
                @Override
                public void onResponse(Call<Bid> call, Response<Bid> response) {
                    monitoringBidsAdapter.addMonitoringBid(response.body());
                    monitoringBidsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Bid> call, Throwable t) {

                }
            });
        }

    }




}