package com.example.logics.sevenlogicssubscriptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
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
    private final static String SUCCESS_MESSAGE  = "You have successfully added new event";
    private final static String FAIL_MESSAGE  = "Something went wrong, please try again";
    private final static String FREE_PLAN = "Free";
    private final static String MONTHLY_PLAN = "Monthly";
    private final static String YEARLY_PLAN = "Yearly";
    private boolean validationSuccess;
    private EditText titleField;
    private EditText descriptionField;
    private EditText timeField;
    private CheckBox notifyByEmailField;
    private String subscriptionType;

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
//                if(subscriptionType.equalsIgnoreCase(MONTHLY_PLAN) || subscriptionType.equalsIgnoreCase(YEARLY_PLAN)){
//                    sendPostRequest();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "This is my Toast message!",
//                            Toast.LENGTH_SHORT).show();
//            }
            checkSubscription();
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
        postData.put("userID", getSharedPreferences("Login", 0).getString("UserID", null));
        new HttpPostAsyncTask(postData).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void checkSubscription(){
        Map<String, String> postData = new HashMap<>();
        postData.put("UserID", getSharedPreferences("Login", 0).getString("UserID", null));

        //check level of subscription
        new CheckSubscriptionPlanAsyncTask(postData).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }


    private void clearUI(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        titleField.setText("");
        descriptionField.setText("");
        titleField.clearFocus();
        descriptionField.clearFocus();
        timeField.clearFocus();
        notifyByEmailField.clearFocus();
        notifyByEmailField.setChecked(false);
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
            String result = "";
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
                    result = HelperUtils.convertStreamToString(inputStream);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String response) {
            //Snackbar resultMsg;
            if(response != null && response.equals("success")){
                clearUI();
                //resultMsg = Snackbar.make(findViewById(R.id.addEventTitle), SUCCESS_MESSAGE, Snackbar.LENGTH_SHORT);
                Toast.makeText(getApplicationContext(), SUCCESS_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                //resultMsg = Snackbar.make(findViewById(R.id.addEventTitle), FAIL_MESSAGE, Snackbar.LENGTH_SHORT);
            }
            //resultMsg.show();
        }
    }

    public class CheckSubscriptionPlanAsyncTask extends AsyncTask<String, Void, String> {
        JSONObject postData;

        public CheckSubscriptionPlanAsyncTask(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL("http://10.0.2.2:8080/RESTfulCRUD/rest/familyOrganizer/checkSubscriptionPlan");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
                if (statusCode == 200) {
                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    result = HelperUtils.convertStreamToString(inputStream);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.isEmpty()) {
                try {
                    JSONObject obj = new JSONObject(result);
                    subscriptionType = obj.getJSONObject("Subscription").getString("subscriptionType");
                    if(subscriptionType.equalsIgnoreCase(MONTHLY_PLAN) || subscriptionType.equalsIgnoreCase(YEARLY_PLAN)){
                        sendPostRequest();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Switch to paid subscription plan to add events.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
