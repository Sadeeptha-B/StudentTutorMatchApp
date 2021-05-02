package com.example.studenttutormatchapp.helpers;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.studenttutormatchapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BidInfoForm {
    public Activity activity;

    private Spinner competencySpinner;
    private Spinner rateTypeSpinner;
    private Spinner daySelectionSpinner;
    private TextView dayPicker;

    private EditText prefRateField;

    public BidInfoForm(Activity _activity,int rateId, int daySelectId, int dayPickId){
        this.activity = _activity;
        setRateTypeSpinner(rateId);
        setDaySelectionSpinner(daySelectId);
        setTimePickerForTextView(dayPickId);
    }

    public void setCompetencySpinner(int spinnerId){
        competencySpinner = activity.findViewById(spinnerId);
        List<String> list = new ArrayList<String>();
        for (int i=0; i<=10; i++){
            list.add(String.valueOf(i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        competencySpinner.setAdapter(dataAdapter);
    }

    public void setRateTypeSpinner(int spinnerId){
        rateTypeSpinner = activity.findViewById(spinnerId);
        ArrayAdapter<CharSequence> rateAdapter = ArrayAdapter.createFromResource(activity, R.array.rate_types, android.R.layout.simple_spinner_item);
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rateTypeSpinner.setAdapter(rateAdapter);
    }

    public void setDaySelectionSpinner(int spinnerId){
        daySelectionSpinner = activity.findViewById(spinnerId);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(activity, R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySelectionSpinner.setAdapter(dayAdapter);
    }

    public void setTimePickerForTextView(int textViewId){
        dayPicker = activity.findViewById(textViewId);
        dayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        dayPicker.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.show();
            }
        });
    }

    public void setPrefRateField(int rateFieldId){
        prefRateField = activity.findViewById(rateFieldId);
    }

    public boolean nonEmptyValidation(TextView[] nonEmptyFields){
        boolean flag = true;

        for (int i=0; i<nonEmptyFields.length; i++){
            boolean isEmpty = nonEmptyFields[i].getText().toString().isEmpty();
            if (isEmpty){
                nonEmptyFields[i].setError("Please fill this field");
            }
            flag = flag && !isEmpty;
        }
        return flag;
    }

    public Spinner getCompetencySpinner() {
        return competencySpinner;
    }

    public Spinner getRateTypeSpinner() {
        return rateTypeSpinner;
    }

    public Spinner getDaySelectionSpinner() {
        return daySelectionSpinner;
    }

    public TextView getDayPicker() {
        return dayPicker;
    }

    public EditText getPrefRateField() {
        return prefRateField;
    }

}
