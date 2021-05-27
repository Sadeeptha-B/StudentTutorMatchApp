package com.example.studenttutormatchapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.studenttutormatchapp.model.repositories.Preference_UserProfile;
import com.example.studenttutormatchapp.model.repositories.UserRepository;

import javax.inject.Inject;

public class DashboardViewModel extends CommonViewModel {

    @Inject
    public DashboardViewModel(UserRepository userRepository){
        super(userRepository);
    }
}
