package com.example.studenttutormatchapp.helpers;

import com.example.studenttutormatchapp.model.pojo.Message;

import java.util.Comparator;

public class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message m1, Message m2) {

        return m1.getDatePosted().compareTo(m2.getDatePosted());
    }
}
