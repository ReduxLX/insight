package com.example.insight.model.Contract;


import android.util.Log;

import com.example.insight.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ContractTermsModel {
    private int competency;
    private int rate;
    private int hoursPerLesson;
    private int lessonsPerWeek;
    private int freeClasses;
    private int contractDurationMonths;
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
            contractDurationMonths = contractTerms.getInt("contractDurationMonths");
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

    public String getCompetencyDetails(){
        return "Level "+String.valueOf(getCompetency())+" ("+getCompetencyStr()+")";
    }

    public int getCompetencyResource(){
        switch(getCompetency()){
            case 1:
                return R.drawable.level_one;
            case 2:
                return R.drawable.level_two;
            case 3:
                return R.drawable.level_three;
            case 4:
                return R.drawable.level_four;
            case 5:
                return R.drawable.level_five;
            case 6:
                return R.drawable.level_six;
            case 7:
                return R.drawable.level_seven;
            case 8:
                return R.drawable.level_eight;
            case 9:
                return R.drawable.level_nine;
            case 10:
                return R.drawable.level_ten;
            default:
                return R.drawable.level_one;
        }
    }

    // Helper functions to convert values to their dropdown item index
    // Note that a change in dropdown ordering will require updating these functions
    public int getHoursPerLessonDropdownIndex(){
        switch(getHoursPerLesson()){
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            case 4: return 4;
            case 5: return 5;
            default: return 0;
        }
    }

    public int getLessonsPerWeekDropdownIndex(){
        switch(getLessonsPerWeek()){
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            case 4: return 4;
            case 5: return 5;
            default: return 0;
        }
    }

    public int getFreeClassesDropdownIndex(){
        switch(getFreeClasses()){
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            case 4: return 4;
            case 5: return 5;
            default: return 0;
        }
    }

    public int getContractDurationDropdownIndex(){
        switch(getContractDurationMonths()){
            case 3: return 0;
            case 6: return 1;
            case 12: return 2;
            case 24: return 3;
            default: return 1;
        }
    }
    public int getContractDurationMonths() {
        return contractDurationMonths;
    }

    public String getContractDurationMonthsStr(){
        return String.valueOf(getContractDurationMonths()) + " months";
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
