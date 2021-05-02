package com.example.studenttutormatchapp;

public class ContractAdditionalInfo {

    private boolean firstPartySigned;
    private boolean secondPartySigned;

    public ContractAdditionalInfo(boolean firstPartySigned, boolean secondPartySigned){
        this.firstPartySigned = firstPartySigned;
        this.secondPartySigned = secondPartySigned;
    }

    public boolean isFirstPartySigned() {
        return firstPartySigned;
    }

    public void setFirstPartySigned(boolean firstPartySigned) {
        this.firstPartySigned = firstPartySigned;
    }

    public boolean isSecondPartySigned() {
        return secondPartySigned;
    }

    public void setSecondPartySigned(boolean secondPartySigned) {
        this.secondPartySigned = secondPartySigned;
    }
}
