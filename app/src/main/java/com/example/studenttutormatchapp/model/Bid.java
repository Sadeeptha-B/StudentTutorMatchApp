
package com.example.studenttutormatchapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bid {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;

    @SerializedName("dateClosedDown")
    @Expose
    private Object dateClosedDown;

    @SerializedName("subject")
    @Expose
    private Subject subject;


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

    public Object getDateClosedDown() {
        return dateClosedDown;
    }

    public void setDateClosedDown(Object dateClosedDown) {
        this.dateClosedDown = dateClosedDown;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}
