package com.example.logics.sevenlogicssubscriptions;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;
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
import java.util.Map;

public class ToDoListActivity extends AppCompatActivity {

    private ListView mTaskListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        mTaskListView = (ListView) findViewById(R.id.list_todo);
//        LayoutInflater layoutInflater = getLayoutInflater();
//        View list_item_view = layoutInflater.inflate(R.layout.activity_listview, null);
//        View view = layoutInflater.inflate(R.layout.item_todo, (ViewGroup)list_item_view , true);
//        Button delete = (Button) view.findViewById(R.id.task_delete);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View parent = (View) v.getParent();
//                TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
//                String task = String.valueOf(taskTextView.getText());
//            }
//        });
        new ToDoListGetAsync().execute();
    }

//    @Override
//    protected void onStart() {
//        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                parent.getSelectedItem();
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    public void onDeleteTask(View view){
//        View parent = (View) view.getParent();
//        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
//        String task = String.valueOf(taskTextView.getText());
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add",   new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                Log.d("TODO ACTIVITY", "Task to add: " + task);
                                sendPostRequest(task);
                                new ToDoListGetAsync().execute();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendPostRequest(String task){
        Map<String, String> postData = new HashMap<>();
        postData.put("title", task);
        postData.put("userID", getSharedPreferences("Login", 0).getString("UserID", null));
        new ToDoListItemStoreAsync(postData).execute();
    }

    private class ToDoListItemStoreAsync extends AsyncTask<String, Void, String>{

        JSONObject postData;

        public ToDoListItemStoreAsync(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                URL url = new URL("http://10.0.2.2:8080/RESTfulCRUD/rest/familyOrganizer/addToDoListItem");
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
    }

    private class ToDoListGetAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try{
                Map<String, String> jsonMap = new HashMap<>();
                jsonMap.put("UserID", getSharedPreferences("Login", 0).getString("UserID", null));
                JSONObject postData = new JSONObject(jsonMap);

                URL url = new URL("http://10.0.2.2:8080/RESTfulCRUD/rest/familyOrganizer/getAllToDoListItems");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.connect();

                if (postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                int statusCode = connection.getResponseCode();
                if (statusCode ==  200) {
                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    result =  HelperUtils.convertStreamToString(inputStream);
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
            try{
                if(result != null && !result.isEmpty()) {
                    JSONObject obj = new JSONObject(result);
                    ArrayList<String> todoItems = new ArrayList<>();
                    if(String.valueOf(obj.get("ToDoList")).charAt(0) == '['){
                        JSONArray jsonArray = obj.getJSONArray("ToDoList");
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject item = jsonArray.getJSONObject(i);
                            todoItems.add(item.getString("title"));
                        }
                    }
                    else{
                        String item = obj.getJSONObject("ToDoList").getString("title");
                        todoItems.add(item);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_todo, R.id.task_title, todoItems);
                    mTaskListView.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

