
package com.harry.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leo.simplearcloader.SimpleArcLoader;

public class CountryStatus extends AppCompatActivity {

    private SimpleArcLoader simpleArcLoader;
    private TextView countryName;
    private TextView cases;
    private TextView todayCases;
    private TextView deaths;
    private TextView todayDeaths;
    private TextView recovered;
    private TextView active;
    private TextView critical;
    private CardView cardView;
    private CountryModal countryModal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affected_countries_layout);
        getSupportActionBar().setTitle("Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        countryModal=intent.getParcelableExtra("country");
        initiViews();
        Log.i("TAG", "onCreate: "+countryModal);
        setData(countryModal);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return true;
    }

    private void setData(CountryModal countryModal) {
        countryName.setText(countryModal.getCountryName());
        cases.setText(countryModal.getCases());
        todayCases.setText(countryModal.getTodayCases());
        deaths.setText(countryModal.getDeaths());
        todayDeaths.setText(countryModal.getTodayDeaths());
        recovered.setText(countryModal.getRecovered());
        active.setText(countryModal.getActive());
        critical.setText(countryModal.getCritical());
        simpleArcLoader.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
    }

    private void initiViews(){
        simpleArcLoader=findViewById(R.id.loader);
        countryName=findViewById(R.id.countryName);
        cases=findViewById(R.id.textview_cases);
        todayCases=findViewById(R.id.textview_today_cases);
        deaths=findViewById(R.id.textview_deaths);
        todayDeaths=findViewById(R.id.textview_today_deaths);
        recovered=findViewById(R.id.textview_recoverd);
        active=findViewById(R.id.textview_active);
        critical=findViewById(R.id.textview_critical);
        cardView=findViewById(R.id.card_view);
    }
}
