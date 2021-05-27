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

    // Search and return index of tutor if found, else null
    public int searchTutorBids(String tutorId){
        try {
            for (int i=0; i < getTutorBids().length(); i++){
                // Get tutor bid's tutor id
                JSONObject tutorBidObj = getTutorBids().getJSONObject(i);
                TutorBidModel tutorBid = new TutorBidModel(tutorBidObj);
                String currentTutorId = tutorBid.getTutor().getId();
                // If they match, remove the bid
                if(tutorId.equals(currentTutorId)){
                    return i;
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return -1;
    }

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
