package com.example.logics.sevenlogicssubscriptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class AddCalendarEventActivity extends AppCompatActivity {

    private String date;
    final private static String SUCCESS_MESSAGE  = "You have successfully added new event";
    final private static String FAIL_MESSAGE  = "Something went wrong, please try again";
    private boolean validationSuccess;
    private EditText titleField;
    private EditText descriptionField;
    private EditText timeField;
    private CheckBox notifyByEmailField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar_event);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        titleField = (EditText) findViewById(R.id.addEventTitle);
        descriptionField = (EditText) findViewById(R.id.addEventDescription);
        notifyByEmailField = (CheckBox) findViewById(R.id.addEventNotifyByEmail);
        timeField = (EditText) findViewById(R.id.addEventTime);
    }

    @Override
    protected void onStart() {
        super.onStart();
        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDialog dialog = new TimeDialog();
                dialog.setEventTime(timeField);
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                titleField.clearFocus();
                dialog.show(ft, "TimePicker");
            }
        });
    }

    public void onSave(View view){
        String message = validateEventInput();
        Snackbar resultMsg = null;
        if(validationSuccess){
            sendPostRequest();
        }
        else {
            resultMsg = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            resultMsg.show();
        }
    }

    private String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateSplitted[] = date.split("-");
        int year = Integer.parseInt(dateSplitted[0]);
        int month = Integer.parseInt(dateSplitted[1]) - 1;
        int day = Integer.parseInt(dateSplitted[2]);

        String timeSplitted[] = timeField.getText().toString().split(":");
        int hours = Integer.parseInt(timeSplitted[0]);
        int minutes = Integer.parseInt(timeSplitted[1]);
        Calendar calendar = new GregorianCalendar(year, month, day, hours, minutes);

        return DateUtilsHelper.formatDate(calendar, sdf);
    }

    private String validateEventInput(){
        StringBuffer failMsg = new StringBuffer();
        validationSuccess = true;

        if(titleField.getText().toString().isEmpty()){
            failMsg.append("Please, fill out following fields: Title ");
            validationSuccess = false;
        }
        return failMsg.toString();
    }

    public void sendPostRequest(){
        Map<String, String> postData = new HashMap<>();
        postData.put("title", titleField.getText().toString());
        postData.put("desc", descriptionField.getText().toString());
        postData.put("notifyByEmail", notifyByEmailField.isChecked() ? "true" : "false");
        postData.put("eventUserSelectedDate", getTime());
        postData.put("username", String.valueOf(1));
        new HttpPostAsyncTask(postData).execute();
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private class HttpPostAsyncTask extends AsyncTask<String, Void, String>{
        JSONObject postData;

        public HttpPostAsyncTask(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = new URL("http://10.0.2.2:8080/RESTfulCRUD/rest/familyOrganizer/addEvent");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.connect();

                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                int statusCode = connection.getResponseCode();
                if (statusCode ==  200) {
                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    return convertStreamToString(inputStream);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            Snackbar resultMsg;
            if(response != null && response.equals("success")){
                resultMsg = Snackbar.make(findViewById(R.id.addEventTitle), SUCCESS_MESSAGE, Snackbar.LENGTH_SHORT);
            }
            else{
                resultMsg = Snackbar.make(findViewById(R.id.addEventTitle), FAIL_MESSAGE, Snackbar.LENGTH_SHORT);
            }
            resultMsg.show();
        }
    }
}
