package com.example.studenttutormatchapp;

public class User {
    private final String id;
    private final String givenName;
    private final String familyName;
    private String userName;

    public User(String _id, String _givenName, String _familyName, String _userName){
        this.id = _id;
        this.givenName = _givenName;
        this.familyName = _familyName;
        this.userName = _userName;
    }


    public String getId() {
        return id;
    }
    public String getGivenName() {
        return givenName;
    }
    public String getFamilyName() {
        return familyName;
    }
    public String getUserName() {
        return userName;
    }
    public void setAge(String userName){
        this.userName = userName;
    }
}
