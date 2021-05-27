package com.example.studenttutormatchapp.viewmodel;

import com.example.studenttutormatchapp.model.repositories.UserRepository;

import javax.inject.Inject;

public class ContractFormViewModel extends CommonViewModel {

    @Inject
    public ContractFormViewModel(UserRepository userRepository) {
        super(userRepository);
    }
}
