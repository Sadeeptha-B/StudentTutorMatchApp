package com.example.studenttutormatchapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.studenttutormatchapp.model.pojo.Bid;
import com.example.studenttutormatchapp.model.pojo.Contract;
import com.example.studenttutormatchapp.model.pojo.Subject;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.model.repositories.ContractRepository;
import com.example.studenttutormatchapp.model.repositories.SubjectRepository;
import com.example.studenttutormatchapp.model.repositories.UserRepository;
import com.example.studenttutormatchapp.remote.response.ApiResource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DashboardViewModel extends CommonViewModel {
    private SubjectRepository subjectRepository;

    private LiveData<ApiResource<User>> bidLiveData;
    private LiveData<ApiResource<List<Contract>>> contractLiveData;

    @Inject
    public DashboardViewModel(UserRepository userRepository, ContractRepository contractRepository, SubjectRepository subjectRepository){
        super(userRepository);
        bidLiveData = userRepository.getStudentBids();
        contractLiveData = contractRepository.getContracts();
        this.subjectRepository = subjectRepository;
    }

    public LiveData<ApiResource<User>> getBidLiveData(){
        return bidLiveData;
    }

    public LiveData<ApiResource<List<Contract>>> getContractLiveData(){ return contractLiveData;}

}
