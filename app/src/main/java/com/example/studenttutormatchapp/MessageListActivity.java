package com.example.studenttutormatchapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttutormatchapp.model.Message;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.MessageService;

import org.json.JSONException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    MessageListAdapter adapter;

    MessageService APImsgInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list_layout);

        recyclerView = findViewById(R.id.msgListRecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences userIdFile = getSharedPreferences("id", 0);

        adapter = new MessageListAdapter(this, userIdFile.getString("USER_ID", ""));
        recyclerView.setAdapter(adapter);

        APImsgInterface = APIUtils.getMessageService();

        getMessages();
    }
    public void getMessages(){
        SharedPreferences userIdFile = getSharedPreferences("id", 0);

        Call<List<Message>> messageCall = APImsgInterface.getMessages();

        messageCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {

                List<Message> messages = response.body();

                    for (int i = 0; i < messages.size(); i++){
                        Message msg = messages.get(i);
                        boolean isSender = msg.getPoster().getId().equals(userIdFile.getString("USER_ID", ""));
                        boolean isReceiver = msg.getAdditionalInfo().getReceiver().equals(userIdFile.getString("USER_ID", ""));

                        if (isSender || isReceiver){
                            adapter.addMessage(msg);
                            adapter.notifyDataSetChanged();
                        }
                    }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }

}
