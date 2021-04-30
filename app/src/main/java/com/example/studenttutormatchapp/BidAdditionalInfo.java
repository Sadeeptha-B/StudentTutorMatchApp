package com.example.studenttutormatchapp;

public class BidAdditionalInfo {

    private String preferedTime;
    private String preferedRate;


    public BidAdditionalInfo(String preferedTime, String preferedRate) {
        this.preferedTime = preferedTime;
        this.preferedRate = preferedRate;
    }

    public String getPreferedTime() {
        return preferedTime;
    }

    public String getPreferedRate() {
        return preferedRate;
    }

    public void setPreferedTime(String preferedTime) {
        this.preferedTime = preferedTime;
    }

    public void setPreferedRate(String preferedRate) {
        this.preferedRate = preferedRate;
    }
}
