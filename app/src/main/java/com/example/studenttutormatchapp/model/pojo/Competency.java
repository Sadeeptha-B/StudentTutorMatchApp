package com.example.studenttutormatchapp.model.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Competency {

    @SerializedName("id")
    @Expose
    private String id;

    @Expose
    private Subject subject;

    @SerializedName("level")
    @Expose
    private Integer level;

    @Expose
    private User owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Subject getSubject(){return subject;}

    public void setSubject(Subject subject){this.subject = subject;}

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
