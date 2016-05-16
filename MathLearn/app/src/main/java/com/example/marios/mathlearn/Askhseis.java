package com.example.marios.mathlearn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Askhseis extends MainBase implements Strategy{

    private static final String URL = String.format("http://mlearning-projectmr.rhcloud.com/assignments.php");
    String ids;
    Assignment[] assignments;
    private ListView listView;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strategy=this;
        listView=(ListView) findViewById(R.id.listView);
        dbHelper = new DataBaseHelper(this);
        populateList();
    }


    @Override
    public void syncedDB(String s) {
        try{
            JSONObject topLevel = new JSONObject(s);
            JSONArray  assignjson = (JSONArray) topLevel.get("assignments");
            String nonexistentassignments = (String) topLevel.get("nonexistentassignments");
            if (assignjson.length()!=0) {
                for (int i=0; i < assignjson.length(); i++)
                {
                    try {
                        JSONObject oneObject = assignjson.getJSONObject(i);
                        // Pulling items from the array
                        int id = oneObject.getInt("ID");
                        String title = oneObject.getString("Title");
                        String link = oneObject.getString("Link");
                        int sequence = oneObject.getInt("Sequence");
                        dbHelper.insertInAssignments(new Assignment(title, link, false, id, sequence));
                    } catch (JSONException e) {
                        // Oops
                    }
                }
            }
            if (!nonexistentassignments.equals("")){
                dbHelper.deleteFromAssignments(nonexistentassignments);
            }
            if (assignjson.length()==0 && nonexistentassignments.equals("") ){
                Toast.makeText(Askhseis.this, "Τίποτα νεότερο.", Toast.LENGTH_LONG).show();
            }else{
                populateList();
                Toast.makeText(Askhseis.this, "Οι ασκήσεις ανανεώθηκαν!", Toast.LENGTH_LONG).show();
            }
        }catch(JSONException e){
            //No connectivity?
        }
    }

    @Override
    public void populateList() {
        assignments=dbHelper.getAssignments();
        AssignmentsAdapter adapter = new AssignmentsAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dbHelper.updateSelectedinAssignments(assignments[position].assignID);
                String url = assignments[position].assignLink;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        StringBuilder sb= new StringBuilder();
        for (int i=0; i < assignments.length; i++){
            sb.append( "'"+assignments[i].assignID+"'," );
        }
        ids=sb.toString();
        ids = ids.substring(0, ids.length() - 1);
    }

    @Override
    public String provideIds() {
        return ids;
    }

    @Override
    public String provideURL() {
        return URL;
    }


    private class AssignmentsAdapter extends BaseAdapter{

        private LayoutInflater inflater=null;

        @Override
        public int getCount() {
            return assignments.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = ( LayoutInflater )Askhseis.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listview, null);
            TextView tv=(TextView) rowView.findViewById(R.id.textView1);
            tv.setText(assignments[position].assignTitle);
            if (assignments[position].assignSelected){
                rowView.setBackgroundColor(1426063360);
            }
            return rowView;
        }
    }

}
