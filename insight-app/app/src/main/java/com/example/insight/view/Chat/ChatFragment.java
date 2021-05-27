package com.example.insight.view.Chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.model.JWTModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Fragment class for the Chat room screen
 */
public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private SharedPreferences prefs;

    private Button buttonSendChat;
    private EditText editTextMessage;

    private String currentBidId;
    private String recipientName;
    private String recipientId;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = root.findViewById(R.id.rv_chat);
        prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);

        // Get current bid id from navigation params
        ChatFragmentArgs navArgs = ChatFragmentArgs.fromBundle(getArguments());
        currentBidId = navArgs.getCurrentBidId();
        recipientName = navArgs.getRecipientName();
        recipientId = navArgs.getRecipientId();

        buttonSendChat = root.findViewById(R.id.buttonSendChat);
        editTextMessage = root.findViewById(R.id.editTextMessage);

        editTextMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    postMessage();
                }
                return false;
            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recipientName);

        buttonSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessage();
            }
        });
        // Render current bid's messages
        getMessages();

        return root;
    }


    private void getMessages(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "ChatFragment: "+"Get Message Success for "+currentBidId);
                JSONArray messages = (JSONArray) response;
                ChatAdapter adapter = new ChatAdapter(getActivity(), messages, currentBidId, recipientId);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    private void postMessage(){
        String content = editTextMessage.getText().toString();
        editTextMessage.setText("");
        if(TextUtils.isEmpty(content)){
            return;
        }

        JSONObject jsonBody = new JSONObject();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String datePosted = ISO8601.format(currentTime);
        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);
        try{
            JSONObject additionalInfo = new JSONObject();
            additionalInfo.put("recipientId", recipientId);
            jsonBody.put("bidId", currentBidId);
            jsonBody.put("posterId", jwtModel.getId());
            jsonBody.put("datePosted", datePosted);
            jsonBody.put("content", content);
            jsonBody.put("additionalInfo", additionalInfo);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Log.i("print", "ChatFragment: "+"Post Message Success");
                    Toast.makeText(getActivity(), "Message posted", Toast.LENGTH_SHORT).show();
                    getMessages();
                }
                @Override
                public void onError(String message) {
                    Log.i("print", "ChatFragment: "+"Post Message Failed "+ message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            };

            Log.i("print", "ChatFragment: "+jsonBody.toString());
            VolleyUtils.makeJSONObjectRequest(
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
