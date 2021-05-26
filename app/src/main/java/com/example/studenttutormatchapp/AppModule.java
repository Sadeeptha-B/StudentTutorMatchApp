package com.example.studenttutormatchapp;

import android.content.Context;

import androidx.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context context;

    public AppModule(@NonNull Context context){
        this.context = context;
    }

    @Singleton
    @Provides
    @NonNull
    public Context providerContext(){
        return context;
    }
}
