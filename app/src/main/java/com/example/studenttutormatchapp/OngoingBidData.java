package com.example.studenttutormatchapp;

public class OngoingBidData {
    private String subjectName;
    private String dateCreated;

    public OngoingBidData(String subjectName, String dateCreated){
        this.subjectName = subjectName;
        this.dateCreated = dateCreated;
    }


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }


}
