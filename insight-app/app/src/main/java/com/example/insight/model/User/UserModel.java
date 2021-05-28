package com.example.insight.model.User;

import android.util.Log;

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
    private boolean isAdmin;
    private UserAdditionalInfoModel additionalInfo;

    public UserModel(JSONObject user){
        try{
            id = user.getString("id");
            givenName = user.getString("givenName");
            familyName = user.getString("familyName");
            userName = user.getString("userName");
            isStudent = user.getBoolean("isStudent");
            isTutor = user.getBoolean("isTutor");
            additionalInfo = new UserAdditionalInfoModel(user.getJSONObject("additionalInfo"));
            try{
                isAdmin = user.getBoolean("isAdmin");
            } catch(JSONException ignored){
            }

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

    public boolean isAdmin() {
        return isAdmin;
    }

    public UserAdditionalInfoModel getAdditionalInfo() {
        return additionalInfo;
    }

    public JSONObject parseIntoJSON(){
        JSONObject json = new JSONObject();
        try{
            json.put("givenName", getGivenName());
            json.put("familyName", getFamilyName());
            json.put("isStudent", isStudent());
            json.put("isTutor", isTutor());
            json.put("isAdmin", isAdmin());
            json.put("additionalInfo", getAdditionalInfo().parseIntoJSON());
        } catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    };

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", userName='" + userName + '\'' +
                ", isStudent=" + isStudent +
                ", isTutor=" + isTutor +
                ", isAdmin=" + isAdmin +
                ", additionalInfo=" + additionalInfo +
                '}';
    }
}
