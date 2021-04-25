package com.example.studenttutormatchapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.security.acl.Owner;

public class Competency {

    @SerializedName("id")
    @Expose
    private String id;

    @Expose
    private Subject subject;

    @SerializedName("level")
    @Expose
    private Integer level;

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

}
