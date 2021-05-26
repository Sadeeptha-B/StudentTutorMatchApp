
package com.example.studenttutormatchapp.model;

import java.util.List;

import com.example.studenttutormatchapp.helpers.UserAdditionalInfo;
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

    @Expose
    private String password;

    @SerializedName("isStudent")
    @Expose
    private Boolean isStudent;

    @SerializedName("isTutor")
    @Expose
    private Boolean isTutor;

    @SerializedName("competencies")
    @Expose
    private List<Competency> competencies = null;

    @SerializedName("qualifications")
    @Expose
    private List<Qualification> qualifications = null;

    @SerializedName("initiatedBids")
    @Expose
    private List<Bid> bids = null;

    @Expose
    private UserAdditionalInfo additionalInfo;

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

    public List<Competency> getCompetencies() {
        return competencies;
    }

    public void setCompetencies(List<Competency> competencies) {
        this.competencies = competencies;
    }

    public List<Qualification> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<Qualification> qualifications) {
        this.qualifications = qualifications;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(UserAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
