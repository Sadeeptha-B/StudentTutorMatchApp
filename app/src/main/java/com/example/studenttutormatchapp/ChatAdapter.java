package com.example.studenttutormatchapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.model.Message;

import java.util.Collections;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    List<Message> messages;
    String userId;
    boolean sent;

    public ChatAdapter(String userId){
        this.userId = userId;
        this.sent = false;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getPoster().getId().equals(userId)){
            sent = true;
            return 0;
        }
        sent = false;
        return 1;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                View viewSent = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sent_card, parent, false);
                return new ViewHolder(viewSent);
            case 1:
                View viewReceived = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_receive_card, parent, false);
                return new ViewHolder(viewReceived);
        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        holder.textView.setText(messages.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (messages == null)
            return 0;
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (sent){
                textView = itemView.findViewById(R.id.messageSent);
            }
            else {
                textView = itemView.findViewById(R.id.messageReceived);
            }
        }
    }

    public void setMessages(List<Message> messages) {
        Collections.reverse(messages);
        this.messages = messages;
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }
}
