package com.harry.example.covid19tracker;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class CountryModal implements Parcelable {
    private String countryName,cases,todayCases,deaths,todayDeaths,recovered,active,critical,image_url;
    //private Bitmap countryImage;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }



    public CountryModal(String countryName,String cases, String todayCases, String deaths, String todayDeaths, String recovered, String critical, String active,String image_url) {
        this.countryName=countryName;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.critical = critical;
        this.active = active;
        this.image_url = image_url;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(String todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCritical() {
        return critical;
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryName);
        dest.writeString(this.cases);
        dest.writeString(this.todayCases);
        dest.writeString(this.deaths);
        dest.writeString(this.todayDeaths);
        dest.writeString(this.recovered);
        dest.writeString(this.active);
        dest.writeString(this.critical);
        dest.writeString(this.image_url);
    }

    protected CountryModal(Parcel in) {
        this.countryName = in.readString();
        this.cases = in.readString();
        this.todayCases = in.readString();
        this.deaths = in.readString();
        this.todayDeaths = in.readString();
        this.recovered = in.readString();
        this.active = in.readString();
        this.critical = in.readString();
        this.image_url = in.readString();
    }

    public static final Creator<CountryModal> CREATOR = new Creator<CountryModal>() {
        @Override
        public CountryModal createFromParcel(Parcel source) {
            return new CountryModal(source);
        }

        @Override
        public CountryModal[] newArray(int size) {
            return new CountryModal[size];
        }
    };
}
