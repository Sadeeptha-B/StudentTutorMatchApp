
package com.example.studenttutormatchapp.model;


import com.example.studenttutormatchapp.BidFormAdditionalInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class Bid {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private String type;

    @Expose
    private String initiatorId;

    @Expose
    private User initiator;

    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;

    @SerializedName("dateClosedDown")
    @Expose
    private String dateClosedDown;

    /*TODO:  Deprecate this- Move to storing ID only */
    @SerializedName("subject")
    @Expose
    private Subject subject;

    @Expose
    private List<Message> messages;

    @SerializedName("subjectId")
    @Expose
    private String subjectId;

    @SerializedName("additionalInfo")
    @Expose
    private BidFormAdditionalInfo additionalInfo;

    public Bid(String type,String initiatorId , String dateCreated, String dateClosedDown, Subject subject, BidFormAdditionalInfo additionalInfo){
        this.type = type;
        this.initiatorId = initiatorId;
        this.dateCreated = dateCreated;
        this.dateClosedDown = dateClosedDown;
        this.subject = subject;
        this.subjectId = this.subject.getId();
        this.additionalInfo = additionalInfo;
    }

    public Bid(String type,User initiator , String dateCreated, String dateClosedDown, Subject subject, List<Message> messages){
        this.type = type;
        this.initiator = initiator;
        this.dateCreated = dateCreated;
        this.dateClosedDown = dateClosedDown;
        this.subject = subject;
        this.messages = messages;
    }

    public BidFormAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(BidFormAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateClosedDown() {
        return dateClosedDown;
    }

    public void setDateClosedDown(String dateClosedDown) {
        this.dateClosedDown = dateClosedDown;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
