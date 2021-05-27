package com.example.studenttutormatchapp.model.repositories;

import androidx.lifecycle.LiveData;

import com.example.studenttutormatchapp.model.pojo.Contract;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.ContractService;
import com.example.studenttutormatchapp.remote.response.ApiResource;
import com.example.studenttutormatchapp.remote.response.CallAdapter;
import com.example.studenttutormatchapp.remote.response.RetrofitLiveData;

import java.util.List;

import javax.inject.Inject;

public class ContractRepository implements Repository.ContractInterface {
    private ContractService contractService = APIUtils.getContractService();

    @Inject
    public ContractRepository(){
    }

    public LiveData<ApiResource<List<Contract>>> getContracts(){
        return new CallAdapter<>(contractService.getContracts()).getLiveData();
    }

}
