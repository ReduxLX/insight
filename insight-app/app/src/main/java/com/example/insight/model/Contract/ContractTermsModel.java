package com.example.insight.model.Contract;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ContractTermsModel {
    private int competency;
    private int rate;
    private int hoursPerLesson;
    private int lessonsPerWeek;
    private int freeClasses;
    private boolean isRateHourly;
    private boolean isRateWeekly;

    public ContractTermsModel(JSONObject contractTerms) {
        try{
            rate = contractTerms.getInt("rate");
            hoursPerLesson = contractTerms.getInt("hoursPerLesson");
            lessonsPerWeek = contractTerms.getInt("lessonsPerWeek");
            isRateHourly = contractTerms.getBoolean("isRateHourly");
            isRateWeekly = contractTerms.getBoolean("isRateWeekly");
            freeClasses = contractTerms.getInt("freeClasses");
            competency = contractTerms.getInt("competency");
        } catch (JSONException e){
            e.printStackTrace();
        }
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

    @Override
    public String toString() {
        return "ContractTermsModel{" +
                "competency=" + competency +
                ", rate=" + rate +
                ", hoursPerLesson=" + hoursPerLesson +
                ", lessonsPerWeek=" + lessonsPerWeek +
                ", freeClasses=" + freeClasses +
                ", isRateHourly=" + isRateHourly +
                ", isRateWeekly=" + isRateWeekly +
                '}';
    }
}
