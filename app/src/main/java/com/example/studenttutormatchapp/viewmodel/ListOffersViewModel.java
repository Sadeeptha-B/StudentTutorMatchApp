package com.example.studenttutormatchapp.viewmodel;

import com.example.studenttutormatchapp.model.repositories.BidRepository;
import com.example.studenttutormatchapp.model.repositories.UserRepository;

import javax.inject.Inject;

public class ListOffersViewModel extends CommonViewModel {
    private BidRepository bidRepository;


    @Inject
    public ListOffersViewModel(UserRepository userRepository, BidRepository bidRepository) {
        super(userRepository);
        this.bidRepository = bidRepository;
    }



}
