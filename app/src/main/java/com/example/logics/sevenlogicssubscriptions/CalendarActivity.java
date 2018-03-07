package com.example.logics.sevenlogicssubscriptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {

    private String date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Calendar currentDate = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = DateUtilsHelper.formatDate(currentDate, sdf);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                date = DateUtilsHelper.formatDate(calendar, sdf);
            }
        });

    }

    public void onClickAddEvent(View view){
        Intent intent = new Intent(this, AddCalendarEventActivity.class);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    public void onClickShowEvents(View view){
        Intent intent = new Intent(this, ShowEventsActivity.class);
        intent.putExtra("date", date);
        startActivity(intent);
    }
}
