package com.example.studenttutormatchapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.Adapters.ContractListAdapter;
import com.example.studenttutormatchapp.MyApplication;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.OngoingBidData;
import com.example.studenttutormatchapp.Adapters.OngoingBidsAdapter;
import com.example.studenttutormatchapp.model.pojo.Bid;
import com.example.studenttutormatchapp.model.pojo.Contract;
import com.example.studenttutormatchapp.model.pojo.Subject;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.SubjectService;
import com.example.studenttutormatchapp.remote.dao.UserService;
import com.example.studenttutormatchapp.remote.response.ApiResource;
import com.example.studenttutormatchapp.viewmodel.DashboardViewModel;
import com.example.studenttutormatchapp.viewmodel.ViewModelFactory;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    /* UI Elements */
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Context context;

    RecyclerView bidRecycler;
    RecyclerView.LayoutManager bidLayoutManager;
    OngoingBidsAdapter bidAdapter;

    ContractListAdapter contractAdapter;

    @Inject
    ViewModelFactory viewModelFactory;
    DashboardViewModel dashboardViewModel;

    List<OngoingBidData> ongoingBidDataList = new ArrayList<OngoingBidData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_dashboard_layout);

        context = this;

        ((MyApplication) getApplication()).getAppComponent().inject(this);
        dashboardViewModel = new ViewModelProvider(this, viewModelFactory).get(DashboardViewModel.class);
        setUIElements();
        getLifecycle().addObserver(dashboardViewModel);

        setObservers();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("CHECK", "I run");
        dashboardViewModel.getBidLiveData().removeObservers(this);
        dashboardViewModel.getContractLiveData().removeObservers(this);
        ongoingBidDataList.clear();
        setObservers();
    }

    public void setObservers(){
        dashboardViewModel.getBidLiveData().observe(this, new Observer<ApiResource<User>>() {
            @Override
            public void onChanged(ApiResource<User> userApiResource) {
                switch(userApiResource.getStatus()){
                    case SUCCESS:
                        onBidsObtained(userApiResource.getData().getBids());
                        break;
                    case ERROR:
                        break;
                    case LOADING:
                        break;
                }
            }
        });

        dashboardViewModel.getContractLiveData().observe(this, new Observer<ApiResource<List<Contract>>>() {
            @Override
            public void onChanged(ApiResource<List<Contract>> listApiResource) {
                switch(listApiResource.getStatus()){
                    case SUCCESS:
                        List<Contract> contracts = getValidContracts(listApiResource.getData(), dashboardViewModel.getUserData().getUserId());
                        contractAdapter.setContracts(contracts);
                        contractAdapter.notifyDataSetChanged();
                        checkContract(contracts);
                        break;
                    case ERROR:
                        break;
                    case LOADING:
                        break;
                }
            }
        });
    }



    public List<Contract> getValidContracts(List<Contract> contracts, String userId) {
        List<Contract> list = new ArrayList<>();
        for (int i = 0; i < contracts.size(); i++){
            Contract contract = contracts.get(i);
            if ((contract.getFirstParty().getId().equals(userId)) || (contract.getSecondParty().getId().equals(userId)))
                list.add(contract);
        }
        return list;

    }

    public void checkContract(List<Contract> contracts) {
        ZonedDateTime todaysDate = ZonedDateTime.now();
        View dashboard = findViewById(R.id.DashboardConstraintLayout);
        for(int i = 0; i < contracts.size(); i++){
            Contract contract = contracts.get(i);
            checkContractExpiry(contract, todaysDate, dashboard);
        }
    }

    public void checkContractExpiry(Contract contract, ZonedDateTime todaysDate, View layout) {
        ZonedDateTime monthBeforeExpiry = contract.getMonthBeforeExpiry();
        String otherParty;
        if (dashboardViewModel.getUserData().getIsStudent())
            otherParty = contract.getFirstParty().getUserName();
        else
            otherParty = contract.getSecondParty().getUserName();

        String message = "Your contract with " + otherParty + " expires on " + contract.getExpiryDateString();

        if (monthBeforeExpiry.isBefore(todaysDate) && todaysDate.isBefore(monthBeforeExpiry.plusDays(21))) {
            Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_INDEFINITE);
            View view = snackbar.getView();
            CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();

        }
    }

    private void onBidsObtained(List<Bid> bids){
        for (int i=0; i< bids.size(); i++){
            String subjectStr = bids.get(i).getSubject().getName() + "|" + bids.get(i).getSubject().getDescription();
            String bidTime = bids.get(i).getDateCreated();
            String bidId = bids.get(i).getId();
            String bidType = bids.get(i).getType();
            ZonedDateTime zdtime = ZonedDateTime.parse(bidTime);
            final String formattedBidTime = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm").format(zdtime);
            ongoingBidDataList.add(new OngoingBidData(subjectStr, formattedBidTime, bidId, bidType));
        }
        bidAdapter.notifyDataSetChanged();
    }


    public void setUIElements()  {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle navToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string,R.string.close_string);
        drawerLayout.addDrawerListener(navToggle);
        navToggle.syncState();

        toolbar.setTitle("Welcome, " + dashboardViewModel.getUserData().getUsername());

        navigationView = findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();

        if (dashboardViewModel.getUserData().getIsStudent()){
            menuNav.setGroupVisible(R.id.studentMenuItems, true);
            menuNav.setGroupVisible(R.id.tutorMenuItems, false);
        } else if (dashboardViewModel.getUserData().getIsTutor()){
            menuNav.setGroupVisible(R.id.tutorMenuItems, true);
            menuNav.setGroupVisible(R.id.studentMenuItems, false);
            findViewById(R.id.textViewOngoingBids).setVisibility(View.GONE);
            findViewById(R.id.ongoingBidsRecycler).setVisibility(View.GONE);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.createBid:
                        bidFormPage();
                        break;
                    case R.id.findBidRequests:
                        findBidRequests();
                        break;
                    case R.id.signout:
                        dashboardViewModel.getUserData().clear();
                        finish();
                        break;
                    case R.id.monitorDashboard:
                        monitoringDashboard();
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        bidRecycler = findViewById(R.id.ongoingBidsRecycler);
        bidLayoutManager = new LinearLayoutManager(context);
        bidRecycler.setLayoutManager(bidLayoutManager);
        bidRecycler.setHasFixedSize(true);

        bidAdapter = new OngoingBidsAdapter(context);
        bidAdapter.setData(ongoingBidDataList);
        bidRecycler.setAdapter(bidAdapter);

        RecyclerView contractRecycler = findViewById(R.id.contractRecycler);
        RecyclerView.LayoutManager contractLayoutManager = new LinearLayoutManager(context);
        contractRecycler.setLayoutManager(contractLayoutManager);


        contractAdapter = new ContractListAdapter(context,  dashboardViewModel.getUserData().getIsStudent());
        contractAdapter.setDate(ZonedDateTime.now());
        contractAdapter.setUserId(dashboardViewModel.getUserData().getUserId());
        contractRecycler.setAdapter(contractAdapter);
    }

    /*Navigation Menu callback */
    private void bidFormPage(){
        Intent activity = new Intent(this, BidFormActivity.class);
        startActivity(activity);
    }

    private void findBidRequests() {
        Intent activity = new Intent(this, FindBidsActivity.class);
        activity.putExtra("user_id", dashboardViewModel.getUserData().getUserId());
        startActivity(activity);
    }

    private void monitoringDashboard() {
        Intent activity = new Intent(this, MonitorDashboardActivity.class);
        activity.putExtra("userId", dashboardViewModel.getUserData().getUserId());
        startActivity(activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent activity = new Intent(this, MessageListActivity.class);
        startActivity(activity);
        return true;
    }


}
