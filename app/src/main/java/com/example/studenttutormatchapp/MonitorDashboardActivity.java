package com.example.studenttutormatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toolbar;

import com.example.studenttutormatchapp.view.MonitorPageView;

public class MonitorDashboardActivity extends AppCompatActivity {

    MonitorPageView monitorPageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        monitorPageView = new MonitorPageView(this, null);
        setContentView(monitorPageView.getRootView());
    }

    @Override
    protected void onResume() {
        super.onResume();
        monitorPageView.bindDataToView();
    }
}