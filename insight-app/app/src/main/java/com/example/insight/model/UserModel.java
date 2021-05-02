package com.example.insight.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Model class which represent the user details
 * Serves as one of the basic foundation model class for others as this is used in Bid, Contract, etc...
 */
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

    public String getFullName(){
        return getGivenName() + " " + getFamilyName();
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public boolean isTutor() {
        return isTutor;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", userName='" + userName + '\'' +
                ", isStudent=" + isStudent +
                ", isTutor=" + isTutor +
                '}';
    }
}
