package com.example.studenttutormatchapp;

public class OngoingBidData {
    private String subjectName;
    private String dateCreated;
    private String bidId;

    public OngoingBidData(String subjectName, String dateCreated, String  bidId){
        this.subjectName = subjectName;
        this.dateCreated = dateCreated;
        this.bidId = bidId;
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

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }
}
