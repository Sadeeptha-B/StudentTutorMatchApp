package com.example.studenttutormatchapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.studenttutormatchapp.Adapters.FindBidsAdapter;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.model.pojo.Bid;
import com.example.studenttutormatchapp.model.pojo.Competency;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.BidService;
import com.example.studenttutormatchapp.remote.dao.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindBidsActivity extends AppCompatActivity {

    private String userId;
    private List<Bid> bids = new ArrayList<>();
    private List<Competency> competencies;
    private List<String> subjectIds = new ArrayList<>();

    BidService APIBidInterface;
    UserService APIUserInterface;

    FindBidsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_bids);

        Intent page = getIntent();

        Bundle bundle = page.getExtras();

        userId = bundle.getString("user_id");
        APIBidInterface = APIUtils.getBidService();
        APIUserInterface = APIUtils.getUserService();

        RecyclerView recyclerView = findViewById(R.id.FindBidRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FindBidsAdapter(this);
        adapter.setUserId(userId);
        recyclerView.setAdapter(adapter);
        getBids();

    }

    public void getBids(){
        Call<List<Bid>> call = APIBidInterface.getBids();
        call.enqueue(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                if (response.isSuccessful()){
                    bids = response.body();
                    getUserCompetencies();
                }
                switch (response.code()){
                    case 401:
                        Log.d("CHECK", "Invalid API key");
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {
                Log.d("CHECK", t.getMessage());
            }
        });
    }

    public void getUserCompetencies(){
        Call<User> call = APIUserInterface.getUserSubjects(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    competencies = response.body().getCompetencies();
                    for (Competency competency: competencies){
                        subjectIds.add(competency.getSubject().getId());
                    }
                    filterBids();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void filterBids(){
        List<Bid> filteredBids = new ArrayList();
        for (int i = 0; i < bids.size(); i++){
            String subjectId = bids.get(i).getSubject().getId();

            if (subjectIds.contains(subjectId)){
                filteredBids.add(bids.get(i));
            }
        }
        adapter.setBids(filteredBids);
        adapter.notifyDataSetChanged();
    }
}