package com.example.studenttutormatchapp.remote.response;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallAdapter<T> {
    private Call<T> call;
    private final MutableLiveData<ApiResource<T>> observable = new MutableLiveData<>();

    public CallAdapter(Call<T> call){
        this.call = call;
        observable.setValue(ApiResource.loading());
    }


    Callback<T> callback = new Callback<T>() {
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
           observable.setValue(ApiResource.create(response));
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            observable.setValue(ApiResource.failure(t.getMessage()));
        }
    };

    public LiveData<ApiResource<T>> getLiveData(){
        call.enqueue(callback);
        return observable;
    }





}
