package com.example.studenttutormatchapp;

import android.app.Activity;

import com.example.studenttutormatchapp.dagger.AppModule;
import com.example.studenttutormatchapp.dagger.ViewModelModule;
import com.example.studenttutormatchapp.view.ContractFormActivity;
import com.example.studenttutormatchapp.view.DashboardActivity;
import com.example.studenttutormatchapp.view.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules={AppModule.class, ViewModelModule.class})
@Singleton
public interface AppComponent {
    void inject(LoginActivity loginActivity);
    void inject(DashboardActivity dashboardActivity);
    void inject(ContractFormActivity contractFormActivity);
}
