package com.example.studenttutormatchapp.model.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.studenttutormatchapp.helpers.Credentials;
import com.example.studenttutormatchapp.remote.response.ApiResource;

import okhttp3.ResponseBody;

public interface Repository {
    interface UserInterface{
        LiveData<ApiResource<ResponseBody>> loginUser(Credentials credentials);
    }

    interface BidInterface{
    }

    interface SubjectInterface{
    }

    interface ContractInterface{
    }

    interface MessageInterface{
    }



}
