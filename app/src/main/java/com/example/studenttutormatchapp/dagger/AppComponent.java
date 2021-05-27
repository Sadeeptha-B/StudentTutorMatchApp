package com.example.studenttutormatchapp.dagger;

import com.example.studenttutormatchapp.view.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules={AppModule.class, ViewModelModule.class})
@Singleton
public interface AppComponent {
    void inject(LoginActivity loginActivity);
}
