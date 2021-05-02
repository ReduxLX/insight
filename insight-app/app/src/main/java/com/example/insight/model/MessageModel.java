package com.example.insight.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Model class which represent the Message object
 */
public class MessageModel {
    private String id;
    private String bidId;
    private String datePosted;
    private String dateLastEdited;
    private String content;
    private JSONObject additionalInfo;
    private String recipientId;
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
            recipientId = additionalInfo.getString("recipientId");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getBidId() {
        return bidId;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public String getDateLastEdited() {
        return dateLastEdited;
    }

    public String getContent() {
        return content;
    }

    public JSONObject getAdditionalInfo() {
        return additionalInfo;
    }

    public UserModel getPoster() {
        return poster;
    }

    public String getRecipientId() {
        return recipientId;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "id='" + id + '\'' +
                ", bidId='" + bidId + '\'' +
                ", datePosted='" + datePosted + '\'' +
                ", dateLastEdited='" + dateLastEdited + '\'' +
                ", content='" + content + '\'' +
                ", additionalInfo=" + additionalInfo +
                ", poster=" + poster +
                '}';
    }
}
