package com.example.studenttutormatchapp.model;

import com.example.studenttutormatchapp.ContractLessonInfo;
import com.example.studenttutormatchapp.ContractPaymentInfo;

public class Contract {

    String id;
    String firstPartyId;
    String secondPartyId;
    User firstParty;
    User secondParty;
    String subjectId;
    Subject subject;
    String dateCreated;
    String dateSigned;
    String dateExpired;
    ContractPaymentInfo paymentInfo;
    ContractLessonInfo lessonInfo;

    public Contract(String firstPartyId, String secondPartyId, String subjectId, String dateCreated, String dateExpired, ContractPaymentInfo paymentInfo, ContractLessonInfo lessonInfo){
        this.firstPartyId = firstPartyId;
        this.secondPartyId =secondPartyId;
        this.subjectId = subjectId;;
        this.dateCreated = dateCreated;
        this.dateExpired = dateExpired;
        this.paymentInfo = paymentInfo;
        this.lessonInfo = lessonInfo;
    }

    public Contract (String id, User firstParty, User secondParty, Subject subject, String dateCreated, String dateSigned, String dateExpired, ContractPaymentInfo paymentInfo, ContractLessonInfo lessonInfo) {
        this.id = id;
        this.firstParty = firstParty;
        this.secondParty = secondParty;
        this.subject = subject;
        this.dateCreated = dateCreated;
        this.dateSigned = dateSigned;
        this.dateExpired = dateExpired;
        this.paymentInfo = paymentInfo;
        this.lessonInfo = lessonInfo;
    }

    public User getFirstParty() {
        return firstParty;
    }

    public User getSecondParty() {
        return secondParty;
    }

    public Subject getSubject() {
        return subject;
    }


}
