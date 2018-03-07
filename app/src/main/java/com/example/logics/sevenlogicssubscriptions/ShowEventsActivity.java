package com.example.logics.sevenlogicssubscriptions;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowEventsActivity extends AppCompatActivity{

    private String date;
    private ListView eventList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);

        eventList = (ListView) findViewById(R.id.eventList);
        Intent intent = getIntent();
        date = intent.getStringExtra("date");

        Map<String, String> postData = new HashMap<>();
        postData.put("UserID", getSharedPreferences("Login", 0).getString("UserID", null));
        postData.put("custName", date);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event)parent.getItemAtPosition(position);
                getDetailedEventInfo(event);
            }
        });
        new GetUserEventsAsyncTask(postData).execute();
    }

    private void getDetailedEventInfo(Event event){
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

    private class GetUserEventsAsyncTask extends AsyncTask<String, Void, String>{
        JSONObject postData;

        public GetUserEventsAsyncTask(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = new URL("http://10.0.2.2:8080/RESTfulCRUD/rest/familyOrganizer/getAllEvents");
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
        protected void onPostExecute(String result) {
            try{
                JSONObject obj = new JSONObject(result);
                ArrayList<Event> events = new ArrayList<>();
                if(String.valueOf(obj.get("CalendarEvent")).charAt(0) == '['){
                    JSONArray jsonArray = obj.getJSONArray("CalendarEvent");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        Event newEvent = new Event(item.getString("eventID"), item.getString("title"), item.getString("desc"), (item.getString("notifyByEmail")),
                                item.getString("eventUserSelectedDate"), item.getString("userID"));
                        events.add(newEvent);
                    }
                }
                else{
                    JSONObject eventJsonObject = obj.getJSONObject("CalendarEvent");
                    events.add(new Event(eventJsonObject.getString("eventID"), eventJsonObject.getString("title"), eventJsonObject.getString("desc"),
                            eventJsonObject.getString("notifyByEmail"), eventJsonObject.getString("eventUserSelectedDate"), eventJsonObject.getString("userID")));
                }
                ArrayAdapter<Event> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.activity_listview, events);
                eventList.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
