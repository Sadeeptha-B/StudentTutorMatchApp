package com.example.studenttutormatchapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bidId")
    @Expose
    private String bidId;

    @SerializedName("datePosted")
    @Expose
    private String datePosted;
    @SerializedName("dateLastEdited")
    @Expose
    private Object dateLastEdited;
    @SerializedName("content")
    @Expose
    private String content;


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

}