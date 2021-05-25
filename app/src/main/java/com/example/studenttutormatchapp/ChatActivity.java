package com.example.studenttutormatchapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.Adapters.ChatAdapter;
import com.example.studenttutormatchapp.helpers.MessageAdditionalInfo;
import com.example.studenttutormatchapp.helpers.MessageComparator;
import com.example.studenttutormatchapp.model.pojo.Bid;
import com.example.studenttutormatchapp.model.pojo.Message;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.BidService;
import com.example.studenttutormatchapp.remote.dao.MessageService;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    String bidId;
    String userId;

    String receiverId;
    String receiverName;

    BidService APIBidInterface;
    MessageService APIMessageInterface;

    ChatAdapter adapter;

    Toolbar toolbar;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        Intent page = getIntent();

        Bundle myExtras = page.getExtras();

        bidId = myExtras.getString("bid_id");
        userId = myExtras.getString("user_id");

        APIBidInterface = APIUtils.getBidService();
        APIMessageInterface = APIUtils.getMessageService();

        adapter = new ChatAdapter(userId);
        getMessages();

        recyclerView = findViewById(R.id.chatRecycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }

    public void sendMessage(View v){
        EditText message = findViewById(R.id.MessageTextField);
        String dateTime = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
        MessageAdditionalInfo additionalInfo = new MessageAdditionalInfo(receiverId, receiverName);
        Call<Message> call =  APIMessageInterface.createMessage(new Message(bidId, userId, dateTime, message.getText().toString(), additionalInfo));

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()){
                    adapter.addMessage(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
        message.setText("");

    }

    public void getMessages(){
        Call<Bid> call = APIBidInterface.getBidWithMessages(bidId);
        toolbar = findViewById(R.id.chatToolbar);
        call.enqueue(new Callback<Bid>() {
            @Override
            public void onResponse(Call<Bid> call, Response<Bid> response) {
                List<Message> messages = response.body().getMessages();
                Collections.sort(messages,new MessageComparator());
                if (response.isSuccessful()){
                    adapter.setMessages(messages);
                    adapter.notifyDataSetChanged();
                    Message msg = messages.get(0);
                    if (msg.getPoster().getId().equals(userId)){
                        receiverId = msg.getAdditionalInfo().getReceiver();
                        receiverName = msg.getAdditionalInfo().getReceiverName();
                                toolbar.setTitle(receiverName);
                    }
                    else {
                        toolbar.setTitle(msg.getPoster().getUserName());
                        receiverId = msg.getPoster().getId();
                        receiverName = msg.getPoster().getUserName();

                    }
                }
            }

            @Override
            public void onFailure(Call<Bid> call, Throwable t) {

            }
        });
    }
}
