package com.example.insight.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ContractModel {

    private String id;
    private String dateCreated;
    private String dateSigned;
    private String expiryDate;

    private JSONObject paymentInfo;
    private JSONObject lessonInfo;
    private JSONObject additionalInfo;

    private UserModel firstParty;
    private UserModel secondParty;
    private SubjectModel subject;


    public ContractModel(JSONObject contract){
        try{
            id = contract.getString("id");
            dateCreated = contract.getString("dateCreated");
            dateSigned = contract.getString("dateSigned");
            expiryDate = contract.getString("expiryDate");

            paymentInfo = contract.getJSONObject("paymentInfo");
            lessonInfo = contract.getJSONObject("lessonInfo");
            additionalInfo = contract.getJSONObject("additionalInfo");

            firstParty = new UserModel(contract.getJSONObject("firstParty"));
            secondParty = new UserModel(contract.getJSONObject("secondParty"));
            subject = new SubjectModel(contract.getJSONObject("subject"));

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(String dateSigned) {
        this.dateSigned = dateSigned;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public JSONObject getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(JSONObject paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public JSONObject getLessonInfo() {
        return lessonInfo;
    }

    public void setLessonInfo(JSONObject lessonInfo) {
        this.lessonInfo = lessonInfo;
    }

    public JSONObject getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(JSONObject additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public UserModel getFirstParty() {
        return firstParty;
    }

    public void setFirstParty(UserModel firstParty) {
        this.firstParty = firstParty;
    }

    public UserModel getSecondParty() {
        return secondParty;
    }

    public void setSecondParty(UserModel secondParty) {
        this.secondParty = secondParty;
    }

    public SubjectModel getSubject() {
        return subject;
    }

    public void setSubjectModel(SubjectModel subjectModel) {
        this.subject = subjectModel;
    }
}
