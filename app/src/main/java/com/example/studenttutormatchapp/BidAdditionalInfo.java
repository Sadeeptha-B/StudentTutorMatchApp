package com.example.studenttutormatchapp;

import java.util.List;

public class BidAdditionalInfo {

    private String competency;
    private String preferredDateTime;
    private String preferredRate;
    private String rateType;
    private List<Offer> offers;

    public BidAdditionalInfo(String competency, String preferredDateTime, String rateType, String preferredRate, String description, List<Offer> offers){
        this.competency = competency;
        this.preferredDateTime = preferredDateTime;
        this.rateType = rateType;
        this.preferredRate = preferredRate;
        this.offers = offers;
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

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
