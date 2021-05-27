package com.example.studenttutormatchapp.view;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.Adapters.MessageListAdapter;
import com.example.studenttutormatchapp.MyApplication;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.model.pojo.Message;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.MessageService;
import com.example.studenttutormatchapp.viewmodel.MessageListViewModel;
import com.example.studenttutormatchapp.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    MessageListAdapter adapter;

    @Inject
    ViewModelFactory viewModelFactory;
    MessageListViewModel messageListViewModel;

    MessageService APImsgInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list_layout);

        recyclerView = findViewById(R.id.msgListRecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ((MyApplication) getApplication()).getAppComponent().inject(this);
        messageListViewModel = new ViewModelProvider(this, viewModelFactory).get(MessageListViewModel.class);


        adapter = new MessageListAdapter(this, messageListViewModel.getUserData().getUserId());
        recyclerView.setAdapter(adapter);

        APImsgInterface = APIUtils.getMessageService();

        getMessages();
    }
    public void getMessages(){

        Call<List<Message>> messageCall = APImsgInterface.getMessages();

        messageCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {

                List<Message> messages = response.body();
                List<String> contacts = new ArrayList<>();
                    for (int i = 0; i < messages.size(); i++){
                        Message msg = messages.get(i);

                        String senderID = msg.getPoster().getId();
                        String receiverID = msg.getAdditionalInfo().getReceiver();

                        String userID = messageListViewModel.getUserData().getUserId();

                        boolean isSender = senderID.equals(userID);
                        boolean isReceiver = receiverID.equals(userID);
                        if ((isSender || isReceiver) && !(contacts.contains(senderID) || contacts.contains(receiverID))){

                            adapter.addMessage(msg);
                            adapter.notifyDataSetChanged();
                        }
                        if (isSender){
                            contacts.add(msg.getAdditionalInfo().getReceiver());
                        }
                        else if (isReceiver){
                            contacts.add(msg.getPoster().getId());
                        }

                    }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }

}
