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

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public UserModel getTutor() {
        return tutor;
    }

    public void setTutor(UserModel tutor) {
        this.tutor = tutor;
    }

    public BidOfferModel getTutorOffer() {
        return tutorOffer;
    }

    public void setTutorOffer(BidOfferModel tutorOffer) {
        this.tutorOffer = tutorOffer;
    }
}
