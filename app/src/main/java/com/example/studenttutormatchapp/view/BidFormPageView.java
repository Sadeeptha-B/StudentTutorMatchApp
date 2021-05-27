package com.example.studenttutormatchapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.studenttutormatchapp.R;
import com.example.studenttutormatchapp.helpers.SubjectSpinner;

public class BidFormPageView implements GenericView {

    Context context;
    BidFormActivity.BidFormController controller;
    View rootView;

    private TextView subjectField;
    private SubjectSpinner subjectSpinner;
    private RadioGroup bidGroup;
//    private BidInfoForm newBidForm;

    public BidFormPageView(Context context, ViewGroup viewGroup) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.bid_form_layout, viewGroup);
        controller = new BidFormActivity.BidFormController(this);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void initViews() {
//        newBidForm = new BidInfoForm(getRootView(),R.id.RateTypeDropdown,R.id.DayDropdown, R.id.makeOfferTime);
        bidGroup = rootView.findViewById(R.id.BidGroup);
        subjectSpinner = rootView.findViewById(R.id.subjectDropdown);
        subjectField = rootView.findViewById(R.id.textViewSubjectBidForm);

        subjectSpinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                TextView textView = findViewById(R.id.textViewSubjectBidForm);
//                textView.setText(subjectSpinner.getSelectedItem().toString());
//                selectionPos = position;
//                newBidForm.getCompetencySpinner().setSelection(competencies.get(0).getLevel() + COMPETENCY_DIFF);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void bindDataToView() {

    }
}
