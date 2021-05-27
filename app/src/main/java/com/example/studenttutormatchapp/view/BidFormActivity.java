package com.example.studenttutormatchapp.view;

import android.content.Context;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.studenttutormatchapp.MyApplication;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.BidAdditionalInfo;
import com.example.studenttutormatchapp.helpers.BidInfoForm;
import com.example.studenttutormatchapp.helpers.Offer;
import com.example.studenttutormatchapp.helpers.SubjectSpinner;
import com.example.studenttutormatchapp.model.pojo.Bid;
import com.example.studenttutormatchapp.model.pojo.Competency;
import com.example.studenttutormatchapp.model.pojo.Subject;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.BidService;
import com.example.studenttutormatchapp.remote.dao.UserService;
import com.example.studenttutormatchapp.remote.response.ApiResource;
import com.example.studenttutormatchapp.viewmodel.BidFormViewModel;
import com.example.studenttutormatchapp.viewmodel.ViewModelFactory;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidFormActivity extends AppCompatActivity {

    private String userID;
    private List<Competency> competencies;
    private List<String> subjectStrings = new ArrayList<>();
    private int selectionPos;

    UserService apiUserInterface;
    Context context;

    private BidInfoForm newBidForm;

    /* Fields : Used to extract data*/
    private TextView subjectField;
    private SubjectSpinner subjectSpinner;
    private RadioGroup bidGroup;

    private static final int COMPETENCY_DIFF = 2;

    @Inject
    ViewModelFactory viewModelFactory;
    BidFormViewModel bidFormViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_form_layout);
        context = this;

        ((MyApplication) getApplication()).getAppComponent().inject(this);
        bidFormViewModel = new ViewModelProvider(this, viewModelFactory).get(BidFormViewModel.class);

        userID = bidFormViewModel.getUserData().getUserId();

        apiUserInterface = APIUtils.getUserService();

        bidFormViewModel.getUserWithSubjects().observe(this, new Observer<ApiResource<User>>() {
            @Override
            public void onChanged(ApiResource<User> userApiResource) {
                switch (userApiResource.getStatus()){
                    case SUCCESS:
                        prepSpinner(bidFormViewModel.getSubjectStrings(userApiResource.getData()));
                        if (subjectStrings.isEmpty()){
                            Toast.makeText(context, "You cannot make a bid without a chosen subject.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        break;
                    case ERROR:
                        break;
                }
            }
        });

        setUIElements();

        bidFormViewModel.createBid().observe(this, new Observer<ApiResource<Bid>>() {
            @Override
            public void onChanged(ApiResource<Bid> bidApiResource) {
                switch (bidApiResource.getStatus()){
                    case SUCCESS:
                        Toast.makeText(context, "Bid created successfully", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR:
                        Toast.makeText(context, "Unable to make Bid. Please contact administrator", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    private void prepSpinner(List<String> strings){
        subjectStrings.addAll(strings);
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

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subjectStrings);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        subjectSpinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = findViewById(R.id.textViewSubjectBidForm);
                textView.setText(subjectSpinner.getSelectedItem().toString());
                selectionPos = position;
                newBidForm.getCompetencySpinner().setSelection(bidFormViewModel.getCompetencyList().get(position).getLevel() + COMPETENCY_DIFF);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void provideData(View v){
        TextView[] nonEmptyFields = new TextView[]{subjectField, newBidForm.getDayPicker(), newBidForm.getPrefRateField()};

        if (newBidForm.nonEmptyValidation(nonEmptyFields)){
            bidFormViewModel.getBidData(obtainData());
        }
    }


    public HashMap<String, String> obtainData(){
        String bidType = "open";

        String competency = newBidForm.getCompetencySpinner().getSelectedItem().toString();
        String preferredDate = newBidForm.getDaySelectionSpinner().getSelectedItem().toString() + " " + newBidForm.getDayPicker().getText().toString();
        String rateType = newBidForm.getRateTypeSpinner().getSelectedItem().toString();
        String preferredRate = newBidForm.getPrefRateField().getText().toString();

        if (bidGroup.getCheckedRadioButtonId() == R.id.OpenBidBtn){
            bidType = "open";
        }
        if (bidGroup.getCheckedRadioButtonId() == R.id.ClosedBidBtn){
            bidType = "closed";
        }
        Subject subject = bidFormViewModel.getCompetencyList().get(selectionPos).getSubject();

        HashMap<String, String> data = new HashMap<>();
        data.put("competency", competency);
        data.put("preferredDate", preferredDate);
        data.put("rateType", rateType);
        data.put("preferredRate", preferredRate);
        data.put("bidType", bidType);
        data.put("subject", new Gson().toJson(subject));
       return data;
    }
}
