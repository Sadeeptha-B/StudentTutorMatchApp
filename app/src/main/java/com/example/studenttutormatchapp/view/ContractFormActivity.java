package com.example.studenttutormatchapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studenttutormatchapp.MyApplication;
import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.BidInfoForm;
import com.example.studenttutormatchapp.helpers.ContractAdditionalInfo;
import com.example.studenttutormatchapp.helpers.ContractLessonInfo;
import com.example.studenttutormatchapp.helpers.ContractPaymentInfo;
import com.example.studenttutormatchapp.helpers.CustomSpinner;
import com.example.studenttutormatchapp.helpers.Offer;

import com.example.studenttutormatchapp.model.pojo.Competency;
import com.example.studenttutormatchapp.model.pojo.Contract;
import com.example.studenttutormatchapp.model.pojo.Qualification;
import com.example.studenttutormatchapp.model.pojo.User;
import com.example.studenttutormatchapp.remote.APIUtils;
import com.example.studenttutormatchapp.remote.dao.CompetencyService;
import com.example.studenttutormatchapp.remote.dao.ContractService;
import com.example.studenttutormatchapp.remote.dao.UserService;
import com.example.studenttutormatchapp.viewmodel.ContractFormViewModel;
import com.example.studenttutormatchapp.viewmodel.ViewModelFactory;
import com.google.gson.Gson;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Math.pow;

public class ContractFormActivity extends AppCompatActivity {

    private Offer offer;
    private String tutorId;
    private String offeredSubject;
    private String offeredSubjectId;
    private String tutorsCompetency;
    private boolean isRenewal;
    private Contract contract;
    private String contractId;
    BidInfoForm contractForm;

    private Spinner contractExpiry;
    private CustomSpinner tutorSpinner;

    @Inject
    ViewModelFactory viewModelFactory;
    ContractFormViewModel contractFormViewModel;

    private List<User> tutors = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_form);

        ((MyApplication) getApplication()).getAppComponent().inject(this);
        contractFormViewModel = new ViewModelProvider(this, viewModelFactory).get(ContractFormViewModel.class);



        Gson gson = new Gson();
        Intent intent = getIntent();
        offer = gson.fromJson(intent.getExtras().getString("offerJson"), Offer.class);
        tutorId = intent.getExtras().getString("tutorId");
        offeredSubject = intent.getExtras().getString("subjectName");
        offeredSubjectId = intent.getExtras().getString("subjectId");
        tutorsCompetency = intent.getExtras().getString("competency");
        isRenewal = intent.getExtras().getBoolean("isRenewal");

        if (isRenewal) {
            handleRenewalContractUI(intent);
        }

        getInfo(tutorId);

        contractForm = new BidInfoForm(this, R.id.spinnerContractRate, R.id.spinnerContractDay, R.id.contractTime);

        contractExpiry = findViewById(R.id.spinnerContractExpiry);
        ArrayAdapter<CharSequence> rateAdapter = ArrayAdapter.createFromResource(this, R.array.contract_duration, android.R.layout.simple_spinner_item);
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contractExpiry.setAdapter(rateAdapter);
        contractExpiry.setSelection(1);
    }

    private void handleRenewalContractUI(Intent intent){
        getNewTutorList(offeredSubjectId);
        contractId = intent.getExtras().getString("contractId");
        tutorSpinner = findViewById(R.id.tutorSpinner);
        tutorSpinner.setVisibility(View.VISIBLE);

        List<String> tutorNames = getListOfTutorNames(tutors);
        Log.d("OFFER", String.valueOf(tutorNames.size()));
        Log.d("OFFER", String.valueOf(tutors.size()));
        ArrayAdapter<String> tutorAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,tutorNames);
        tutorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tutorSpinner.setAdapter(tutorAdapter);tutorAdapter.notifyDataSetChanged();

        tutorSpinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = findViewById(R.id.textViewContractTutor);
                textView.setText(tutorSpinner.getSelectedItem().toString());
                tutorId = tutors.get(position).getId();
                tutorSpinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

        student.setText(contractFormViewModel.getUserData().getUsername());
        tutor.setText(userName);
        subject.setText(offeredSubject);
        competency.setText(tutorsCompetency);
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
        if (contractForm.nonEmptyValidation(nonEmptyFields))
            contract = createContract(rate, rateType, daySelection, timeStr, expiry);
            if (!isRenewal){
                postContract(contract);
            }
            else {
            patchContract(contract);
        }
    }

    private Contract createContract(String rate, String rateType, String daySelection, String timeStr, int expiry){
        ZonedDateTime dateOpened = ZonedDateTime.now();
        String dateCreatedStr = dateOpened.format(DateTimeFormatter.ISO_INSTANT);
        String dateExpiredStr = dateOpened.plus(expiry, ChronoUnit.MONTHS).format(DateTimeFormatter.ISO_INSTANT);

        ContractPaymentInfo contractPaymentInfo = new ContractPaymentInfo(rate, rateType);
        ContractLessonInfo contractLessonInfo = new ContractLessonInfo(daySelection, timeStr);
        ContractAdditionalInfo additionalInfo = new ContractAdditionalInfo(false, true);

        String studentId = contractFormViewModel.getUserData().getUserId();
        Contract contract = new Contract(tutorId, studentId, offeredSubjectId, dateCreatedStr, dateExpiredStr, contractPaymentInfo, contractLessonInfo, additionalInfo);

        return contract;
    }

    private void patchContract(Contract contract){
        ContractService contractService = APIUtils.getContractService();

        Call<Contract> call = contractService.updateContract(contractId, contract);
        call.enqueue(new Callback<Contract>() {
            @Override
            public void onResponse(Call<Contract> call, Response<Contract> response) {
                onSuccess("Contract renewed");
            }

            @Override
            public void onFailure(Call<Contract> call, Throwable t) {

            }
        });
    }

    private void postContract(Contract contract){
        ContractService contractService = APIUtils.getContractService();
        Call<Contract> call = contractService.createContract(contract);
        call.enqueue(new Callback<Contract>() {
            @Override
            public void onResponse(Call<Contract> call, Response<Contract> response) {
               onSuccess("Contract signed");
            }

            @Override
            public void onFailure(Call<Contract> call, Throwable t) {

            }
        });
    }

    private List<String> getListOfTutorNames(List<User> tutors){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < tutors.size(); i++){
            list.add(tutors.get(i).getUserName());
        }
        return list;
    }

    private void onSuccess(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }

    public void getNewTutorList(String subject){
        CompetencyService apiCompetencyInterface = APIUtils.getCompetencyService();

        Call<List<Competency>> call = apiCompetencyInterface.getCompetencies();

        call.enqueue(new Callback<List<Competency>>() {
            @Override
            public void onResponse(Call<List<Competency>> call, Response<List<Competency>> response) {
                if (response.isSuccessful()){
                    getSuitableTutors(response.body(), subject);
                }
            }

            @Override
            public void onFailure(Call<List<Competency>> call, Throwable t) {

            }
        });

    }

    public void getSuitableTutors(List<Competency> competencies, String subjectId){
        for (int i = 0; i < competencies.size(); i++){
            Competency competency = competencies.get(i);
            if (competency.getOwner().getIsTutor() && competency.getSubject().getId().equals(subjectId))
                tutors.add(competency.getOwner());
        }
    }
}