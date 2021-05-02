package com.example.studenttutormatchapp.helpers;

public class OngoingBidData {
    private String subjectName;
    private String dateCreated;
    private String bidId;
    private String bidType;


    public OngoingBidData(String subjectName, String dateCreated, String bidId, String bidType){
        this.subjectName = subjectName;
        this.dateCreated = dateCreated;
        this.bidId = bidId;
        this.bidType = bidType;

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

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }
}
