package com.example.studenttutormatchapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;

import com.example.studenttutormatchapp.model.pojo.Bid;
import com.example.studenttutormatchapp.model.pojo.Contract;
import com.example.studenttutormatchapp.model.pojo.Subject;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.model.repositories.ContractRepository;
import com.example.studenttutormatchapp.model.repositories.Repository;
import com.example.studenttutormatchapp.model.repositories.SubjectRepository;
import com.example.studenttutormatchapp.model.repositories.UserRepository;
import com.example.studenttutormatchapp.remote.response.ApiResource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DashboardViewModel extends CommonViewModel implements LifecycleObserver {
    private ContractRepository contractRepository;

    private LiveData<ApiResource<User>> bidLiveData = new MutableLiveData<>();
    private LiveData<ApiResource<List<Contract>>> contractLiveData = new MutableLiveData<>();

    @Inject
    public DashboardViewModel(UserRepository userRepository, ContractRepository contractRepository){
        super(userRepository);
        this.contractRepository = contractRepository;
        bidLiveData = getUserRepository().getStudentBids();
        contractLiveData = contractRepository.getContracts();
    }

    public LiveData<ApiResource<User>> getBidLiveData(){
        return bidLiveData;
    }

    public LiveData<ApiResource<List<Contract>>> getContractLiveData(){ return contractLiveData;}



    public void refresh(){
        bidLiveData = getUserRepository().getStudentBids();
        contractLiveData = contractRepository.getContracts();
    }

}
