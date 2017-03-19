package com.sunshine.mohan.findfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sunshine.mohan.findfriends.data.AppConfig;
import com.sunshine.mohan.findfriends.data.AppController;
import com.sunshine.mohan.findfriends.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AcceptRequestActivity extends AppCompatActivity {
    private Button acceptButton,denyButton;
    private TextView display;

    private SessionManager session;
    public static final String LOG_TAG = AcceptRequestActivity.class.getSimpleName();



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
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);
        session = new SessionManager(getApplicationContext());

        display = (TextView) findViewById(R.id.friendEmail);
        acceptButton = (Button) findViewById(R.id.acceptRequest);
        denyButton = (Button) findViewById(R.id.rejectRequest);
        fetchRequest(session.getPrefName().toString().trim());


        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAccept();
                //perform accept thing
            }
        });

        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //performDeny();
                //perform deny thing
            }
        });



    }


    private void performAccept(){
        final String myFriendEmail = display.getText().toString();
        final String myEmail = session.getPrefName().toString().trim();

        String tag_req = "acceptrequest_request";


        //method type,url,response
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                AppConfig.FRIEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");

                            if(!error){
                                String uid = jsonObject.getString("uid");
//                                final String freindEmail = uid;
                                display.setText(uid.toString());
                                Log.d(LOG_TAG, "worked" + uid);
                            }
                            else{
                                String error_msg = jsonObject.getString("error_msg");
                                Log.e(LOG_TAG,"does not" + error_msg);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Friend accept request exception" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "error response in Friend accept request" +error.getMessage());

                    }
                }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "acceptrequest");
                p.put("myemail", myEmail);
                p.put("femail", myFriendEmail);
                return p;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_req);

    }

    private void fetchRequest(final String email) {

        String tag_req = "accept_request";


        //method type,url,response
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                AppConfig.FRIEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");

                            if(!error){
                                String uid = jsonObject.getString("uid");
//                                final String freindEmail = uid;
                                display.setText(uid.toString());
                                Log.d(LOG_TAG, "worked" + uid);
                            }
                            else{
                                String error_msg = jsonObject.getString("error_msg");
                                Log.e(LOG_TAG,"does not" + error_msg);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Friend accept request exception" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "error response in Friend accept request" +error.getMessage());

                    }
                }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "accept");
                p.put("email", email);
                return p;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_req);
    }



}
