package com.example.studenttutormatchapp.helpers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BidAdditionalInfo {
    @SerializedName("competency")
    @Expose
    private String competency;

    @SerializedName("preferredDateTime")
    @Expose
    private String preferredDateTime;

    @SerializedName("preferredRate")
    @Expose
    private String preferredRate;

    @SerializedName("rateType")
    @Expose
    private String rateType;

    @SerializedName("offers")
    @Expose
    private List<Offer> offers;

    public BidAdditionalInfo(String competency, String preferredDateTime, String rateType, String preferredRate, ArrayList<Offer> offers){
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

    public void addOffer(Offer offer){
        offers.add(offer);
    }

    public List<Offer> getOffers(){
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    @Override
    public String toString() {
        return "BidAdditionalInfo{" +
                "competency='" + competency + '\'' +
                ", preferredDateTime='" + preferredDateTime + '\'' +
                ", preferredRate='" + preferredRate + '\'' +
                ", rateType='" + rateType + '\'' +
                ", offers=" + offers +
                '}';
    }
}
