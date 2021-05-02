package com.example.studenttutormatchapp.model;

import com.example.studenttutormatchapp.helpers.ContractAdditionalInfo;
import com.example.studenttutormatchapp.helpers.ContractLessonInfo;
import com.example.studenttutormatchapp.helpers.ContractPaymentInfo;

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
    String expiryDate;
    ContractPaymentInfo paymentInfo;
    ContractLessonInfo lessonInfo;
    ContractAdditionalInfo additionalInfo;

    public Contract(String firstPartyId, String secondPartyId, String subjectId, String dateCreated, String expiryDate, ContractPaymentInfo paymentInfo, ContractLessonInfo lessonInfo, ContractAdditionalInfo additionalInfo){
        this.firstPartyId = firstPartyId;
        this.secondPartyId = secondPartyId;
        this.subjectId = subjectId;;
        this.dateCreated = dateCreated;
        this.expiryDate = expiryDate;
        this.paymentInfo = paymentInfo;
        this.lessonInfo = lessonInfo;
        this.additionalInfo = additionalInfo;
    }

    public Contract (String id, User firstParty, User secondParty, Subject subject, String dateCreated, String dateSigned, String expiryDate, ContractPaymentInfo paymentInfo, ContractLessonInfo lessonInfo) {
        this.id = id;
        this.firstParty = firstParty;
        this.secondParty = secondParty;
        this.subject = subject;
        this.dateCreated = dateCreated;
        this.dateSigned = dateSigned;
        this.expiryDate = expiryDate;
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

    public String getDateSigned() {
        return dateSigned;
    }

    public ContractAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public String getId() {
        return id;
    }

}
