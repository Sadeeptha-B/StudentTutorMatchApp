package com.example.studenttutormatchapp;

import android.app.Activity;

import com.example.studenttutormatchapp.dagger.AppModule;
import com.example.studenttutormatchapp.dagger.ViewModelModule;
import com.example.studenttutormatchapp.view.BidFormActivity;
import com.example.studenttutormatchapp.view.ChatActivity;
import com.example.studenttutormatchapp.view.ContractFormActivity;
import com.example.studenttutormatchapp.view.DashboardActivity;
import com.example.studenttutormatchapp.view.FindBidsActivity;
import com.example.studenttutormatchapp.view.ListOffersActivity;
import com.example.studenttutormatchapp.view.LoginActivity;
import com.example.studenttutormatchapp.view.MakeOfferFormActivity;
import com.example.studenttutormatchapp.view.MessageListActivity;
import com.example.studenttutormatchapp.view.MonitorDashboardActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules={AppModule.class, ViewModelModule.class})
@Singleton
public interface AppComponent {
    void inject(LoginActivity loginActivity);
    void inject(DashboardActivity dashboardActivity);
    void inject(ContractFormActivity contractFormActivity);
    void inject(BidFormActivity bidFormActivity);
    void inject(ChatActivity chatActivity);
    void inject(FindBidsActivity findBidsActivity);
    void inject(ListOffersActivity listOffersActivity);
    void inject(MakeOfferFormActivity makeOfferFormActivity);
    void inject(MessageListActivity messageListActivity);
    void inject(MonitorDashboardActivity monitorDashboardActivity);

}
