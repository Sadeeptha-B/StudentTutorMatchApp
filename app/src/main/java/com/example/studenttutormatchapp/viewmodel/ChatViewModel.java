package com.example.studenttutormatchapp.viewmodel;

import com.example.studenttutormatchapp.model.repositories.UserRepository;

import javax.inject.Inject;

public class ChatViewModel extends CommonViewModel{
    @Inject
    public ChatViewModel(UserRepository userRepository) {
        super(userRepository);
    }
}
