package com.example.insight.model.Bid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Locale;

/**
 * Model class which represent the additionalInfo object of a Bid
 */
public class BidAdditionalInfoModel {
    private BidOfferModel studentOffer;
    private JSONArray tutorBids;
    private String expiryDate;

    public BidAdditionalInfoModel(JSONObject additionalInfo) {
        try{
            studentOffer = new BidOfferModel(additionalInfo.getJSONObject("studentOffer"), false);
            tutorBids = additionalInfo.getJSONArray("tutorBids");
            expiryDate = additionalInfo.getString("expiryDate");

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public JSONObject parseIntoJSON(){
        JSONObject json = new JSONObject();
        try{
            json.put("studentOffer", studentOffer.parseIntoJSON());
            json.put("tutorBids", getTutorBids());
            json.put("expiryDate", getExpiryDate());
        } catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    };

    public BidOfferModel getStudentOffer() {
        return studentOffer;
    }

    public JSONArray getTutorBids() {
        return tutorBids;
    }

    public String getTutorBidsStr(){
        return String.format(Locale.getDefault(),
                "%d bids", getTutorBids().length());
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String toString() {
        return "BidAdditionalInfoModel{" +
                "studentOffer=" + studentOffer +
                ", tutorBids=" + tutorBids +
                ", expiryDate='" + expiryDate + '\'' +
                '}';
    }
}
