package com.example.studenttutormatchapp;

import android.app.Application;

import com.example.studenttutormatchapp.dagger.AppComponent;
import com.example.studenttutormatchapp.dagger.AppModule;

public class MyApplication extends Application {

    private AppComponent appComponent;

    public AppComponent getAppComponent(){
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = buildComponent();
    }

    public AppComponent buildComponent(){
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this.getApplicationContext()))
                .build();
    }
}
