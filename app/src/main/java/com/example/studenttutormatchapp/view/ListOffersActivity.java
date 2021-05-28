package com.example.studenttutormatchapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.studenttutormatchapp.Adapters.ListOffersAdapter;
import com.example.studenttutormatchapp.MyApplication;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.Offer;
import com.example.studenttutormatchapp.model.pojo.Bid;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.BidService;
import com.example.studenttutormatchapp.viewmodel.ListOffersViewModel;
import com.example.studenttutormatchapp.viewmodel.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOffersActivity extends AppCompatActivity {

    String bidId;
    String userId;

    RecyclerView recyclerView;

    ListOffersAdapter adapter;
    Bid bid;
    List<Offer> offers;

    @Inject
    ViewModelFactory viewModelFactory;
    ListOffersViewModel listOffersViewModel;

    BidService APIBidInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offers);

        APIBidInterface = APIUtils.getBidService();

        ((MyApplication) getApplication()).getAppComponent().inject(this);
        listOffersViewModel = new ViewModelProvider(this, viewModelFactory).get(ListOffersViewModel.class);

        Intent page = getIntent();
        Bundle bundle = page.getExtras();

        bidId = bundle.getString("bidId");
        userId = listOffersViewModel.getUserData().getUserId();
        String subjectDescription = bundle.getString("subject");

        Toolbar toolbar = findViewById(R.id.OffersToolbar);
        toolbar.setTitle("Offers on "+ subjectDescription);

        getBid();

        recyclerView = findViewById(R.id.listOffersRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ListOffersAdapter(bidId, userId, this);
        recyclerView.setAdapter(adapter);
    }

    public void getBid(){
        Call<Bid> call = APIBidInterface.getBid(bidId);

        call.enqueue(new Callback<Bid>() {
            @Override
            public void onResponse(Call<Bid> call, Response<Bid> response) {
                if (response.isSuccessful()){
                    bid = response.body();
                    offers = bid.getAdditionalInfo().getOffers();
                    adapter.setBidType(bid.getType());
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