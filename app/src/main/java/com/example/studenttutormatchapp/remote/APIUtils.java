package com.example.studenttutormatchapp.remote;

import com.example.studenttutormatchapp.BuildConfig;
import com.example.studenttutormatchapp.model.Contract;

public class APIUtils {

    private static String API_KEY = BuildConfig.FIT3077_API_KEY;
    private static final String API_URL = "https://fit3077.com/api/v1/";

    public static UserService getUserService(){

        return RetrofitClient.getClient(API_URL, API_KEY).create(UserService.class);
    }

    public static BidService getBidService(){
        return RetrofitClient.getClient(API_URL, API_KEY).create(BidService.class);
    }

    public static MessageService getMessageService(){
        return RetrofitClient.getClient(API_URL, API_KEY).create(MessageService.class);
    }

    public static SubjectService getSubjectService(){
        return RetrofitClient.getClient(API_URL, API_KEY).create(SubjectService.class);
    }

    public static ContractService getContractService(){
        return RetrofitClient.getClient(API_URL, API_KEY).create(ContractService.class);
    }
}
