package com.example.insight.model;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageModel {
    private String id;
    private String bidId;
    private String datePosted;
    private String dateLastEdited;
    private String content;
    private JSONObject additionalInfo;
    private UserModel poster;

    public MessageModel(JSONObject message) {
        try{
            id = message.getString("id");
            bidId = message.getString("bidId");
            datePosted = message.getString("datePosted");
            dateLastEdited = message.getString("dateLastEdited");
            content = message.getString("content");
            additionalInfo = message.getJSONObject("additionalInfo");
            poster = new UserModel(message.getJSONObject("poster"));
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

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getDateLastEdited() {
        return dateLastEdited;
    }

    public void setDateLastEdited(String dateLastEdited) {
        this.dateLastEdited = dateLastEdited;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public JSONObject getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(JSONObject additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public UserModel getPoster() {
        return poster;
    }

    public void setPoster(UserModel poster) {
        this.poster = poster;
    }
}
