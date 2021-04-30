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

    public BidOfferModel(JSONObject bidOffer, boolean isTutorBid) {
        try{
            competency = bidOffer.getInt("competency");
            rate = bidOffer.getInt("rate");
            hoursPerLesson = bidOffer.getInt("hoursPerLesson");
            lessonsPerWeek = bidOffer.getInt("lessonsPerWeek");
            isRateHourly = bidOffer.getBoolean("isRateHourly");
            isRateWeekly = bidOffer.getBoolean("isRateWeekly");
            if(isTutorBid()){
                freeClasses = bidOffer.getInt("freeClasses");
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

    public void setCompetency(int competency) {
        this.competency = competency;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getHoursPerLesson() {
        return hoursPerLesson;
    }

    public void setHoursPerLesson(int hoursPerLesson) {
        this.hoursPerLesson = hoursPerLesson;
    }

    public int getLessonsPerWeek() {
        return lessonsPerWeek;
    }

    public void setLessonsPerWeek(int lessonsPerWeek) {
        this.lessonsPerWeek = lessonsPerWeek;
    }

    public int getFreeClasses() {
        return freeClasses;
    }

    public void setFreeClasses(int freeClasses) {
        this.freeClasses = freeClasses;
    }

    public boolean isRateHourly() {
        return isRateHourly;
    }

    public void setRateHourly(boolean rateHourly) {
        isRateHourly = rateHourly;
    }

    public boolean isRateWeekly() {
        return isRateWeekly;
    }

    public void setRateWeekly(boolean rateWeekly) {
        isRateWeekly = rateWeekly;
    }

    public boolean isTutorBid() {
        return isTutorBid;
    }

    public void setTutorBid(boolean tutorBid) {
        isTutorBid = tutorBid;
    }
}
