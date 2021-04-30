package com.example.insight.model;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class JWTModel {
    private String inputJWT;
    private String givenName;
    private String familyName;
    private String username;

    private boolean isStudent;
    private boolean isTutor;
    private long expiryDateInSeconds;
    private Date expiryDate;
    private String expiryDate_ISO8601;

    public JWTModel(String jwt){
        JWT parsedJWT = new JWT(jwt);
        Map<String, Claim> allClaims = parsedJWT.getClaims();
        givenName = allClaims.get("givenName").asString();
        familyName = allClaims.get("familyName").asString();
        username = allClaims.get("username").asString();
        isStudent = allClaims.get("isStudent").asBoolean();
        isTutor = allClaims.get("isTutor").asBoolean();
        expiryDateInSeconds = allClaims.get("exp").asLong();

        expiryDate = new Date();
        expiryDate.setTime(getExpiryDateInSeconds() * 1000);

        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        expiryDate_ISO8601 = ISO8601.format(expiryDate);
    }

    // Compare current date with expiry date to see if JWT is expired
    public boolean isJWTExpired(){
        return new Date().after(expiryDate);
    }

    public String getInputJWT() {
        return inputJWT;
    }

    public void setInputJWT(String inputJWT) {
        this.inputJWT = inputJWT;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public long getExpiryDateInSeconds() {
        return expiryDateInSeconds;
    }

    public void setExpiryDateInSeconds(long expiryDateInSeconds) {
        this.expiryDateInSeconds = expiryDateInSeconds;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate_ISO8601() {
        return expiryDate_ISO8601;
    }

    public void setExpiryDate_ISO8601(String expiryDate_ISO8601) {
        this.expiryDate_ISO8601 = expiryDate_ISO8601;
    }
}
