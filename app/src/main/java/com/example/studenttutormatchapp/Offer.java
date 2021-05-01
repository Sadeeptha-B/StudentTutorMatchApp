package com.example.studenttutormatchapp;

public class Offer {

    private String competency;
    private String description;
    private String tutorName;
    private String offeredDate;
    private String offeredRate;
    private String rateType;


    public Offer(String competency, String description, String tutorName, String offeredDate, String offeredRate, String rateType) {
        this.competency = competency;
        this.description = description;
        this.tutorName = tutorName;
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
}
