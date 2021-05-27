package com.example.studenttutormatchapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttutormatchapp.MyApplication;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.viewmodel.LoginViewModel;
import com.example.studenttutormatchapp.viewmodel.ViewModelFactory;
import com.example.studenttutormatchapp.helpers.Credentials;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.UserService;
import com.example.studenttutormatchapp.remote.response.ApiResource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView user;
    TextView password;
    Context context;

    /* API interface */
    UserService apiInterface;
    JSONObject loginResponse;

    /* Shared Pref */
    SharedPreferences jwtFile;
    SharedPreferences.Editor jwtFileEditor;

    @Inject
    ViewModelFactory viewModelFactory;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.userName);
        password = findViewById(R.id.Password);

        ((MyApplication) getApplication()).getAppComponent().inject(this);

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        getLifecycle().addObserver(loginViewModel);

        jwtFile = getSharedPreferences("jwt", 0);
        jwtFileEditor = jwtFile.edit();

        apiInterface = APIUtils.getUserService();
        context = this;


        loginViewModel.loginHandle.observe(this, new Observer<ApiResource<ResponseBody>>() {
            @Override
            public void onChanged(ApiResource<ResponseBody> loginResponse) {
                switch (loginResponse.getStatus()){
                    case SUCCESS:
                        onLoginSuccess(loginResponse.getData());
                        try {
                            Log.d("CHECK", loginResponse.getData().string());
                            JSONObject response = new JSONObject(loginResponse.getData().string());
                            Log.d("CHECK", response.getString("jwt"));
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ERROR:
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    public void onLoginClick(View v){
        loginViewModel.login(new Credentials(user.getText().toString(), password.getText().toString()));
    }

    public void onLoginSuccess(ResponseBody response){
        loginViewModel.onLoginSuccess(response);
        Toast.makeText(context, "Successfully logged in", Toast.LENGTH_SHORT).show();
        moveToDashboard();
    }

    public void storeJWT(Response<ResponseBody> response){
        try {
            loginResponse = new JSONObject(response.body().string());
            Log.d("CHECK", loginResponse.getString("jwt"));
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