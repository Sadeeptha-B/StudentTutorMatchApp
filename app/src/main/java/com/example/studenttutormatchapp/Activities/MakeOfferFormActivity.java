package com.example.studenttutormatchapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studenttutormatchapp.BidFormAdditionalInfo;
import com.example.studenttutormatchapp.BidInfoForm;
import com.example.studenttutormatchapp.Offer;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.model.Bid;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.BidService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeOfferFormActivity extends AppCompatActivity {

    Bid bid;
    BidInfoForm offerForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer_form);

        Gson gson = new Gson();
        bid = gson.fromJson(getIntent().getStringExtra("bidJson"), Bid.class);

        offerForm = new BidInfoForm(this, R.id.spinnerMakeOfferComp,R.id.spinnerMakeOfferRate,R.id.spinnerMakeOfferDay,R.id.makeOfferTime);
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

    public void makeOffer(View v){
        boolean valid = offerForm.nonEmptyValidation(new TextView[]{offerForm.getDayPicker(), offerForm.getPrefRateField()});
        if (valid){
            postOffer();
        }
    }

    private void postOffer(){
        String competency = offerForm.getCompetencySpinner().getSelectedItem().toString();
        String rateType = offerForm.getRateTypeSpinner().getSelectedItem().toString();
        String prefTime = offerForm.getDaySelectionSpinner().getSelectedItem().toString() + " " + offerForm.getDayPicker().getText().toString();
        String prefRate = offerForm.getPrefRateField().getText().toString();

        EditText descField = findViewById(R.id.editTextMakeOfferDesc);
        String desc = descField.getText().toString();

        Offer offer = new Offer(competency, rateType, prefTime, prefRate, desc);
        bid.getAdditionalInfo().addOffer(offer);

        BidService apiBidService = APIUtils.getBidService();
        Call<BidFormAdditionalInfo> makeOfferCall = apiBidService.updateBid(bid.getId(), bid.getAdditionalInfo());
        makeOfferCall.enqueue(new Callback<BidFormAdditionalInfo>() {
            @Override
            public void onResponse(Call<BidFormAdditionalInfo> call, Response<BidFormAdditionalInfo> response) {
                Log.d("OFFER", String.valueOf(response.code()));
                Log.d("OFFER", response.body().getOffers().toString());
            }

            @Override
            public void onFailure(Call<BidFormAdditionalInfo> call, Throwable t) {

            }
        });
    }
}