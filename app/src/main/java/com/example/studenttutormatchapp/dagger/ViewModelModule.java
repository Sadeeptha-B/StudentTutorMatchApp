package com.example.studenttutormatchapp.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.studenttutormatchapp.viewmodel.BidFormViewModel;
import com.example.studenttutormatchapp.viewmodel.ChatViewModel;
import com.example.studenttutormatchapp.viewmodel.ContractFormViewModel;
import com.example.studenttutormatchapp.viewmodel.DashboardViewModel;
import com.example.studenttutormatchapp.viewmodel.FindBidsViewModel;
import com.example.studenttutormatchapp.viewmodel.ListOffersViewModel;
import com.example.studenttutormatchapp.viewmodel.MessageListViewModel;
import com.example.studenttutormatchapp.viewmodel.ViewModelFactory;
import com.example.studenttutormatchapp.viewmodel.LoginViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(BidFormViewModel.class)
    abstract ViewModel bidFormViewModel(BidFormViewModel bidFormViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel.class)
    abstract ViewModel chatViewModel(ChatViewModel chatViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ContractFormViewModel.class)
    abstract ViewModel contractFormViewModel(ContractFormViewModel contractFormViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel.class)
    abstract ViewModel dashboardViewModel(DashboardViewModel dashboardViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FindBidsViewModel.class)
    abstract ViewModel findBidsViewModel(FindBidsViewModel findBidsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ListOffersViewModel.class)
    abstract ViewModel listOffersViewModel(ListOffersViewModel listOffersViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel loginViewModel(LoginViewModel loginViewModel);

    //MakeOfferFormViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MessageListViewModel.class)
    abstract ViewModel MessageListViewModel(MessageListViewModel messageListViewModel);

    //MonitorDashboardViewModel

    //MonitorOffersViewModel
}
