package com.example.insight.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.model.MessageModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private String currentBidId;
    // TODO: Replace this with real user's id
    private String userId = "ecc52cc1-a3e4-4037-a80f-62d3799645f4";

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        // Get current bid id from navigation params
        ChatFragmentArgs navArgs = ChatFragmentArgs.fromBundle(getArguments());
        currentBidId = navArgs.getCurrentBidId();

        // Render current bid's messages
        getMessages();

        // TODO: Hook this up to the send button
        // (Debug) Post new message
        // postMessage("Or maybe increasing the hours per week?");

        return root;
    }

    private void getMessages(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "ChatFragment: "+"Get Message Success for "+currentBidId);
                JSONArray messages = (JSONArray) response;
                try{
                    for (int i=0 ; i < messages.length(); i++) {
                        JSONObject messageObj = messages.getJSONObject(i);
                        MessageModel message = new MessageModel(messageObj);

                        // Filter messages that belong to this bid
                        // Use userId to identify "me" posts
                        if(message.getBidId() != null && currentBidId.equals(message.getBidId())){
                            // TODO: Render messages into the UI
                            Log.i("console_log", message.getContent());
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                Log.i("print", "ChatFragment: "+"Get Message Failed "+message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJSONArrayRequest(
            getActivity(),
            "message",
            Request.Method.GET,
            new JSONObject(),
            listener
        );
    }

    private void postMessage(String content){
        JSONObject jsonBody = new JSONObject();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String datePosted = ISO8601.format(currentTime);

        try{
            jsonBody.put("bidId", currentBidId);
            jsonBody.put("posterId", userId);
            jsonBody.put("datePosted", datePosted);
            jsonBody.put("content", content);
            jsonBody.put("additionalInfo", new JSONObject());

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Log.i("print", "ChatFragment: "+"Post Message Success");
                    Toast.makeText(getActivity(), "Message posted", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onError(String message) {
                    Log.i("print", "ChatFragment: "+"Post Message Failed "+ message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            };

            Log.i("console_log", jsonBody.toString());
            VolleyUtils.makeJsonObjectRequest(
                getActivity(),
                "message",
                Request.Method.POST,
                jsonBody,
                listener
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
