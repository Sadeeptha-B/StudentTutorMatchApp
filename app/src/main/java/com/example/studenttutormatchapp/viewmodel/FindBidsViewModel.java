package com.example.studenttutormatchapp.viewmodel;

import com.example.studenttutormatchapp.model.repositories.UserRepository;

import javax.inject.Inject;

public class FindBidsViewModel extends CommonViewModel{
    @Inject
    public FindBidsViewModel(UserRepository userRepository) {
        super(userRepository);
    }
}
