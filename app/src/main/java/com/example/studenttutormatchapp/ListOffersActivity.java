package com.example.studenttutormatchapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

public class ListOffersActivity extends AppCompatActivity {

    ListOffersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offers);

        Intent page = getIntent();
        Bundle bundle = page.getExtras();

        String bidId = bundle.getString("bidId");
        String subjectDescription = bundle.getString("subject");

        Toolbar toolbar = findViewById(R.id.OffersToolbar);
        toolbar.setTitle("Offers on "+ subjectDescription);

        adapter = new ListOffersAdapter();
    }
}