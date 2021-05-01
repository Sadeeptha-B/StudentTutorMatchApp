package com.example.studenttutormatchapp;

public class Offer {

    private String competencyLevel;
    private String prefRateType;
    private String prefDayTime;
    private String prefRate;
    private String description;

    public Offer(String competencyLevel, String prefRateType, String prefDayTime, String prefRate, String description) {
        this.competencyLevel = competencyLevel;
        this.prefRateType = prefRateType;
        this.prefDayTime = prefDayTime;
        this.prefRate = prefRate;
        this.description = description;
    }

    public String getCompetencyLevel() {
        return competencyLevel;
    }

    public void setCompetencyLevel(String competencyLevel) {
        this.competencyLevel = competencyLevel;
    }

    public String getPrefRateType() {
        return prefRateType;
    }

    public void setPrefRateType(String prefRateType) {
        this.prefRateType = prefRateType;
    }

    public String getPrefDayTime() {
        return prefDayTime;
    }

    public void setPrefDayTime(String prefDayTime) {
        this.prefDayTime = prefDayTime;
    }

    public String getPrefRate() {
        return prefRate;
    }

    public void setPrefRate(String prefRate) {
        this.prefRate = prefRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "competencyLevel='" + competencyLevel + '\'' +
                ", prefRateType='" + prefRateType + '\'' +
                ", prefDayTime='" + prefDayTime + '\'' +
                ", prefRate='" + prefRate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
