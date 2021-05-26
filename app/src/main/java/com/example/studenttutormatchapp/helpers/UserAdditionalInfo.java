package com.example.studenttutormatchapp.helpers;

import java.util.ArrayList;
import java.util.List;

public class UserAdditionalInfo {
    private List<String> subscribedBid;

    public List<String> getSubscribedBid() {
        return subscribedBid;
    }

    public void setSubscribedBid(List<String> subscribedBid) {
        this.subscribedBid = subscribedBid;
    }

    public void addSubscribedBids(String newBid){
        if (subscribedBid == null){
            subscribedBid = new ArrayList<String>();
        }
        subscribedBid.add(newBid);
    }
}
