package com.example.insight.model.Bid;

import com.example.insight.model.SubjectModel;
import com.example.insight.model.User.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


/**
 * Model class which represent the Bid Object
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

    public String getSubjectAndCompetencyStr(){
        return String.format(Locale.getDefault(),
                "%s (%s)", subject.getName(),
                additionalInfo.getStudentOffer().getCompetencyStr());
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateClosedDown() {
        return dateClosedDown;
    }

    public UserModel getInitiator() {
        return initiator;
    }

    public SubjectModel getSubject() {
        return subject;
    }

    public BidAdditionalInfoModel getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public String toString() {
        return "BidModel{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateClosedDown='" + dateClosedDown + '\'' +
                ", initiator=" + initiator +
                ", subject=" + subject +
                ", additionalInfo=" + additionalInfo +
                '}';
    }
}
