package com.harry.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

        private ScrollView scrollView;
        private SimpleArcLoader simpleArcLoader;
        private PieChart pieChart;
        private TextView cases;
        private TextView active;
        private TextView recovered;
        private TextView critical;
        private TextView today_cases;
        private TextView today_deaths;
        private TextView total_deaths;
        private TextView affected_countries;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        }

    @Override
    protected void onStart() {
        super.onStart();
        initViews();
        fetchData();
        Log.i("TAG", "onStart: ");
    }

    private void fetchData() {
            FetchData fetchData=new FetchData(getApplicationContext());
            String content_url="https://corona.lmao.ninja/v2/all";
                fetchData.execute(content_url);
        }

        public void trackCountries(View view) {
            Intent intent=new Intent(getApplicationContext(),AffectedCountries.class);
            startActivity(intent);
        }
        private void initViews(){
            scrollView=findViewById(R.id.scroll_stats);
            simpleArcLoader=findViewById(R.id.loader);
            pieChart=findViewById(R.id.piechart);
            cases=findViewById(R.id.textview_cases);
            active=findViewById(R.id.textview_active);
            recovered=findViewById(R.id.textview_recoverd);
            critical=findViewById(R.id.textview_critical);
            today_cases=findViewById(R.id.textview_today_cases);
            today_deaths=findViewById(R.id.textview_today_deaths);
            total_deaths=findViewById(R.id.textview_total_deaths);
            affected_countries=findViewById(R.id.textview_affected_countries);
        }
        private class FetchData extends AsyncTask<String,Void,JSONObject>{

            private Context context;
            private FetchData(Context context){
                this.context=context;
            }
            @Override
            protected void onPreExecute() {
               simpleArcLoader.start();
            }

            @Override
            protected JSONObject doInBackground(String... content_urls) {
                Log.i("TAG", "doInBackground: ");
                RequestQueue requestQueue= Volley.newRequestQueue(context);
                final JSONObject[] jsonObject = new JSONObject[1];
                JsonObjectRequest  jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, content_urls[0], null,new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        jsonObject[0] =response;
                        Log.i("TAG", "onPostExecute: "+String.valueOf(jsonObject == null));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonObjectRequest);
                while (jsonObject[0] == null){}
                return jsonObject[0];
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                try{
                    cases.setText(jsonObject.getString("cases"));
                    active.setText(jsonObject.getString("active"));
                    recovered.setText(jsonObject.getString("recovered"));
                    critical.setText(jsonObject.getString("critical"));
                    today_cases.setText(jsonObject.getString("todayCases"));
                    today_deaths.setText(jsonObject.getString("todayDeaths"));
                    total_deaths.setText(jsonObject.getString("deaths"));
                    affected_countries.setText(jsonObject.getString("affectedCountries"));
                    pieChart.addPieSlice(new PieModel("Total Cases", Integer.parseInt(jsonObject.getString("cases")), Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(jsonObject.getString("recovered")), Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(jsonObject.getString("deaths")), Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(jsonObject.getString("active")) , Color.parseColor("#29B6F6")));
                    pieChart.startAnimation();
                    simpleArcLoader.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }catch (JSONException jsonException){
                    jsonException.printStackTrace();
                }
            }
        }
}
