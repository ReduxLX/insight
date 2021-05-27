package com.example.insight.model.Competency;

import com.example.insight.model.SubjectModel;
import com.example.insight.model.User.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

public class CompetencyModel {
    private String id;
    private UserModel owner;
    private SubjectModel subject;
    private int level;

    public CompetencyModel(JSONObject competency) {
        try {
            id = competency.getString("id");
            owner = new UserModel(competency.getJSONObject("owner"));
            subject = new SubjectModel(competency.getJSONObject("subject"));
            level = competency.getInt("level");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public UserModel getOwner() {
        return owner;
    }

    public SubjectModel getSubject() {
        return subject;
    }

    public int getLevel() {
        return level;
    }

    public String getLevelStr() {
        return String.valueOf(getLevel());
    }

    @Override
    public String toString() {
        return "CompetencyModel{" +
                "id='" + id + '\'' +
                ", owner=" + owner +
                ", subject=" + subject +
                ", level=" + level +
                '}';
    }
}
