package com.example.insight.view.Chat;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insight.R;
import com.example.insight.model.JWTModel;
import com.example.insight.model.MessageModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Adapter class used to populate the chat recycler view
 * which is responsible for displaying chat messages between the user and a second party
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private SharedPreferences prefs;

    private ArrayList<MessageModel> messageArray = new ArrayList<>();

    ChatAdapter(Context ctx, JSONArray messages, String currentBidId, String recipientId){
        context = ctx;
        prefs = ctx.getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);
        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);
        try{
            for (int i=messages.length()-1 ; i >= 0; i--) {
                JSONObject messageObj = messages.getJSONObject(i);
                MessageModel message = new MessageModel(messageObj);
                String posterId = message.getPoster().getId();
                String myId = jwtModel.getId();

                // My messages are: 1. Posted using my id 2. Has my id as recipientId stored in additionalDetails
                boolean myMessages = posterId.equals(myId) && message.getRecipientId().equals(recipientId);
                // Their messages are: 1. Posted using their id 2. Has their id as recipientId stored in additionalDetails
                boolean theirMessages = posterId.equals(recipientId) && message.getRecipientId().equals(myId);

                boolean messagePostedByRecipient = posterId.equals(recipientId);
                // A message belongs to this chatroom if it's for the same bid and sent for each other exclusively
                if(currentBidId.equals(message.getBidId()) && (myMessages || theirMessages)){
                    messageArray.add(message);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_chat, parent,false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        final MessageModel message = messageArray.get(position);

        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);
        String posterId = message.getPoster().getId();
        String posterName = message.getPoster().getFullName();
        if(posterId.equals(jwtModel.getId())){
            posterName = "You";
        }

        holder.name.setText(posterName);
        holder.content.setText(message.getContent());
    }

    @Override
    public int getItemCount() {
        return messageArray.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView name, content;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_user_chat);
            content = itemView.findViewById(R.id.tv_message_chat);

        }
    }
}
