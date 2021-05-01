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

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateSigned() {
        return dateSigned;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public JSONObject getPaymentInfo() {
        return paymentInfo;
    }

    public JSONObject getLessonInfo() {
        return lessonInfo;
    }

    public JSONObject getAdditionalInfo() {
        return additionalInfo;
    }

    public UserModel getFirstParty() {
        return firstParty;
    }

    public UserModel getSecondParty() {
        return secondParty;
    }

    public SubjectModel getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "ContractModel{" +
                "id='" + id + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateSigned='" + dateSigned + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", paymentInfo=" + paymentInfo +
                ", lessonInfo=" + lessonInfo +
                ", additionalInfo=" + additionalInfo +
                ", firstParty=" + firstParty +
                ", secondParty=" + secondParty +
                ", subject=" + subject +
                '}';
    }
}
