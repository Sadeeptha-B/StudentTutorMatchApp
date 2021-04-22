package com.example.studenttutormatchapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("userName")
    @Expose
    private String userName;

    public User(){
    }

    public User(int id, String userName){
        this.id = id;
        this.userName = userName;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String name){
        this.userName = userName;
    }
}
