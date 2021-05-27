package com.example.studenttutormatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttutormatchapp.helpers.BidAdditionalInfo;
import com.example.studenttutormatchapp.helpers.BidAdditionalInfoWrapper;
import com.example.studenttutormatchapp.helpers.BidInfoForm;
import com.example.studenttutormatchapp.helpers.MessageAdditionalInfo;
import com.example.studenttutormatchapp.helpers.Offer;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.model.Bid;
import com.example.studenttutormatchapp.model.Competency;
import com.example.studenttutormatchapp.model.Message;
import com.example.studenttutormatchapp.model.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.BidService;
import com.example.studenttutormatchapp.remote.UserService;
import com.google.gson.Gson;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeOfferFormActivity extends AppCompatActivity {

    Bid bid;
    BidInfoForm offerForm;
    private String userName;
    private String competencyLevel;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer_form);

        /* Get data*/
        Gson gson = new Gson();
        Intent intent = getIntent();
        bid = gson.fromJson(intent.getStringExtra("bidJson"), Bid.class);
        userId = intent.getExtras().getString("userId");

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

        TextView tVsubject = findViewById(R.id.textViewMakeOfferSubj);
        String subject = tVsubject.getText().toString();

        List<Offer> Offers = bid.getAdditionalInfo().getOffers();
        Offer offer = new Offer(competency, userId, userName, bid.getSubject().getId(), subject, rateType, prefTime, prefRate, desc);

        bid.getAdditionalInfo().getOffers().stream().filter(o -> o.getTutorId().equals(userId)).findFirst().orElse(null);
        String toastText = "";
        boolean isRenewOffer = false;
        for (int i = 0; i < Offers.size(); i++){
            if (Offers.get(i).getTutorId().equals(userId)) {
                Offers.remove(i);
                Offers.add(i, offer);
                bid.getAdditionalInfo().setOffers(Offers);
                isRenewOffer = true;
                toastText = "Your Offer has been updated";
                break;
            }

        }
        if (!isRenewOffer) {
            bid.getAdditionalInfo().addOffer(offer);
            toastText = "Your bid offer has been submitted";
        }



        BidAdditionalInfoWrapper wrapper = new BidAdditionalInfoWrapper(bid.getAdditionalInfo());

        BidService apiBidService = APIUtils.getBidService();
        Call<Bid> makeOfferCall = apiBidService.updateBid(bid.getId(), wrapper);
        String finalToastText = toastText;
        makeOfferCall.enqueue(new Callback<Bid>() {
            @Override
            public void onResponse(Call<Bid> call, Response<Bid> response) {

                if (response.isSuccessful() && bid.getType().equals("closed")){
                    String[] splitDate = offer.getOfferedDate().split(" ");

                    ZonedDateTime datePosted = ZonedDateTime.now();
                    String datePostedStr = datePosted.format(DateTimeFormatter.ISO_INSTANT);
                    String msgContent = "Hi I am " + offer.getTutorName() + ", a level " + offer.getCompetency()
                            + " competent in "+ bid.getSubject().getDescription() + " I can conduct lessons on "
                            + splitDate[0] + " at " + splitDate[1] + " for a rate of " + offer.getOfferedRate() + ", "+ offer.getRateType();

                    MessageAdditionalInfo additionalInfo = new MessageAdditionalInfo(bid.getInitiator().getId(), bid.getInitiator().getUserName());

                    Message message = new Message(bid.getId(), userId, datePostedStr, msgContent, additionalInfo);
                    sendMessage(message);
                }
                offerSuccess(finalToastText);
            }

            @Override
            public void onFailure(Call<Bid> call, Throwable t) {
            }
        });
    }

    private void offerSuccess(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }

    public void sendMessage(Message message){
        Call<Message> call = APIUtils.getMessageService().createMessage(message);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
            }
        });
    }
}
