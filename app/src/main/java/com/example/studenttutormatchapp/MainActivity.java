package com.example.studenttutormatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttutormatchapp.helpers.Credentials;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView user;
    TextView password;
    Context context;

    /* API interface */
    UserService apiInterface;
    JSONObject loginResponse;

    /* Shared Pref */
    SharedPreferences jwtFile;
    SharedPreferences.Editor jwtFileEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.userName);
        password = findViewById(R.id.Password);

        jwtFile = getSharedPreferences("jwt", 0);
        jwtFileEditor = jwtFile.edit();

        apiInterface = APIUtils.getUserService();
        context = this;
    }

    public void login(View v){
        Call<ResponseBody> call  = apiInterface.loginUser(new Credentials(user.getText().toString(), password.getText().toString()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    storeJWT(response);
                    moveToDashboard();

                    Toast.makeText(context, "Successfully logged in", Toast.LENGTH_SHORT).show();
                }
                switch (response.code()){
                    case 400:
                        Log.d("Login_debug", "Error: "+ response.code() + " Request body was invalid");
                        Toast.makeText(context, "Please enter Username/ Password", Toast.LENGTH_SHORT).show();
                        break;
                    case 401:
                        Log.d("Login_debug", "Invalid API key");
                        break;
                    case 403:
                        Toast.makeText(context, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                        Log.d("Login_debug", "Invalid login");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Login_debug", t.getMessage());
            }
        });

    }

    public void storeJWT(Response<ResponseBody> response){
        try {
            loginResponse = new JSONObject(response.body().string());
            jwtFileEditor.putString("JWT", loginResponse.getString("jwt"));
            jwtFileEditor.apply();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void moveToDashboard(){
        Intent activity = new Intent(context, DashboardActivity.class);
        startActivity(activity);
    }
}