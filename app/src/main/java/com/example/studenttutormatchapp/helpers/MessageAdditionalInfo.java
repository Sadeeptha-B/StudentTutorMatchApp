package com.example.studenttutormatchapp.helpers;

public class MessageAdditionalInfo {
    private String receiver;
    private String receiverName;

    public MessageAdditionalInfo(String receiver, String receiverName){
        this.receiver = receiver;
        this.receiverName = receiverName;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
