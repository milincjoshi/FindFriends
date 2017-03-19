package com.sunshine.mohan.findfriends;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sunshine.mohan.findfriends.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AcceptFriendActivity extends AppCompatActivity {

    String email = null;
    String emailFromSharedPref;
    private SessionManager session;
    Button acceptFriend;
    Button rejectFriend;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            startActivity(new Intent(this, DetailsActivity.class));
            return true;
        }

        if (id == R.id.logout) {
            session.setLogin(false);
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_friend);

        session = new SessionManager(getApplicationContext());
        emailFromSharedPref = session.getPrefName().toString().trim();//current user's email
        Log.v("shaed", emailFromSharedPref);

        LongOperation lo = new LongOperation();
        lo.execute();

        acceptFriend = (Button) findViewById(R.id.accept_button);
        rejectFriend = (Button) findViewById(R.id.reject_button);
        acceptFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    String link = "http://nearbyfriendsapplogin.gear.host/include/accept_friend_true.php?user_email=" + emailFromSharedPref+"&friend_email="+email;

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
                            Log.v("accepted", "friend");

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                Toast.makeText(AcceptFriendActivity.this, "You have Accepted The Request", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AcceptFriendActivity.this, DetailsActivity.class);
                startActivity(i);
                finish();

            }
        });
        rejectFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String link = "http://nearbyfriendsapplogin.gear.host/include/reject_friend_delete.php?user_email=" + emailFromSharedPref+"&friend_email="+email;

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
                            Log.v("deleted", "friend");
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                Toast.makeText(AcceptFriendActivity.this, "You have Rejected This Request", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AcceptFriendActivity.this, DetailsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private class LongOperation extends AsyncTask<String, Void, String> {

        int byGetOrPost = 0;
        @Override
        protected String doInBackground(String... params) {

            if(byGetOrPost == 0){ //means by Get Method
                try{
                    String link = "http://nearbyfriendsapplogin.gear.host/include/accept_reject.php?user_email="+emailFromSharedPref;

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
                            Log.v("Answer", sb.toString());
                            JSONArray acceptObject = new JSONArray(sb.toString());
                            for(int i=0; i< acceptObject.length();i++){
                                JSONObject single_friend = acceptObject.getJSONObject(i);

                                email = single_friend.getString("user_email");

                            }


                            br.close();
                    }
                }

                catch(Exception e){
                    Log.v("String Builder", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView email_text = (TextView)findViewById(R.id.acceptEmail);


            if(email != null){
                email_text.setText(email);
            }
            else{
                Toast.makeText(AcceptFriendActivity.this, "No Request To Display", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AcceptFriendActivity.this, DetailsActivity.class);
                startActivity(i);
                finish();
            }

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
