package com.example.insight.model.Bid;


import org.json.JSONException;
import org.json.JSONObject;

public class BidOfferModel {
    private int competency;
    private int rate;
    private int hoursPerLesson;
    private int lessonsPerWeek;
    private int freeClasses;
    private boolean isRateHourly;
    private boolean isRateWeekly;
    private boolean isTutorBid;
    private JSONObject bidOffer;

    public BidOfferModel(JSONObject bidOffer, boolean isTutorBid) {
        try{
            this.bidOffer = bidOffer;
            this.isTutorBid = isTutorBid;
            rate = bidOffer.getInt("rate");
            hoursPerLesson = bidOffer.getInt("hoursPerLesson");
            lessonsPerWeek = bidOffer.getInt("lessonsPerWeek");
            isRateHourly = bidOffer.getBoolean("isRateHourly");
            isRateWeekly = bidOffer.getBoolean("isRateWeekly");
            if(isTutorBid){
                freeClasses = bidOffer.getInt("freeClasses");
            }else{
                competency = bidOffer.getInt("competency");
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public JSONObject parseIntoJSON(){
        JSONObject json = new JSONObject();
        try{
            json.put("competency", competency);
            json.put("rate", rate);
            json.put("hoursPerLesson", hoursPerLesson);
            json.put("lessonsPerWeek", lessonsPerWeek);
            json.put("isRateHourly", isRateHourly);
            json.put("isRateWeekly", isRateWeekly);
            if(isTutorBid()){
                json.put("freeClasses", freeClasses);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    };

    public int getCompetency() {
        return competency;
    }

    public int getRate() {
        return rate;
    }

    public int getHoursPerLesson() {
        return hoursPerLesson;
    }

    public int getLessonsPerWeek() {
        return lessonsPerWeek;
    }

    public int getFreeClasses() {
        return freeClasses;
    }

    public boolean isRateHourly() {
        return isRateHourly;
    }

    public boolean isRateWeekly() {
        return isRateWeekly;
    }

    public boolean isTutorBid() {
        return isTutorBid;
    }

    public JSONObject getBidOffer() {
        return bidOffer;
    }
}
