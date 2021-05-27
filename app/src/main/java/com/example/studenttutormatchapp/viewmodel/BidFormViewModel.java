package com.example.studenttutormatchapp.viewmodel;

import com.example.studenttutormatchapp.model.repositories.UserRepository;

import javax.inject.Inject;

public class BidFormViewModel extends CommonViewModel{
    @Inject
    public BidFormViewModel(UserRepository userRepository) {
        super(userRepository);
    }
}
