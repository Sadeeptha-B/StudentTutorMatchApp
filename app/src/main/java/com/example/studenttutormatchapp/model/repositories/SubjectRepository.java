package com.example.studenttutormatchapp.model.repositories;

import androidx.lifecycle.LiveData;

import com.example.studenttutormatchapp.model.pojo.Subject;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.SubjectService;
import com.example.studenttutormatchapp.remote.response.ApiResource;
import com.example.studenttutormatchapp.remote.response.CallAdapter;
import com.example.studenttutormatchapp.remote.response.RetrofitLiveData;

import javax.inject.Inject;

public class SubjectRepository implements Repository.SubjectInterface{
    private SubjectService subjectService = APIUtils.getSubjectService();

    @Inject
    public SubjectRepository() {
    }

    public LiveData<ApiResource<Subject>> getSubject(String id){
        return new CallAdapter<>(subjectService.getSubject(id)).getLiveData();
    }
}
