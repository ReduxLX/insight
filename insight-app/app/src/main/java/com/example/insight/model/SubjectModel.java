package com.example.insight.model;

import org.json.JSONException;
import org.json.JSONObject;

public class SubjectModel {
    private String id;
    private String name;
    private String description;

    public SubjectModel(JSONObject subject) {
        try{
            id = subject.getString("id");
            name = subject.getString("name");
            name = subject.getString("name");
            description = subject.getString("description");

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "SubjectModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
