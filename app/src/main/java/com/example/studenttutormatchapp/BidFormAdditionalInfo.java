package com.example.studenttutormatchapp;

public class BidFormAdditionalInfo {

    private String competency;
    private String preferredTime;
    private String preferredRate;
    private String description;

    public BidFormAdditionalInfo(String competency, String preferredTime, String preferredRate, String description){
        this.competency = competency;
        this.preferredTime = preferredTime;
        this.preferredRate = preferredRate;
        this.description = description;
    }

    public String getCompetency() {
        return competency;
    }

    public void setCompetency(String competency) {
        this.competency = competency;
    }

    public String getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(String preferredTime) {
        this.preferredTime = preferredTime;
    }

    public String getPreferredRate() {
        return preferredRate;
    }

    public void setPreferredRate(String preferredRate) {
        this.preferredRate = preferredRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
