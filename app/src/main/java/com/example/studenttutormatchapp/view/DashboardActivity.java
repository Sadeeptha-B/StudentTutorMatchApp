package com.example.studenttutormatchapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.example.studenttutormatchapp.viewmodel.DashboardViewModel;
import com.example.studenttutormatchapp.viewmodel.ViewModelFactory;
import com.google.android.material.navigation.NavigationView;

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

    /* API interface */
    UserService apiUserInterface;
    SubjectService apiSubjectInterface;

    /*Shared Pref*/
    SharedPreferences jwtFile;
    SharedPreferences.Editor jwtFileEditor;

    Context context;
    JSONObject jwtObject;

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

        apiUserInterface = APIUtils.getUserService();
        apiSubjectInterface = APIUtils.getSubjectService();

        jwtFile = getSharedPreferences("jwt", 0);
        jwtFileEditor = jwtFile.edit();
        context = this;

        ((MyApplication) getApplication()).getAppComponent().inject(this);
        dashboardViewModel = new ViewModelProvider(this, viewModelFactory).get(DashboardViewModel.class);

        Log.d("CHECK",dashboardViewModel.getUserData().getUsername());

        try {
            decodeJWT();
            storeUserData();
            setUIElements();
            getBids();
            getContracts();
        } catch (JSONException | UnsupportedEncodingException e) {
//            e.printStackTrace();
            Toast.makeText(context, "You are not logged in. Please try again", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Temporary fix
        ongoingBidDataList.clear();
        contractAdapter.clearContracts();

        try {
            getBids();
            getContracts();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void decodeJWT() throws UnsupportedEncodingException, JSONException {
        String[] jwt = jwtFile.getString("JWT", "").split("\\.");
        byte[] decodedBytes = Base64.decode(jwt[1], Base64.URL_SAFE);
        String body = new String(decodedBytes, "UTF-8");
        jwtObject = new JSONObject(body);
    }

    public void storeUserData() throws JSONException {
        // User Id will be stored in the main shared pref file
        SharedPreferences userIdFile = getSharedPreferences("id", 0);
        SharedPreferences.Editor userIdFileEditor = userIdFile.edit();

        userIdFileEditor.putString("USER_ID", jwtObject.getString("sub"));
        userIdFileEditor.putString("USERNAME", jwtObject.getString("username"));
        userIdFileEditor.putBoolean("IS_STUDENT", jwtObject.getBoolean("isStudent"));
        userIdFileEditor.putBoolean("IS_TUTOR", jwtObject.getBoolean("isTutor"));
        userIdFileEditor.apply();
    }

    public void getBids() throws JSONException {
        Call<User> call = apiUserInterface.getStudentBids(dashboardViewModel.getUserData().getUserId());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                List<Bid> bids = response.body().getBids();
                for (int i=0; i< bids.size(); i++){
                    String subjectId = bids.get(i).getSubject().getId();
                    String bidTime = bids.get(i).getDateCreated();
                    String bidId = bids.get(i).getId();
                    String bidType = bids.get(i).getType();
                    getSubject(subjectId, bidTime, bidId, bidType);
                }
                Log.d("CHECK", bids.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("CHECK","Response:" + t.getMessage());
            }
        });
    }

    private void getSubject(String subjectId, String bidTime, String bidId, String bidType){
        ZonedDateTime zdtime = ZonedDateTime.parse(bidTime);
        final String formattedBidTime = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm").format(zdtime);

        Call<Subject> call = apiSubjectInterface.getSubject(subjectId);
        call.enqueue(new Callback<Subject>() {
            @Override
            public void onResponse(Call<Subject> call, Response<Subject> response) {
                if(response.isSuccessful()){
                    String subjectStr = response.body().getName() + " | " + response.body().getDescription();
                    ongoingBidDataList.add(new OngoingBidData(subjectStr, formattedBidTime, bidId, bidType));
                }
                bidAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Subject> call, Throwable t) {
                Log.d("CHECK","Response:" + t.getMessage());
            }
        });
    }

    public void getContracts(){
        Call<List<Contract>> call = APIUtils.getContractService().getContracts();

        call.enqueue(new Callback<List<Contract>>() {
            @Override
            public void onResponse(Call<List<Contract>> call, Response<List<Contract>> response) {
                if(response.isSuccessful()) {
                    contractAdapter.setContracts(response.body());
                    contractAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Contract>> call, Throwable t) {

            }
        });
    }

    public void setUIElements() throws JSONException {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle navToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string,R.string.close_string);
        drawerLayout.addDrawerListener(navToggle);
        navToggle.syncState();

        toolbar.setTitle("Welcome, " + dashboardViewModel.getUserData().getUsername());

        navigationView = findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();

        if (jwtObject.getBoolean("isStudent")){
            menuNav.setGroupVisible(R.id.studentMenuItems, true);
            menuNav.setGroupVisible(R.id.tutorMenuItems, false);
        } else if (jwtObject.getBoolean("isTutor")){
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
                        try {
                            findBidRequests();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.signout:
                        jwtFileEditor.clear().apply();
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

        bidAdapter = new OngoingBidsAdapter(context, dashboardViewModel.getUserData().getUserId());
        bidAdapter.setData(ongoingBidDataList);
        bidRecycler.setAdapter(bidAdapter);

        RecyclerView contractRecycler = findViewById(R.id.contractRecycler);
        RecyclerView.LayoutManager contractLayoutManager = new LinearLayoutManager(context);
        contractRecycler.setLayoutManager(contractLayoutManager);

        contractAdapter = new ContractListAdapter(context, jwtObject.getBoolean("isStudent"));
        contractAdapter.setUserId(dashboardViewModel.getUserData().getUserId());
        contractRecycler.setAdapter(contractAdapter);

    }

    /*Navigation Menu callback */
    private void bidFormPage(){
        Intent activity = new Intent(this, BidFormActivity.class);
        startActivity(activity);
    }

    private void findBidRequests() throws JSONException {
        Intent activity = new Intent(this, FindBidsActivity.class);
        activity.putExtra("user_id", dashboardViewModel.getUserData().getUserId());
        startActivity(activity);
    }

    private void monitoringDashboard(){
        Intent activity = new Intent(this, MonitorDashboardActivity.class);
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
