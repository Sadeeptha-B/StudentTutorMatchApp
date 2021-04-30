package com.example.studenttutormatchapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studenttutormatchapp.model.*;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.BidService;
import com.example.studenttutormatchapp.remote.UserService;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidFormActivity extends AppCompatActivity {

    private String userID;
    private List<Competency> competencies;
    private ArrayList<String> subjectStrings = new ArrayList<>();

    UserService apiUserInterface;
    Context context;

    private EditText competencyField;
    private EditText preferredDateField;
    private TextView preferredTimeField;
    private EditText preferredRateField;
    private EditText descriptionField;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_form_layout);
        context = this;

        competencyField = findViewById(R.id.QualificationsField);
        preferredDateField = findViewById(R.id.DateField);
        preferredTimeField = findViewById(R.id.TimeField);
        preferredRateField = findViewById(R.id.RateField);
        descriptionField = findViewById(R.id.DescriptionField);

        SharedPreferences sharedPreferences = getSharedPreferences("id", 0);
        userID = sharedPreferences.getString("USER_ID", "");

        apiUserInterface = APIUtils.getUserService();
        getUserSubjects();
        setListeners();
    }


    public void getUserSubjects(){
        Call<User> call = apiUserInterface.getUserSubject(userID);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                competencies = response.body().getCompetencies();
                for (int i = 0; i < competencies.size(); i++){
                    Subject subject = competencies.get(i).getSubject();
                    subjectStrings.add(subject.getDescription() + " - " + subject.getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("CHECK","Response:" + t.getMessage());
            }
        });
    }

    public void setListeners(){
        Spinner subjectSpinner = findViewById(R.id.subjectDropdown);
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectStrings);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(parent.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        preferredTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BidFormActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        preferredTimeField.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.show();
            }
        });
    }

    public void validateData(View v){
        //All fields except description should be filled.

        Bid bid =createBidClass();
        createBid(bid);
    }

    private void createBid(Bid bid){
        BidService apiBidService = APIUtils.getBidService();

        Call<Bid> bidCall = apiBidService.createBid(bid);
        bidCall.enqueue(new Callback<Bid>() {
            @Override
            public void onResponse(Call<Bid> call, Response<Bid> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Bid created successfully", Toast.LENGTH_LONG).show();
//                    finish();
                }
            }

            @Override
            public void onFailure(Call<Bid> call, Throwable t) {
                Log.d("Bidding_debug", t.getMessage());
            }
        });
    }

    public Bid createBidClass(){
        ZonedDateTime dateOpened;
        String dateOpenedStr;
        String dateClosedStr;
        Spinner subjects = findViewById(R.id.subjectDropdown);

        String competency = competencyField.getText().toString();
        String preferredDate = preferredDateField.getText().toString();

        Log.d("CHECK", preferredDate);
        String preferredRate = preferredRateField.getText().toString();
        String description = descriptionField.getText().toString();

        /* Default bid Type: Open */
        dateOpened = ZonedDateTime.now();
        dateOpenedStr = dateOpened.format(DateTimeFormatter.ISO_INSTANT);
        dateClosedStr = dateOpened.plus(30, ChronoUnit.MINUTES).format(DateTimeFormatter.ISO_INSTANT);
        String bidType = "open";

        RadioGroup bidGroup = findViewById(R.id.BidGroup);

        if (bidGroup.getCheckedRadioButtonId() == R.id.ClosedBidBtn){
            dateClosedStr = dateOpened.plus(1, ChronoUnit.WEEKS).format(DateTimeFormatter.ISO_INSTANT);
            bidType = "closed";
        }

        Subject subject = competencies.get(0).getSubject();
        BidFormAdditionalInfo additionalInfo = new BidFormAdditionalInfo(competency,preferredDate, preferredRate, description);

        Bid createdBid = new Bid(bidType, userID ,dateOpenedStr, dateClosedStr, subject, additionalInfo);
        return createdBid;
    }

    private void checkIfFieldEmpty(EditText editText){

    }
}



// If student look for all bids that they've created using initiator
// If tutor look for offers using 