package com.example.insight.model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {
    private String id;
    private String givenName;
    private String familyName;
    private String userName;
    private boolean isStudent;
    private boolean isTutor;

    public UserModel(JSONObject user){
        try{
            id = user.getString("id");
            givenName = user.getString("givenName");
            familyName = user.getString("familyName");
            userName = user.getString("userName");
            isStudent = user.getBoolean("isStudent");
            isTutor = user.getBoolean("isTutor");

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

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public boolean isTutor() {
        return isTutor;
    }

    public void setTutor(boolean tutor) {
        isTutor = tutor;
    }
}
