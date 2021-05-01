package com.example.studenttutormatchapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.Activities.ChatActivity;
import com.example.studenttutormatchapp.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    List<Message> messages  = new ArrayList<>();
    Context context;

    String userId;
    public MessageListAdapter(Context context, String userId){
        this.context = context;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MessageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListAdapter.ViewHolder holder, int position) {
        String posterId = messages.get(position).getPoster().getId();
        if (posterId.equals(userId)){
            holder.name.setText(messages.get(position).getAdditionalInfo().getReceiverName());
        }
        else {
            holder.name.setText(messages.get(position).getPoster().getUserName());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(v.getContext(), ChatActivity.class);
                activity.putExtra("bid_id", messages.get(position).getBidId());
                activity.putExtra("user_id", userId);
                context.startActivity(activity);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (messages == null)
            return 0;
        return messages.size();
    }

    public void addMessage(Message message){
        messages.add(message);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            name = itemView.findViewById(R.id.name);

        }
    }
}
