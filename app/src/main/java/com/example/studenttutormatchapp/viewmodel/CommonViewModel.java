package com.example.studenttutormatchapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.studenttutormatchapp.model.repositories.Preference_UserProfile;
import com.example.studenttutormatchapp.model.repositories.UserRepository;

public class CommonViewModel extends ViewModel {
    private UserRepository userRepository;


    public CommonViewModel(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Preference_UserProfile getUserData(){
        return userRepository.getUserSharedPref();
    }

    protected UserRepository getUserRepository() {
        return userRepository;
    }
}
