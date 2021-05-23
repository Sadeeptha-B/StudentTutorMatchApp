package com.example.studenttutormatchapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.controller.MonitorPageController;

public class MonitorPageView implements MonitorPageViewInterface {

    private View rootView;

    RecyclerView recyclerView;
    RecyclerView.Adapter recylerViewAdapter;
    MonitorPageController controller;



    public MonitorPageView(Context context, ViewGroup viewGroup){
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_monitor_dashboard, viewGroup);
        controller = new MonitorPageController(this);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void bindDataToView() {

    }
}
