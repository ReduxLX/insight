package com.example.insight.model.Bid;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

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
    }

    public String getRateStr(){
        boolean isRateWeekly = isRateWeekly();
        String hourlyRate = String.format(Locale.getDefault(),
                "$%d/h", getRate());
        String weeklyRate = String.format(Locale.getDefault(),
                "$%d/week", getRate());
        return isRateWeekly ? weeklyRate : hourlyRate;
    }

    public String getFreeClassesStr(){
        return String.format(Locale.getDefault(),
                "%d free classes", getFreeClasses());
    }

    public String getHoursPerLessonStr(){
        return String.format(Locale.getDefault(),
                "%d hours per lesson", getHoursPerLesson());
    }

    public String getLessonsPerWeekStr(){
        return String.format(Locale.getDefault(),
                "%d lessons per week", getLessonsPerWeek());
    }

    public String getCompetencyStr(){
        if(getCompetency() >= 1 && getCompetency() <= 3){
            return "Beginner";
        }else if (getCompetency() >= 4 && getCompetency() <= 6){
            return  "Novice";
        }else if (getCompetency() >= 7 && getCompetency() <= 8){
            return "Intermediate";
        }else if (getCompetency() >= 9 && getCompetency() <= 10){
            return "Advanced";
        }else{
            return "No Difficulty";
        }
    }

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

    @Override
    public String toString() {
        return "BidOfferModel{" +
                "competency=" + competency +
                ", rate=" + rate +
                ", hoursPerLesson=" + hoursPerLesson +
                ", lessonsPerWeek=" + lessonsPerWeek +
                ", freeClasses=" + freeClasses +
                ", isRateHourly=" + isRateHourly +
                ", isRateWeekly=" + isRateWeekly +
                ", isTutorBid=" + isTutorBid +
                ", bidOffer=" + bidOffer +
                '}';
    }
}
