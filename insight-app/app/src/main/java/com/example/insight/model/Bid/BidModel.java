package com.example.insight.model.Bid;

import com.example.insight.model.SubjectModel;
import com.example.insight.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class used to destructure Bid JSON Object
 */
public class BidModel {
    private String id;
    private String type;
    private String dateCreated;
    private String dateClosedDown;
    private UserModel initiator;
    private SubjectModel subject;
    private BidAdditionalInfoModel additionalInfo;

    public BidModel(JSONObject bid) {
        try{
            id = bid.getString("id");
            type = bid.getString("type");
            dateCreated = bid.getString("dateCreated");
            dateClosedDown = bid.getString("dateClosedDown");
            initiator = new UserModel(bid.getJSONObject("initiator"));
            subject = new SubjectModel(bid.getJSONObject("subject"));
            additionalInfo = new BidAdditionalInfoModel(bid.getJSONObject("additionalInfo"));
            dateClosedDown = bid.getString("dateClosedDown");
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateClosedDown() {
        return dateClosedDown;
    }

    public void setDateClosedDown(String dateClosedDown) {
        this.dateClosedDown = dateClosedDown;
    }

    public UserModel getInitiator() {
        return initiator;
    }

    public void setInitiator(UserModel initiator) {
        this.initiator = initiator;
    }

    public SubjectModel getSubject() {
        return subject;
    }

    public void setSubject(SubjectModel subject) {
        this.subject = subject;
    }

    public BidAdditionalInfoModel getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(BidAdditionalInfoModel additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
