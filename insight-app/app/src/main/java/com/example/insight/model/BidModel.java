package com.example.insight.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class used to destructure Bid JSON Object
 */
public class BidModel {
    private String id = "";
    private String type = "";
    private String country = "";

    private String studentId = "";
    private String givenName = "";
    private String familyName = "";

    private String subjectId = "";
    private String subjectName = "";

    private int competency = 0;
    private int rate = 0;
    private int hoursPerLesson = 0;
    private int lessonsPerWeek = 0;
    private boolean isRateHourly = false;
    private boolean isRateWeekly = false;
    private JSONArray tutorBids;
    private JSONObject additionalInfo;

    public BidModel(JSONObject bid) {
        try{
            id = bid.getString("id");
            type = bid.getString("type");

            JSONObject initiator = bid.getJSONObject("initiator");
            studentId = initiator.getString("id");
            givenName = initiator.getString("givenName");
            familyName = initiator.getString("familyName");

            JSONObject subject = bid.getJSONObject("subject");
            subjectId = subject.getString("id");
            subjectName = subject.getString("name");

            additionalInfo = bid.getJSONObject("additionalInfo");
            competency = additionalInfo.getInt("competency");
            rate = additionalInfo.getInt("rate");
            hoursPerLesson = additionalInfo.getInt("hoursPerLesson");
            lessonsPerWeek = additionalInfo.getInt("lessonsPerWeek");
            isRateHourly = additionalInfo.getBoolean("isRateHourly");
            isRateWeekly = additionalInfo.getBoolean("isRateWeekly");
            tutorBids = additionalInfo.getJSONArray("tutorBids");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

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

    public JSONObject getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(JSONObject additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public JSONArray getTutorBids() {
        return tutorBids;
    }

    public void setTutorBids(JSONArray tutorBids) {
        this.tutorBids = tutorBids;
    }
}
