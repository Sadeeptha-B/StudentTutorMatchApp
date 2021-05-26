package com.example.studenttutormatchapp;

import com.example.studenttutormatchapp.model.repositories.UserRepository;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules={AppModule.class})
@Singleton
public interface AppComponent {
    void inject(UserRepository userRepository);

}
