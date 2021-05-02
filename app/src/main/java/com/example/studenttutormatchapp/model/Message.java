package com.example.studenttutormatchapp.model;

import com.example.studenttutormatchapp.helpers.MessageAdditionalInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bidId")
    @Expose
    private String bidId;

    @Expose
    private String posterId;

    @Expose
    private User poster;

    @SerializedName("datePosted")
    @Expose
    private String datePosted;
    @SerializedName("dateLastEdited")
    @Expose
    private Object dateLastEdited;
    @SerializedName("content")
    @Expose
    private String content;

    @Expose
    private MessageAdditionalInfo additionalInfo;


    // Creating a message
    public Message(String bidId, String posterId, String datePosted, String content, MessageAdditionalInfo additionalInfo) {
        this.bidId = bidId;
        this.posterId = posterId;
        this.datePosted = datePosted;
        this.content = content;
        this.additionalInfo = additionalInfo;
    }

    //Receiving a message
    public Message(String id, String bidId, User poster, String datePosted, String content){
        this.id = id;
        this.bidId = bidId;
        this.poster = poster;
        this.datePosted = datePosted;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public Object getDateLastEdited() {
        return dateLastEdited;
    }

    public void setDateLastEdited(Object dateLastEdited) {
        this.dateLastEdited = dateLastEdited;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageAdditionalInfo getAdditionalInfo(){return additionalInfo;}

    public void setAdditionalInfo(MessageAdditionalInfo additionalInfo){this.additionalInfo = additionalInfo;}

}