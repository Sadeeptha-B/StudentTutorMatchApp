package com.example.studenttutormatchapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.Spinner;

public class SubjectSpinner extends androidx.appcompat.widget.AppCompatSpinner {
    AdapterView.OnItemSelectedListener listener;
    public SubjectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            listener.onItemSelected(this, getSelectedView(), position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            AdapterView.OnItemSelectedListener listener) {
        this.listener = listener;
    }
}