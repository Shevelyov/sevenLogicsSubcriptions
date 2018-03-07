package com.example.logics.sevenlogicssubscriptions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    final private static String SUCCESS_MESSAGE  = "You have successfully signed up";
    final private static String FAIL_MESSAGE  = "Something went wrong, please try again";
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText usernameField;
    private EditText passwordField;
    private EditText repeatPasswordField;
    private Spinner subscriptionSpinner;
    private String subscriptionId;
    boolean validation;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstNameField = (EditText) findViewById(R.id.firstName);
        lastNameField = (EditText) findViewById(R.id.lastName);
        emailField = (EditText) findViewById(R.id.email);
        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        repeatPasswordField = (EditText) findViewById(R.id.passwordRep);
        subscriptionSpinner = (Spinner) findViewById(R.id.subscriptionSpinner);

        //execute background task to fill the spinner from DB
        new GetSubscriptionsAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        subscriptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subscription subscription = (Subscription) parent.getSelectedItem();
                subscriptionId = subscription.getSubscriptionId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onSignUp(View view){
        View focusView = validateSignUp();
        if(!validation){
            focusView.requestFocus();
        }
        else{
            sendPostRequest();
        }

    }


    private View validateSignUp(){
        ArrayList<String> validationList = new ArrayList<>();
        View focusView = null;
        validation = true;

        if(firstNameField.getText().toString().isEmpty()){
            firstNameField.setError("This field is required");
            focusView = firstNameField;
            validation = false;
        }
        if(lastNameField.getText().toString().isEmpty()){
            lastNameField.setError("This field is required");
            focusView = lastNameField;
            validation = false;
        }
        if(emailField.getText().toString().isEmpty()){
            emailField.setError("This field is required");
            focusView = emailField;
            validation = false;
        }
        if(usernameField.getText().toString().isEmpty()){
            usernameField.setError("This field is required");
            focusView = usernameField;
            validation = false;
        }
        if(passwordField.getText().toString().isEmpty()){
            passwordField.setError("This field is required");
            focusView = passwordField;
            validation = false;
        }
        if(repeatPasswordField.getText().toString().isEmpty()){
            repeatPasswordField.setError("This field is required");
            focusView = repeatPasswordField;
            validation = false;
        }
        if(!passwordField.getText().toString().equals(repeatPasswordField.getText().toString())){
            repeatPasswordField.setError("Password do not match");
            focusView = repeatPasswordField;
            validation = false;
        }
        return focusView;
    }

    public void sendPostRequest(){
        Map<String, String> postData = new HashMap<>();
        postData.put("userName", usernameField.getText().toString());
        postData.put("pwd", passwordField.getText().toString());
        postData.put("custName", firstNameField.getText().toString());
        postData.put("familyName", lastNameField.getText().toString());
        postData.put("emailID", emailField.getText().toString());
        postData.put("subscriptionType", subscriptionId);
        new SignUpUserAsyncTask(postData).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private class GetSubscriptionsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String inputLine;
            try {
                URL url = new URL("http://10.0.2.2:8080/RESTfulCRUD/rest/familyOrganizer/getSubscriptionData");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject obj = new JSONObject(result);
                ArrayList<Subscription> subscriptions = new ArrayList<>();
                JSONArray jsonArray = obj.getJSONArray("Subscription");
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject subscription = jsonArray.getJSONObject(i);
                    subscriptions.add(new Subscription(subscription.getString("subscriptionType"), subscription.getString("subscriptionID")));
                }
                ArrayAdapter<Subscription> adapter = new ArrayAdapter<Subscription>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subscriptions);
                subscriptionSpinner.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class SignUpUserAsyncTask extends AsyncTask<String, Void, String>{
        JSONObject postData;

        public SignUpUserAsyncTask(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }
        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL("http://10.0.2.2:8080/RESTfulCRUD/rest/familyOrganizer/addUser");
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
                    return HelperUtils.convertStreamToString(inputStream);
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
            if(response != null){
                resultMsg = Snackbar.make(findViewById(R.id.signUpLayout), SUCCESS_MESSAGE, Snackbar.LENGTH_SHORT);
            }
            else{
                resultMsg = Snackbar.make(findViewById(R.id.signUpLayout), FAIL_MESSAGE, Snackbar.LENGTH_SHORT);
            }
            resultMsg.show();
        }
    }
}



