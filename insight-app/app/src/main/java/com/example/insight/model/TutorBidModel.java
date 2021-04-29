package com.example.insight.model;

import org.json.JSONException;
import org.json.JSONObject;

public class TutorBidModel {
    private String dateCreated = "";
    private String tutorId = "";
    private String givenName = "";
    private String familyName = "";
    private String userName = "";
    private int rate = 0;
    private int lessonsPerWeek = 0;
    private int hoursPerLesson = 0;
    private int freeClasses = 0;
    private boolean isRateHourly;
    private boolean isRateWeekly;

    public TutorBidModel(JSONObject tutorBid) {
        try{
            dateCreated = tutorBid.getString("dateCreated");

            JSONObject tutor = tutorBid.getJSONObject("tutor");
            tutorId = tutor.getString("id");
            givenName = tutor.getString("givenName");
            familyName = tutor.getString("familyName");
            userName = tutor.getString("userName");

            JSONObject tutorOffer = tutorBid.getJSONObject("tutorOffer");
            rate = tutorOffer.getInt("rate");
            hoursPerLesson = tutorOffer.getInt("hoursPerLesson");
            lessonsPerWeek = tutorOffer.getInt("lessonsPerWeek");
            freeClasses = tutorOffer.getInt("freeClasses");
            isRateHourly = tutorOffer.getBoolean("isRateHourly");
            isRateWeekly = tutorOffer.getBoolean("isRateWeekly");

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

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getLessonsPerWeek() {
        return lessonsPerWeek;
    }

    public void setLessonsPerWeek(int lessonsPerWeek) {
        this.lessonsPerWeek = lessonsPerWeek;
    }

    public int getHoursPerLesson() {
        return hoursPerLesson;
    }

    public void setHoursPerLesson(int hoursPerLesson) {
        this.hoursPerLesson = hoursPerLesson;
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
}
