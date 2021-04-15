package com.example.studenttutormatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView user;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.userName);
        password = findViewById(R.id.Password);
    }

    public void login(View v){
        //TODO implement string comparison to validate user name and password
        Intent activity = new Intent(this, DashboardActivity.class);
        startActivity(activity);
    }
}