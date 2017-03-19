package com.sunshine.mohan.findfriends.helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sunshine.mohan.findfriends.data.AppConfig;
import com.sunshine.mohan.findfriends.data.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FEBIELGIVA on 6/8/2016.
 */
public class FetchCurrentLocation{
    public static final String LOG_TAG = FetchCurrentLocation.class.getSimpleName();
    GPSTracker gps;



    public FetchCurrentLocation(){

    }

    public void updateLocationTable(final String email,Context context){
        //select query for email
        //if exsit update the value
        //or else insert
        double latitudeDouble =0.0,longitudeDouble = 0.0;

        gps = new GPSTracker(context);

        if(gps.canGetLocation()) {
            latitudeDouble = gps.getLatitude();
            longitudeDouble = gps.getLongitude();
        }

        final String latitude = Double.toString(latitudeDouble);
        final String longitude = Double.toString(longitudeDouble);

        String tag_req = "location_request";
        //method type,url,response
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                AppConfig.LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String uid = jsonObject.getString("uid");
                            if(!error){
                              Log.d(LOG_TAG,"location entered without error" + uid);
                            }
                            else{
                                String error_msg = jsonObject.getString("error_msg");
                                Log.e(LOG_TAG,"location entered response"+error_msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "location entered error" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "error in location" +error.getMessage());

                    }
                }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "location");
                p.put("email", email);
                p.put("lat", latitude);
                p.put("lon", longitude);
                return p;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest,tag_req);


    }

}
