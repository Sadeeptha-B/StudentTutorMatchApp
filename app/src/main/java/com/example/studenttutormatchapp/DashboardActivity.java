package com.example.studenttutormatchapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.model.Bid;
import com.example.studenttutormatchapp.model.Subject;
import com.example.studenttutormatchapp.model.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.SubjectService;
import com.example.studenttutormatchapp.remote.UserService;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

    RecyclerView ongoingBidRecycler;
    RecyclerView.LayoutManager ongoingBidLayoutManager;
    OngoingBidsAdapter ongoingBidAdapter;

    List<OngoingBidData> ongoingBidDataList = new ArrayList<OngoingBidData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_dashboard_layout);

        apiUserInterface = APIUtils.getUserService();
        apiSubjectInterface = APIUtils.getSubjectService();

        jwtFile = getSharedPreferences("jwt", 0);
        context = this;

        try {
            decodeJWT();
            storeUserData();
            setUIElements();
            getBids();
        } catch (JSONException | UnsupportedEncodingException e) {
//            e.printStackTrace();
              Toast.makeText(context, "Please log in again", Toast.LENGTH_SHORT).show();
        }

        fillData();
    }

    public void decodeJWT() throws UnsupportedEncodingException, JSONException {
        String[] jwt = jwtFile.getString("JWT", "").split("\\.");
        byte[] decodedBytes = Base64.decode(jwt[1], Base64.URL_SAFE);
        String body = new String(decodedBytes, "UTF-8");
        jwtObject = new JSONObject(body);
        Log.i("CHECK", jwtObject.toString());
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
        Call<User> call = apiUserInterface.getStudentBids(jwtObject.getString("sub"));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                List<Bid> bids = response.body().getBids();
                for (int i=0; i< bids.size(); i++){
                    String subjectId = bids.get(i).getSubject().getId();
                    String bidTime = bids.get(i).getDateCreated();
                    getSubject(subjectId, bidTime);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("CHECK","Response:" + t.getMessage());
            }
        });
    }

    private void getSubject(String subjectId, String bidTime){
        Call<Subject> call = apiSubjectInterface.getSubject(subjectId);
        call.enqueue(new Callback<Subject>() {
            @Override
            public void onResponse(Call<Subject> call, Response<Subject> response) {
                String subjectName = response.body().getName();
                Log.d("CHECK", subjectName);
                ongoingBidDataList.add(new OngoingBidData(subjectName, bidTime));
            }

            @Override
            public void onFailure(Call<Subject> call, Throwable t) {
                Log.d("CHECK","Response:" + t.getMessage());
            }
        });
    }

    private void fillData(){
        ongoingBidAdapter.setData(ongoingBidDataList);
        ongoingBidAdapter.notifyDataSetChanged();
    }

    public void setUIElements() throws JSONException {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle navToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string,R.string.close_string);
        drawerLayout.addDrawerListener(navToggle);
        navToggle.syncState();

        toolbar.setTitle("Welcome, " + jwtObject.getString("username"));

        navigationView = findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();

        if (jwtObject.getBoolean("isStudent")){
            menuNav.setGroupVisible(R.id.studentMenuItems, true);
            menuNav.setGroupVisible(R.id.tutorMenuItems, false);
        }

        if (jwtObject.getBoolean("isTutor")){
            menuNav.setGroupVisible(R.id.tutorMenuItems, true);
            menuNav.setGroupVisible(R.id.studentMenuItems, false);
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
                        jwtFileEditor.clear().apply();
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ongoingBidRecycler = findViewById(R.id.ongoingBidsRecycler);
        ongoingBidLayoutManager = new LinearLayoutManager(context);
        ongoingBidRecycler.setLayoutManager(ongoingBidLayoutManager);
        ongoingBidAdapter = new OngoingBidsAdapter(context);
        ongoingBidRecycler.setAdapter(ongoingBidAdapter);
    }

    /*Navigation Menu callback */
    private void bidFormPage(){
        Intent activity = new Intent(this, BidFormActivity.class);
        startActivity(activity);
    }

    private void findBidRequests(){
        Intent activity = new Intent(this, FindBidsActivity.class);
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
