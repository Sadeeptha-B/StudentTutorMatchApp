package com.example.studenttutormatchapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studenttutormatchapp.BidAdditionalInfo;
import com.example.studenttutormatchapp.BidInfoForm;
import com.example.studenttutormatchapp.Offer;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.model.Bid;
import com.example.studenttutormatchapp.model.Competency;
import com.example.studenttutormatchapp.model.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.BidService;
import com.example.studenttutormatchapp.remote.UserService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeOfferFormActivity extends AppCompatActivity {

    Bid bid;
    BidInfoForm offerForm;
    private String userName;
    private String competencyLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer_form);

        /* Get data*/
        Gson gson = new Gson();
        Intent intent = getIntent();
        bid = gson.fromJson(intent.getStringExtra("bidJson"), Bid.class);
        String userId = intent.getExtras().getString("userId");

        Log.d("OFFER", bid.getId());
        getUser(userId);


        /*UI elements*/
        offerForm = new BidInfoForm(this,R.id.spinnerMakeOfferRate,R.id.spinnerMakeOfferDay,R.id.makeOfferTime);
        offerForm.setPrefRateField(R.id.editTextMakeOfferRate);
        fillForm();
    }

    private void fillForm(){
        TextView tVOfferSubject = findViewById(R.id.textViewMakeOfferSubj);
        TextView tvBidType = findViewById(R.id.textViewMakeOfferBidType);
        TextView tVComp = findViewById(R.id.textViewMakeOfferStuPrefComp);
        TextView tVRateType = findViewById(R.id.textViewMakeOfferStuPrefRateType);
        TextView tVDay = findViewById(R.id.textViewMakeOfferStuPrefDay);
        TextView tVRate = findViewById(R.id.textViewMakeOfferStuPrefRate);

        tVOfferSubject.setText(bid.getSubject().getDescription() + " | " + bid.getSubject().getName());
        tvBidType.setText(bid.getType());
        tVComp.setText(bid.getAdditionalInfo().getCompetency());
        tVRateType.setText(bid.getAdditionalInfo().getRateType());
        tVDay.setText(bid.getAdditionalInfo().getPreferredDateTime());
        tVRate.setText(bid.getAdditionalInfo().getPreferredRate());
    }


    private void getUser(String userId){
        UserService apiUserService = APIUtils.getUserService();
        Call<User> userCall = apiUserService.getUserSubject(userId);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userName = response.body().getUserName();
                List<Competency> competencies = response.body().getCompetencies();
                for (int i=0; i<competencies.size(); i++){
                    if (competencies.get(i).getSubject().getId().equals(bid.getSubject().getId())){
                        competencyLevel = competencies.get(i).getLevel().toString();

                        /*Fill competency*/
                        TextView tVTutComp = findViewById(R.id.textViewMakeOfferComp);
                        tVTutComp.setText(competencyLevel);
                    }
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });

    }

    public void makeOffer(View v){
        boolean valid = offerForm.nonEmptyValidation(new TextView[]{offerForm.getDayPicker(), offerForm.getPrefRateField()});
        if (valid){
            postOffer();
        }
    }

    private void postOffer(){
        String competency = competencyLevel;
        String rateType = offerForm.getRateTypeSpinner().getSelectedItem().toString();
        String prefTime = offerForm.getDaySelectionSpinner().getSelectedItem().toString() + " " + offerForm.getDayPicker().getText().toString();
        String prefRate = offerForm.getPrefRateField().getText().toString();

        EditText descField = findViewById(R.id.editTextMakeOfferDesc);
        String desc = descField.getText().toString();

        Offer offer = new Offer(competency, userName, rateType, prefTime, prefRate, desc);
        bid.getAdditionalInfo().addOffer(offer);


        BidService apiBidService = APIUtils.getBidService();
        Call<Bid> makeOfferCall = apiBidService.updateBid(bid.getId(), bid.getAdditionalInfo());
        makeOfferCall.enqueue(new Callback<Bid>() {
            @Override
            public void onResponse(Call<Bid> call, Response<Bid> response) {
                Log.d("OFFER", bid.getAdditionalInfo().toString());
//                Log.d("OFFER", response.body().getAdditionalInfo().getOffers().toString());
                Gson gson = new Gson();
                String msg = gson.toJson(response.body());
                Log.d("OFFER", msg);
                for (int i=0; i<response.body().getAdditionalInfo().getOffers().size(); i++){

                }

            }

            @Override
            public void onFailure(Call<Bid> call, Throwable t) {

            }
        });
    }
}