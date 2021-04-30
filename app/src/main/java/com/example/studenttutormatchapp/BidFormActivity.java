package com.example.studenttutormatchapp;

import android.app.TimePickerDialog;
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
    private int selectionPos;

    UserService apiUserInterface;
    Context context;

    /* Fields : Used to extract data*/
    private TextView subjectField;
    private Spinner competencyField;
    private Spinner daySpinner;
    private TextView preferredTimeField;
    private Spinner rateTypeSpinner;
    private EditText preferredRateField;
    private EditText descriptionField;

    private SubjectSpinner subjectSpinner;
    private RadioGroup bidGroup;

    private static final int COMPETENCY_DIFF = 2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_form_layout);
        context = this;

        SharedPreferences sharedPreferences = getSharedPreferences("id", 0);
        userID = sharedPreferences.getString("USER_ID", "");

        apiUserInterface = APIUtils.getUserService();
        getUserSubjects();
        setUIElements();
    }


    public void getUserSubjects(){
        Call<User> call = apiUserInterface.getUserSubject(userID);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                competencies = response.body().getCompetencies();
                for (int i = 0; i < competencies.size(); i++){
                    Subject subject = competencies.get(i).getSubject();
                    subjectStrings.add(subject.getDescription() + " | " + subject.getName());

                }

                if (subjectStrings.isEmpty()){
                    Toast.makeText(context, "You cannot make a bid without a chosen subject.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("CHECK","Response:" + t.getMessage());
            }
        });
    }

    public void setUIElements(){
        /*Fields*/
        subjectField = findViewById(R.id.textViewSubjectBidForm);
        preferredTimeField = findViewById(R.id.TimeField);
        preferredRateField = findViewById(R.id.RateField);
        descriptionField = findViewById(R.id.DescriptionField);

        //RadioGroup
        bidGroup = findViewById(R.id.BidGroup);
        bidGroup.check(R.id.OpenBidBtn);

        /*Spinners*/
        //Subject
        subjectSpinner = findViewById(R.id.subjectDropdown);
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                subjectStrings);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        subjectSpinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = findViewById(R.id.textViewSubjectBidForm);
                textView.setText(subjectSpinner.getSelectedItem().toString());
                selectionPos = position;
                competencyField.setSelection(competencies.get(0).getLevel() + COMPETENCY_DIFF);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //RateType
        rateTypeSpinner = findViewById(R.id.RateTypeDropdown);
        ArrayAdapter<CharSequence> rateAdapter = ArrayAdapter.createFromResource(this, R.array.rate_types, android.R.layout.simple_spinner_item);
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rateTypeSpinner.setAdapter(rateAdapter);

        //Day of the week
        daySpinner = findViewById(R.id.DayDropdown);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        //Competency
        competencyField = findViewById(R.id.CompetencyField);
        List<String> list = new ArrayList<String>();
        for (int i=0; i<=10; i++){
            list.add(String.valueOf(i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        competencyField.setAdapter(dataAdapter);

        // Listener for TimePicker
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
        boolean returnFlag = false;

        TextView[] nonEmptyFields = new TextView[]{subjectField, preferredTimeField, preferredRateField};

        for (int i=0; i<nonEmptyFields.length; i++){
            if (nonEmptyFields[i].getText().toString().isEmpty()){
                nonEmptyFields[i].setError("Please fill this field");
                return;
            }
        }
        Bid bid = createBidClass();
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
                }
            }

            @Override
            public void onFailure(Call<Bid> call, Throwable t) {
                Log.d("Bidding_debug", t.getMessage());
            }
        });
    }

    public Bid createBidClass(){
        String dateOpenedStr;
        String dateClosedStr;
        String bidType = "open"; /*Default*/

        String competency = competencyField.getSelectedItem().toString();
        String preferredDate = daySpinner.getSelectedItem().toString() + " " + preferredTimeField.getText().toString();
        String rateType = rateTypeSpinner.getSelectedItem().toString();
        String preferredRate = preferredRateField.getText().toString();
        String description = descriptionField.getText().toString();
        BidFormAdditionalInfo additionalInfo = new BidFormAdditionalInfo(competency, preferredDate, rateType, preferredRate,description);

        ZonedDateTime dateOpened = ZonedDateTime.now();
        dateOpenedStr = dateOpened.format(DateTimeFormatter.ISO_INSTANT);
        dateClosedStr = dateOpened.plus(30, ChronoUnit.MINUTES).format(DateTimeFormatter.ISO_INSTANT); /*Default*/


        if (bidGroup.getCheckedRadioButtonId() == R.id.OpenBidBtn){
            dateClosedStr = dateOpened.plus(30, ChronoUnit.MINUTES).format(DateTimeFormatter.ISO_INSTANT);
            bidType = "open";
        }
        if (bidGroup.getCheckedRadioButtonId() == R.id.ClosedBidBtn){
            dateClosedStr = dateOpened.plus(1, ChronoUnit.WEEKS).format(DateTimeFormatter.ISO_INSTANT);
            bidType = "closed";
        }

        Subject subject = competencies.get(selectionPos).getSubject();
        Bid createdBid = new Bid(bidType, userID ,dateOpenedStr, dateClosedStr, subject, additionalInfo);
        return createdBid;
    }
}
