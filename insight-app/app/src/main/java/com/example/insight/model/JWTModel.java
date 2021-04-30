package com.example.insight.model;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class JWTModel {

    private String id;
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
        id = allClaims.get("sub").asString();
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

    public String getId() {
        return id;
    }

    public String getFullName() {
        return givenName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getUsername() {
        return username;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public boolean isTutor() {
        return isTutor;
    }

    public long getExpiryDateInSeconds() {
        return expiryDateInSeconds;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getExpiryDate_ISO8601() {
        return expiryDate_ISO8601;
    }
}
