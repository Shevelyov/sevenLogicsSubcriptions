package com.example.logics.sevenlogicssubscriptions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Spinner subscriptionSpinner = (Spinner) findViewById(R.id.subscriptionSpinner);
        List<String> subcriptionList = new ArrayList<>();
        subcriptionList.add("Free (default)");
        subcriptionList.add("Premium");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subcriptionList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        subscriptionSpinner.setAdapter(arrayAdapter);
        subscriptionSpinner.setSelection(0);
    }

}
