package com.example.studenttutormatchapp;

import android.app.Application;

public class MyApplication extends Application {

    private static AppComponent appComponent;

    public static AppComponent getAppComponent(){
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = buildComponent();
    }

    public AppComponent buildComponent(){
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
