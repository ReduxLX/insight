package com.example.insight.model.Bid;

import com.example.insight.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

public class TutorBidModel {
    private String dateCreated;
    private UserModel tutor;
    private BidOfferModel tutorOffer;

    public TutorBidModel(JSONObject tutorBid){
        try{
            dateCreated = tutorBid.getString("dateCreated");
            tutor = new UserModel(tutorBid.getJSONObject("tutor"));
            tutorOffer = new BidOfferModel(tutorBid.getJSONObject("tutorOffer"), true);

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public UserModel getTutor() {
        return tutor;
    }

    public BidOfferModel getTutorOffer() {
        return tutorOffer;
    }

}
