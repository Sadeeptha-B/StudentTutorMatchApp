package com.example.studenttutormatchapp.model.repositories;

import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.skydoves.preferenceroom.PreferenceFunction;

@PreferenceEntity
public class UserProfile {
    @KeyName("userId") protected final String userId = "placeholder";
    @KeyName("username") protected final String username = "placeholder";
    @KeyName("isStudent") protected final boolean isStudent = false;
    @KeyName("isTutor") protected final boolean isTutor = false;

    @PreferenceFunction("userId")
    public String putId(String userId){
        return userId;
    }

    @PreferenceFunction("userId")
    public String getId(String userId){
        return userId;
    }

    @PreferenceFunction("username")
    public String putName(String username){return username;}

    @PreferenceFunction("username")
    public String getName(String username){return username;}

    @PreferenceFunction("isStudent")
    public boolean putStudentStatus(boolean isStudent){return isStudent;}

    @PreferenceFunction("isStudent")
    public boolean getStudentStatus(boolean isStudent){return isStudent;}

    @PreferenceFunction("isTutor")
    public boolean putTutorStatus(boolean isTutor){return isTutor;}

    @PreferenceFunction("isTutor")
    public boolean getTutorStatus(boolean isTutor){return isTutor;}
}
