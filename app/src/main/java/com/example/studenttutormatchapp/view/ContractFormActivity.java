package com.example.studenttutormatchapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.BidInfoForm;
import com.example.studenttutormatchapp.helpers.ContractAdditionalInfo;
import com.example.studenttutormatchapp.helpers.ContractLessonInfo;
import com.example.studenttutormatchapp.helpers.ContractPaymentInfo;
import com.example.studenttutormatchapp.helpers.Offer;
import com.example.studenttutormatchapp.model.pojo.Contract;
import com.example.studenttutormatchapp.model.pojo.Qualification;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.ContractService;
import com.example.studenttutormatchapp.remote.dao.UserService;
import com.google.gson.Gson;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Math.pow;

public class ContractFormActivity extends AppCompatActivity {

    private Offer offer;
    SharedPreferences userSp;
    BidInfoForm contractForm;

    private Spinner contractExpiry;

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

        contractExpiry = findViewById(R.id.spinnerContractExpiry);
        ArrayAdapter<CharSequence> rateAdapter = ArrayAdapter.createFromResource(this, R.array.contract_duration, android.R.layout.simple_spinner_item);
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contractExpiry.setAdapter(rateAdapter);
        contractExpiry.setSelection(1);
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

        int expiry = 3 *(int) pow(2, contractExpiry.getSelectedItemPosition());

        String daySelection = contractForm.getDaySelectionSpinner().getSelectedItem().toString();
        String timeStr = time.getText().toString();

        TextView[] nonEmptyFields = new TextView[]{rateField, time};
        if (contractForm.nonEmptyValidation(nonEmptyFields)){
            postContract(rate, rateType, daySelection, timeStr, expiry);
        }
    }

    private void postContract(String rate, String rateType, String daySelection, String timeStr, int expiry){
        ZonedDateTime dateOpened = ZonedDateTime.now();
        String dateCreatedStr = dateOpened.format(DateTimeFormatter.ISO_INSTANT);
        String dateExpiredStr = dateOpened.plus(expiry, ChronoUnit.MONTHS).format(DateTimeFormatter.ISO_INSTANT);

        ContractPaymentInfo contractPaymentInfo = new ContractPaymentInfo(rate, rateType);
        ContractLessonInfo contractLessonInfo = new ContractLessonInfo(daySelection, timeStr);
        ContractAdditionalInfo additionalInfo = new ContractAdditionalInfo(false, true);

        String studentId = userSp.getString("USER_ID", "0");
        Contract contract = new Contract(offer.getTutorId(), studentId, offer.getSubjectId(), dateCreatedStr, dateExpiredStr, contractPaymentInfo, contractLessonInfo, additionalInfo);

        ContractService contractService = APIUtils.getContractService();
        Call<Contract> call = contractService.createContract(contract);
        call.enqueue(new Callback<Contract>() {
            @Override
            public void onResponse(Call<Contract> call, Response<Contract> response) {
               onSuccess();
            }

            @Override
            public void onFailure(Call<Contract> call, Throwable t) {

            }
        });
    }

    private void onSuccess(){
        Toast.makeText(this, "Contract signed", Toast.LENGTH_LONG).show();
        finish();
    }
}