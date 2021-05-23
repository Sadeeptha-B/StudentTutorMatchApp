package com.example.studenttutormatchapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studenttutormatchapp.helpers.BidAdditionalInfo;
import com.example.studenttutormatchapp.helpers.BidInfoForm;
import com.example.studenttutormatchapp.helpers.Offer;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.SubjectSpinner;
import com.example.studenttutormatchapp.model.*;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.BidService;
import com.example.studenttutormatchapp.remote.UserService;
import com.example.studenttutormatchapp.view.BidFormPageView;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    private BidInfoForm newBidForm;

    /* Fields : Used to extract data*/
    private TextView subjectField;

    private SubjectSpinner subjectSpinner;
    private RadioGroup bidGroup;

    private static final int COMPETENCY_DIFF = 2;

    BidFormPageView bidFormPageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bidFormPageView = new BidFormPageView(this, null);
        setContentView(bidFormPageView.getRootView());
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
        newBidForm = new BidInfoForm(this,R.id.RateTypeDropdown,R.id.DayDropdown, R.id.makeOfferTime);
        newBidForm.setCompetencySpinner(R.id.CompetencyField);
        newBidForm.setPrefRateField(R.id.RateField);
        subjectField = findViewById(R.id.textViewSubjectBidForm);

        //RadioGroup
        bidGroup = findViewById(R.id.BidGroup);
        bidGroup.check(R.id.OpenBidBtn);

        //Subject spinner
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
                newBidForm.getCompetencySpinner().setSelection(competencies.get(0).getLevel() + COMPETENCY_DIFF);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void validateData(View v){
        TextView[] nonEmptyFields = new TextView[]{subjectField, newBidForm.getDayPicker(), newBidForm.getPrefRateField()};

        if (newBidForm.nonEmptyValidation(nonEmptyFields)){
            Bid bid = createBidClass();
            createBid(bid);
        }
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
        String bidType = "open";

        String competency = newBidForm.getCompetencySpinner().getSelectedItem().toString();
        String preferredDate = newBidForm.getDaySelectionSpinner().getSelectedItem().toString() + " " + newBidForm.getDayPicker().getText().toString();
        String rateType = newBidForm.getRateTypeSpinner().getSelectedItem().toString();
        String preferredRate = newBidForm.getPrefRateField().getText().toString();
        ArrayList<Offer> offerData = new ArrayList<Offer>();

        BidAdditionalInfo additionalInfo = new BidAdditionalInfo(competency, preferredDate, rateType, preferredRate, offerData);


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
