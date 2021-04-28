package com.example.studenttutormatchapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studenttutormatchapp.model.*;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.BidService;
import com.example.studenttutormatchapp.remote.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidFormActivity extends AppCompatActivity {

    private String userID;

    private List<Competency> competencies = new ArrayList<>();
    private ArrayList<String> subjectStrings = new ArrayList<>();

    UserService apiUserInterface;

    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_form_layout);

        context = this;

        SharedPreferences sharedPreferences = getSharedPreferences("id", 0);
        userID = sharedPreferences.getString("USER_ID", "");

        apiUserInterface = APIUtils.getUserService();
        getUserSubjects();
        createSubjectDropdown();
    }

    public void getUserSubjects(){
        Call<User> call = apiUserInterface.getStudentSubject(userID);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                List<Competency> competencies = response.body().getCompetencies();
                for (int i = 0; i < competencies.size(); i++){
                    Subject subject = competencies.get(i).getSubject();
                    subjectStrings.add(subject.getDescription() + " - " + subject.getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Bidding_debug","Response:" + t.getMessage());

            }
        });
    }

    public void createSubjectDropdown(){
        Spinner subjectSpinner = findViewById(R.id.subjectDropdown);
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectStrings);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(parent.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        subjectSpinner.setAdapter(subjectAdapter);
    }

    public void createBid(View v){
        BidService apiBidService = APIUtils.getBidService();

        Call<Bid> bidCall = apiBidService.createBid(createBidClass());
        bidCall.enqueue(new Callback<Bid>() {
            @Override
            public void onResponse(Call<Bid> call, Response<Bid> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Bid created successfully" + response.message(), Toast.LENGTH_LONG).show();
//                    finish();
                }
                try {
                    Log.d("Bidding_debug", response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Bid> call, Throwable t) {
                Log.d("Bidding_debug", t.getMessage());
            }
        });
    }

    public Bid createBidClass(){
        Spinner subjects = findViewById(R.id.subjectDropdown);

        EditText qualification = findViewById(R.id.QualificationsField);
        EditText preferredTime = findViewById(R.id.TimeField);

        EditText preferredRate = findViewById(R.id.RateField);
        EditText description = findViewById(R.id.DescriptionField);

        String bidType = "open";

        RadioGroup bidGroup = findViewById(R.id.BidGroup);

        Calendar calendar = Calendar.getInstance();

        Date dateOpened = calendar.getTime();
        calendar.add(Calendar.MINUTE, 30);
        Date dateClosing = calendar.getTime();


        if (bidGroup.getCheckedRadioButtonId() == R.id.ClosedBidBtn){
            calendar.add(Calendar.MINUTE, -30);
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
            dateClosing = calendar.getTime();
            bidType = "closed";

        }

        Toast.makeText(context, dateClosing.toString(), Toast.LENGTH_LONG).show();
        Subject subject = competencies.get(0).getSubject();
        Bid createdBid = new Bid(bidType,userID ,dateOpened, dateOpened, subject);
        return createdBid;

    }
}

// Bid creation is bugged(Date time formatting passing in initiator ID)
// Messages might only be a one time thing 
// If student look for all bids that they've created using initiator
// If tutor look for offers using 