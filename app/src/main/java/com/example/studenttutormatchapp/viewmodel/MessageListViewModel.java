package com.example.studenttutormatchapp.viewmodel;

import com.example.studenttutormatchapp.model.repositories.UserRepository;

import javax.inject.Inject;

public class MessageListViewModel extends CommonViewModel{

    @Inject
    public MessageListViewModel(UserRepository userRepository){
        super(userRepository);
    }
}
