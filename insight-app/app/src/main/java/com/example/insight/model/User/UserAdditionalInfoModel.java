package com.example.insight.model.User;

import android.util.Log;

import com.example.insight.model.Bid.TutorBidModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserAdditionalInfoModel {
    private ArrayList<String> bookmarkedBidIds = new ArrayList<>();

    public UserAdditionalInfoModel(JSONObject additionalInfo){
        try{
            JSONArray bidIdJSON = additionalInfo.getJSONArray("bookmarkedBidIds");
            for (int i=0; i < bidIdJSON.length(); i++){
                String bidId = bidIdJSON.getString(i);
                bookmarkedBidIds.add(bidId);
            }
        } catch (JSONException e){
            Log.i("print", "No Bookmarked bids");
        }
    }

    public void addBidId(String bidId){
        bookmarkedBidIds.add(bidId);
    }

    public Boolean removeBidId(String targetBidId){
        for(int i=0; i<getBookmarkedBidIds().size(); i++){
            if(getBookmarkedBidIds().get(i).equals(targetBidId)){
                getBookmarkedBidIds().remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean searchBid(String targetBidId){
        for(String bidId: getBookmarkedBidIds()){
            if(bidId.equals(targetBidId)) return true;
        }
        return false;
    }

    public ArrayList<String> getBookmarkedBidIds() {
        return bookmarkedBidIds;
    }

    public JSONObject parseIntoJSON(){
        JSONObject json = new JSONObject();
        try{
            JSONArray bidIdArray = new JSONArray();
            for(String bidId: bookmarkedBidIds){
                bidIdArray.put(bidId);
            }
            json.put("bookmarkedBidIds", bidIdArray);
        } catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    };

    @Override
    public String toString() {
        return "UserAdditionalInfoModel{" +
                "bookmarkedBidIds=" + bookmarkedBidIds +
                '}';
    }
}
