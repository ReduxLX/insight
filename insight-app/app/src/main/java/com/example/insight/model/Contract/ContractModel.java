package com.example.insight.model.Contract;

import com.example.insight.model.SubjectModel;
import com.example.insight.model.User.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContractModel {

    private String id;
    private String dateCreated;
    private String dateSigned;
    private String expiryDate;

    private JSONObject paymentInfo;
    private JSONObject lessonInfo;
    private ContractAdditionalInfoModel additionalInfo;

    private UserModel firstParty;
    private UserModel secondParty;
    private SubjectModel subject;

    private JSONObject contractJSON;


    public ContractModel(JSONObject contract){
        try{
            id = contract.getString("id");
            dateCreated = contract.getString("dateCreated");
            dateSigned = contract.getString("dateSigned");
            expiryDate = contract.getString("expiryDate");

            paymentInfo = contract.getJSONObject("paymentInfo");
            lessonInfo = contract.getJSONObject("lessonInfo");
            additionalInfo = new ContractAdditionalInfoModel(contract.getJSONObject("additionalInfo"));

            firstParty = new UserModel(contract.getJSONObject("firstParty"));
            secondParty = new UserModel(contract.getJSONObject("secondParty"));
            subject = new SubjectModel(contract.getJSONObject("subject"));

            // Store original json
            contractJSON = contract;

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getSubjectAndCompetencyStr(){
        return String.format(Locale.getDefault(),
                "%s (%s)", subject.getName(),
                additionalInfo.getContractTerms().getCompetencyStr());
    }

    public String getExpiryDateStr(){
        String expiryDateStr = "Invalid Date";

        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        SimpleDateFormat DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try{
            // IMPORTANT: Need to replace Z to +0700 for ISO8601 parse to work
            Date expiryDateObj = ISO8601.parse(getExpiryDate().replace("Z", "+0700"));
            assert expiryDateObj != null;
            expiryDateStr = String.format(Locale.getDefault(),
                    "Expires %s", DD_MM_YYYY.format(expiryDateObj));
        }catch (ParseException e){
            e.printStackTrace();
        }

        return expiryDateStr;
    }

    public Date getExpiryDateObj(){
        Date expiryDateObj = null;
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        try{
            expiryDateObj = ISO8601.parse(getExpiryDate().replace("Z", "+0700"));
        }catch (ParseException e){
            e.printStackTrace();
        }

        return expiryDateObj;
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

    public ContractAdditionalInfoModel getAdditionalInfo() {
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

    public JSONObject getContractJSON() {
        return contractJSON;
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
