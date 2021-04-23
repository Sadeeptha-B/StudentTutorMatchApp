
package com.example.studenttutormatchapp.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("givenName")
    @Expose
    private String givenName;

    @SerializedName("familyName")
    @Expose
    private String familyName;

    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("isStudent")
    @Expose
    private Boolean isStudent;

    @SerializedName("isTutor")
    @Expose
    private Boolean isTutor;

    @SerializedName("competencies")
    @Expose
    private List<Object> competencies = null;

    @SerializedName("qualifications")
    @Expose
    private List<Object> qualifications = null;

    @SerializedName("initiatedBids")
    @Expose
    private List<Bid> bids = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(Boolean isStudent) {
        this.isStudent = isStudent;
    }

    public Boolean getIsTutor() {
        return isTutor;
    }

    public void setIsTutor(Boolean isTutor) {
        this.isTutor = isTutor;
    }

    public List<Object> getCompetencies() {
        return competencies;
    }

    public void setCompetencies(List<Object> competencies) {
        this.competencies = competencies;
    }

    public List<Object> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<Object> qualifications) {
        this.qualifications = qualifications;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

}
