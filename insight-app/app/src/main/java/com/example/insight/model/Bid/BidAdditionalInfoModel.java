package com.example.insight.model.Bid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BidAdditionalInfoModel {
    private BidOfferModel studentOffer;
    private JSONArray tutorBids;

    public BidAdditionalInfoModel(JSONObject additionalInfo) {
        try{
            studentOffer = new BidOfferModel(additionalInfo.getJSONObject("studentOffer"), false);
            tutorBids = additionalInfo.getJSONArray("tutorBids");

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public JSONObject parseIntoJSON(){
        JSONObject json = new JSONObject();
        try{
            json.put("studentOffer", studentOffer.parseIntoJSON());
            json.put("tutorBids", tutorBids);
        } catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    };

    public BidOfferModel getStudentOffer() {
        return studentOffer;
    }

    public void setStudentOffer(BidOfferModel studentOffer) {
        this.studentOffer = studentOffer;
    }

    public JSONArray getTutorBids() {
        return tutorBids;
    }

    public void setTutorBids(JSONArray tutorBids) {
        this.tutorBids = tutorBids;
    }
}
