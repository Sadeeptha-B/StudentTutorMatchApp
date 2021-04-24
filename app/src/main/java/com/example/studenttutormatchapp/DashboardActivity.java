package com.example.studenttutormatchapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class DashboardActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    SharedPreferences jwtFile;
    SharedPreferences.Editor jwtFileEditor;

    JSONObject jwtObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_dashboard_layout);

        jwtFile = getSharedPreferences("jwt", 0);
//        jwtFileEditor = jwtFile.edit();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle navToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string,R.string.close_string);

        drawerLayout.addDrawerListener(navToggle);
        navToggle.syncState();

        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.createBid:
                        openBidPage();
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
        try {
            decodeJWT();
            toolbar.setTitle(jwtObject.getString("givenName"));
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    public void openBidPage(){
        Intent activity = new Intent(this, BidFormActivity.class);
        startActivity(activity);

    }
    public void decodeJWT() throws UnsupportedEncodingException, JSONException {
        String[] jwt = jwtFile.getString("JWT", "").split("\\.");
        byte[] decodedBytes = Base64.decode(jwt[1], Base64.URL_SAFE);
        String body = new String(decodedBytes, "UTF-8");
        jwtObject = new JSONObject(body);
    }

}
