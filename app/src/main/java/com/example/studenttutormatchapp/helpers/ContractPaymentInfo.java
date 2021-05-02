package com.example.studenttutormatchapp.helpers;

public class ContractPaymentInfo {

    String rate;
    String rateType;

    public ContractPaymentInfo(String rate, String rateType){
        this.rate = rate;
        this.rateType = rateType;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }
}
