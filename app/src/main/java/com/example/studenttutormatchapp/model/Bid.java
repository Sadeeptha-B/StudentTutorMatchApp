
package com.example.studenttutormatchapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Bid {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("dateCreated")
    @Expose
    private Date dateCreated;

    @SerializedName("dateClosedDown")
    @Expose
    private Date dateClosedDown;

    @SerializedName("subject")
    @Expose
    private Subject subject;

    public Bid(String type, Date dateCreated, Date dateClosedDown, Subject subject){
        this.type = type;
        this.dateCreated = dateCreated;
        this.dateClosedDown = dateClosedDown;
        this.subject = subject;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateClosedDown() {
        return dateClosedDown;
    }

    public void setDateClosedDown(Date dateClosedDown) {
        this.dateClosedDown = dateClosedDown;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}
