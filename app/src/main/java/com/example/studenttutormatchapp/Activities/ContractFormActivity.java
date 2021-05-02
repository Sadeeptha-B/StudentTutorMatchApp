package com.example.studenttutormatchapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studenttutormatchapp.BidInfoForm;
import com.example.studenttutormatchapp.ContractAdditionalInfo;
import com.example.studenttutormatchapp.ContractLessonInfo;
import com.example.studenttutormatchapp.ContractPaymentInfo;
import com.example.studenttutormatchapp.Offer;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.model.Contract;
import com.example.studenttutormatchapp.model.Qualification;
import com.example.studenttutormatchapp.model.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.UserService;
import com.google.gson.Gson;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractFormActivity extends AppCompatActivity {

    private Offer offer;
    SharedPreferences userSp;
    BidInfoForm contractForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_form);

        userSp = getSharedPreferences("id", 0);

        Gson gson = new Gson();
        Intent intent = getIntent();
        offer = gson.fromJson(intent.getExtras().getString("offerJson"), Offer.class);

        getInfo(offer.getTutorId());

        contractForm = new BidInfoForm(this, R.id.spinnerContractRate, R.id.spinnerContractDay, R.id.contractTime);
    }


    private void getInfo(String tutorId){
        UserService apiUserService = APIUtils.getUserService();
        Call<User> call = apiUserService.getQualifications(tutorId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String userName =response.body().getUserName();
                String qualificationsString = "";
                List<Qualification> qualifications = response.body().getQualifications();
                for (int i=0; i<qualifications.size(); i++){
                    qualificationsString += qualifications.get(i).getTitle() + " ";
                }
                fillInfo(userName, qualificationsString);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void fillInfo(String userName,String qualificationsStr){
        TextView student = findViewById(R.id.textViewContractStudent);
        TextView tutor = findViewById(R.id.textViewContractTutor);
        TextView subject = findViewById(R.id.textViewContractSubj);
        TextView competency = findViewById(R.id.textViewContractComp);
        TextView qualification = findViewById(R.id.textViewContractQualification);

        student.setText(userSp.getString("USERNAME", "unavailable"));
        tutor.setText(userName);
        subject.setText(offer.getSubject());
        competency.setText(offer.getCompetency());
        qualification.setText(qualificationsStr);
    }

    public void signContract(View v){
        EditText rateField = findViewById(R.id.editTextContractRate);
        TextView time = findViewById(R.id.contractTime);
        String rateType = contractForm.getRateTypeSpinner().getSelectedItem().toString();
        String rate = rateField.getText().toString();

        String daySelection = contractForm.getDaySelectionSpinner().getSelectedItem().toString();
        String timeStr = time.getText().toString();

        TextView[] nonEmptyFields = new TextView[]{rateField, time};
        if (contractForm.nonEmptyValidation(nonEmptyFields)){
            postContract(rate, rateType, daySelection, timeStr);
        }
    }

    private void postContract(String rate, String rateType, String daySelection, String timeStr){
        ZonedDateTime dateOpened = ZonedDateTime.now();
        String dateCreatedStr = dateOpened.format(DateTimeFormatter.ISO_INSTANT);
        String dateExpiredStr = dateOpened.plus(1, ChronoUnit.HOURS).format(DateTimeFormatter.ISO_INSTANT);

        ContractPaymentInfo contractPaymentInfo = new ContractPaymentInfo(rate, rateType);
        ContractLessonInfo contractLessonInfo = new ContractLessonInfo(daySelection, timeStr);
        ContractAdditionalInfo additionalInfo = new ContractAdditionalInfo(true, false);

        String studentId = userSp.getString("USER_ID", "0");
        Contract contract = new Contract(studentId, offer.getTutorId(),offer.getSubjectId(), dateCreatedStr, dateExpiredStr, contractPaymentInfo, contractLessonInfo, additionalInfo);



    }
}