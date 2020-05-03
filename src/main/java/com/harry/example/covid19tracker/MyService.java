package com.harry.example.covid19tracker;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class MyService extends IntentService{
    private static JSONArray jsonArray;
    public MyService(){
        super("MyService");
    }

    public static JSONArray getJsonArray() {
        return jsonArray;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("TAG", "onStartCommand: "+Thread.currentThread().getName());
        String url=intent.getStringExtra("url");
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                jsonArray=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
        while (jsonArray == null){}
        Log.i("TAG", "onResponse: "+Thread.currentThread().getName());
        Intent intent1=new Intent("jsonArray_from_service");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
    }
}
