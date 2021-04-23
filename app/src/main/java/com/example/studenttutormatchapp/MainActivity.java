package com.example.studenttutormatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.UserService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView user;
    TextView password;
    UserService apiInterface;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        user = findViewById(R.id.userName);
        password = findViewById(R.id.Password);

        apiInterface = APIUtils.getUserService();
    }

    public void login(View v){

        Call<ResponseBody> call  = apiInterface.loginUser(new Credentials(user.getText().toString(), password.getText().toString()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Intent activity = new Intent(context, DashboardActivity.class);
                    startActivity(activity);

                    Toast.makeText(context, "Successfully logged in", Toast.LENGTH_SHORT).show();
                }
                switch (response.code()){
                    case 400:
                        Log.d("Login debug", "Request body was invalid");
                        Toast.makeText(context, "Please enter Username/ Password", Toast.LENGTH_SHORT).show();
                        break;
                    case 401:
                        Log.d("Login debug", "Invalid API key");
                        break;
                    case 403:
                        Toast.makeText(context, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                        Log.d("Login debug", "Invalid login");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Login debug", t.getMessage());
            }
        });

    }
}