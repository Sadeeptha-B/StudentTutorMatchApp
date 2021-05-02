package com.example.studenttutormatchapp;

public class Offer {

    private String competency;
    private String description;
    private String tutorId;
    private String tutorName;
    private String subjectId;
    private String subject;
    private String offeredDate;
    private String offeredRate;
    private String rateType;

    public Offer(String competency, String tutorId, String tutorName, String subjectId, String subject, String rateType, String offeredDate, String offeredRate, String description) {
        this.competency = competency;
        this.description = description;
        this.tutorId = tutorId;
        this.tutorName = tutorName;
        this.subjectId = subjectId;
        this.subject = subject;
        this.offeredDate = offeredDate;
        this.offeredRate = offeredRate;
        this.rateType = rateType;
    }

    public String  getCompetency() {
        return competency;
    }

    public void setCompetency(String  competency) {
        this.competency = competency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getOfferedDate() {
        return offeredDate;
    }

    public void setOfferedDate(String offeredDate) {
        this.offeredDate = offeredDate;
    }

    public String getOfferedRate() {
        return offeredRate;
    }

    public void setOfferedRate(String offeredRate) {
        this.offeredRate = offeredRate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;

    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "competency='" + competency + '\'' +
                ", description='" + description + '\'' +
                ", tutorName='" + tutorName + '\'' +
                ", offeredDate='" + offeredDate + '\'' +
                ", offeredRate='" + offeredRate + '\'' +
                ", rateType='" + rateType + '\'' +
                '}';
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}