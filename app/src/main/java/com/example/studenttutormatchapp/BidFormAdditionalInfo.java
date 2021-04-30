package com.example.studenttutormatchapp;

public class BidFormAdditionalInfo {

    private String competency;
    private String preferredDateTime;
    private String preferredRate;
    private String description;
    private String rateType;

    public BidFormAdditionalInfo(String competency, String preferredDateTime, String rateType, String preferredRate, String description){
        this.competency = competency;
        this.preferredDateTime = preferredDateTime;
        this.rateType = rateType;
        this.preferredRate = preferredRate;
        this.description = description;
    }

    public String getCompetency() {
        return competency;
    }

    public void setCompetency(String competency) {
        this.competency = competency;
    }

    public String getPreferredDateTime() {
        return preferredDateTime;
    }

    public void setPreferredDateTime(String preferredDateTime) {
        this.preferredDateTime = preferredDateTime;
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

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }


}
