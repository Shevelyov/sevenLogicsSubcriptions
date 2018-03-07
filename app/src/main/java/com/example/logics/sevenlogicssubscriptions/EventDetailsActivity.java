package com.example.logics.sevenlogicssubscriptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_details);

        Intent intent = getIntent();
        Event event = (Event) intent.getParcelableExtra("event");

        String eventId = event.getEventId();
        String eventTitle = event.getEventTitle();
        String eventDesc = event.getEventDescription();
        String notifyEvent = event.getNotifyByEmail();
        String eventDate = event.getEventDate();
        String userId = event.getUserId();
        EditText eventTitleText = (EditText) findViewById(R.id.eventName);
        EditText eventDescriptionText = (EditText) findViewById(R.id.eventDesc);
        EditText eventDateText = (EditText) findViewById(R.id.eventDate);
        CheckBox eventNotifyBox = (CheckBox) findViewById(R.id.eventNotify);

        eventTitleText.setText(eventTitle);
        eventTitleText.setFocusable(false);
        eventDescriptionText.setText(eventDesc);
        eventDescriptionText.setFocusable(false);
        eventDateText.setText(eventDate);
        eventDateText.setFocusable(false);
        eventNotifyBox.setChecked(Boolean.valueOf(notifyEvent));
        eventNotifyBox.setFocusable(false);
    }
}
