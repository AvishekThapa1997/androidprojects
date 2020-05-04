package com.harry.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AffectedCountries extends AppCompatActivity implements MyAdapter.DataFromRecyclerView {

    private SimpleArcLoader simpleArcLoader;
    private RecyclerView recyclerView;
    private EditText countrySearch;
    private JSONArray jsonArray;
    private MyAdapter myAdapter;
    private Context context;
    private HashMap<String,CountryModal> countryModalHashMap;
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            jsonArray=MyService.getJsonArray();
            String countryName=countrySearch.getText().toString();
            setCountryResult(jsonArray,countryName);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_countries);
        getSupportActionBar().setTitle("Affected Countries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;
        countryModalHashMap=new HashMap<>();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver,new IntentFilter("jsonArray_from_service"));
        initView();
        fetchData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return  true;
    }

    private void setCountryResult(final JSONArray jsonArray, final String countryName){
        Thread thread=new Thread(new Runnable() {
            List<CountryModal> countryModalList=new ArrayList<>();
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    try {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String country=jsonObject.getString("country");
                        String cases=jsonObject.getString("cases");
                        String todayCases=jsonObject.getString("todayCases");
                        String deaths=jsonObject.getString("deaths");
                        String todayDeaths=jsonObject.getString("todayDeaths");
                        String recovered=jsonObject.getString("recovered");
                        String active=jsonObject.getString("active");
                        String critical=jsonObject.getString("critical");
                        String image_url=jsonObject.getJSONObject("countryInfo").getString("flag");
                        CountryModal countryModal=new CountryModal(country,cases,todayCases,deaths,todayDeaths,recovered,critical,active,image_url);
                        countryModalList.add(countryModal);
                        countryModalHashMap.put(country,countryModal);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("TAG", "run: "+"Complete");
                setData(countryModalList);

            }

            private void setDataForRecyclerView(String country,String cases, String todayCases, String deaths, String todayDeaths, String recovered, String active, String critical, Bitmap bitmap) {

            }
//            private Bitmap getCountryImageFromUrl(String image_url) {
//
//            }

        });
        thread.start();
    }


    private void setData(final List<CountryModal> countryModalList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myAdapter=new MyAdapter(context,countryModalList);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(myAdapter);
                hideArcLoader();
                countrySearch.setEnabled(true);
            }
        });
    }
    private void hideArcLoader() {
        simpleArcLoader.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void initView(){
        simpleArcLoader=findViewById(R.id.loader);
        recyclerView=findViewById(R.id.recycler_view);
        countrySearch=findViewById(R.id.edtSearch);
        countrySearch.setEnabled(false);
        countrySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    myAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void fetchData(){
        Log.i("TAG", "fetchData: ");
        String url="https://corona.lmao.ninja/v2/countries";
        Intent intent=new Intent(getApplicationContext(),MyService.class);
        intent.putExtra("url",url);
        startService(intent);
//
//        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
//        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET,)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void dataFromRecycler(String countryName) {
       CountryModal countryModal=countryModalHashMap.get(countryName);
       Intent intent=new Intent(context,CountryStatus.class);
       intent.putExtra("country",countryModal);
       startActivity(intent);
    }
}
