package com.sunshine.mohan.findfriends;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sunshine.mohan.findfriends.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListFriends extends AppCompatActivity {

    ListView lv;
    Friends_Adapter friend_adapter;
    String emailFromSharedPref;
    String mine_lat, mine_lon;
    Friend this_is_me;//Current location of the user
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_friends);

        ArrayList<Friend> friends_List = new ArrayList<Friend>();//list to be displayed

        lv = (ListView) findViewById(R.id.friends_display_list);
        friend_adapter = new Friends_Adapter(this, friends_List);
        lv.setAdapter(friend_adapter);//binding listview to list

        session = new SessionManager(getApplicationContext());
        emailFromSharedPref = session.getPrefName().toString().trim();//current user's email

        Log.v("renfce ", emailFromSharedPref);///debug
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object o = lv.getItemAtPosition(position);
                Friend f = (Friend) o;
                Intent myIntent = new Intent(ListFriends.this, MapsActivityNew.class);
                myIntent.putExtra("email", f.getEmail()); //Optional parameters
                myIntent.putExtra("lat", Double.toString(f.getLat())); //Optional parameters
                myIntent.putExtra("lon", Double.toString(f.getLon())); //Optional parameters

                ListFriends.this.startActivity(myIntent);
            }
        });//Open Maps Activity
    }
    public void fetch_data(){
        Fetch_Data c = new Fetch_Data();
        c.execute();//Run background task for fetching data
    }
    public void connect_to_db(View view) {
        fetch_data();
    }
    public double calculate_distance(String my_lat, String my_lon, String friend_lat, String friend_lon){

        Location my_location = new Location("");
        my_location.setLatitude(Double.parseDouble(my_lat));
        my_location.setLongitude(Double.parseDouble(my_lon));

        Location friend_location = new Location("");
        friend_location.setLatitude(Double.parseDouble(friend_lat));
        friend_location.setLongitude(Double.parseDouble(friend_lon));

        float distanceInMeters = my_location.distanceTo(friend_location);

        String x = String.format("%.2f",distanceInMeters/1000);
        DecimalFormat df = new DecimalFormat();

        double distance  = Double.parseDouble(x);

        return distance;
    }

    class Fetch_Data extends AsyncTask<Void, Void, ArrayList<Friend>> {

        private int byGetOrPost = 0;

        @Override
        protected void onPostExecute(ArrayList<Friend> friends_list) {
            super.onPostExecute(friends_list);

            friend_adapter.clear();

            lv = (ListView) findViewById(R.id.friends_display_list);
            friend_adapter = new Friends_Adapter(ListFriends.this, friends_list);
            lv.setAdapter(friend_adapter);//refresh and display the list

        }
        @Override
        protected ArrayList<Friend> doInBackground(Void... params) {

            ArrayList<Friend> friends_list_return = new ArrayList<Friend>();

            if(byGetOrPost == 0){ //means by Get Method
                try{
                    String link = "http://nearbyfriendsapplogin.gear.host/include/get_friends.php?user_email="+emailFromSharedPref;

                    HttpURLConnection c = null;

                    URL u = new URL(link);
                    c = (HttpURLConnection) u.openConnection();
                    c.setRequestMethod("GET");
                    c.setRequestProperty("Content-length", "0");
                    c.setUseCaches(false);
                    c.setAllowUserInteraction(false);
                    c.connect();
                    int status = c.getResponseCode();

                    switch (status) {
                        case 200:
                        case 201:
                            BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;

                            while ((line = br.readLine()) != null) {
                                sb.append(line+"\n");
                            }
                            Log.v("Answer" , sb.toString());
                            JSONArray friendObject = new JSONArray(sb.toString());

                            JSONArray myLocationObject = getMyLocation(emailFromSharedPref);
                            Log.v("mylocation", String.valueOf(myLocationObject));

                            for (int i = 0; i < myLocationObject.length(); i++) {//Iterate through the current user's details
                                JSONObject jsonObject = myLocationObject.getJSONObject(i);
                                String friend_email = jsonObject.getString("email");
                                this_is_me = new Friend();
                                this_is_me.setEmail(friend_email);
 /*                              mine_lat = jsonObject.getString("lat");
                                mine_lon = jsonObject.getString("lon");
 */
                                this_is_me.setLat(Double.parseDouble(jsonObject.getString("lat")));
                                this_is_me.setLon(Double.parseDouble(jsonObject.getString("lon")));

                            }
                            for (int i = 0; i < friendObject.length(); i++) {

                                JSONObject jsonObject = friendObject.getJSONObject(i);
                                String friend_email = jsonObject.getString("email");
                                String lat = jsonObject.getString("lat");
                                String lon = jsonObject.getString("lon");
                                //String name = jsonObject.getString("name");
                                Friend f = new Friend();
                                f.setEmail(friend_email);
                                //f.setFirst_name(name);
                                f.setLat(Double.parseDouble(lat));
                                f.setLon(Double.parseDouble(lon));
                                Log.v("me lat", Double.toString(this_is_me.getLat()));
                                Log.v("me lon", Double.toString(this_is_me.getLon()));
                                Log.v("your lat", lat);
                                Log.v("your lat", lon);

                                Double distance = calculate_distance(Double.toString(this_is_me.getLat()),Double.toString(this_is_me.getLon()), lat, lon);
                                f.setDistance(distance);
                                friends_list_return.add(f);


                            }
                            Log.v("String Builder", sb.toString());

                            br.close();
                    }
                }

                catch(Exception e){
                    Log.v("String Builder", e.getMessage());
                }
            }

            return friends_list_return;
        }


    }

    private JSONArray getMyLocation(String emailFromSharedPref) {
        String link1 = "http://nearbyfriendsapplogin.gear.host/include/get_user_lat_lon.php?user_email="+emailFromSharedPref;

        HttpURLConnection c1 = null;

        try {
            URL u1 = new URL(link1);
            c1 = (HttpURLConnection) u1.openConnection();
            c1.setRequestMethod("GET");
            c1.setRequestProperty("Content-length", "0");
            c1.setUseCaches(false);
            c1.setAllowUserInteraction(false);
            c1.connect();
            int status1 = c1.getResponseCode();

            switch (status1) {
                case 200:
                case 201:
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(c1.getInputStream()));
                    StringBuilder sb1 = new StringBuilder();
                    String line1;

                    while ((line1 = br1.readLine()) != null) {
                        sb1.append(line1 + "\n");
                    }
                    Log.v("Answer", sb1.toString());
                    JSONArray myLocationObject = new JSONArray(sb1.toString());
                    return myLocationObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}