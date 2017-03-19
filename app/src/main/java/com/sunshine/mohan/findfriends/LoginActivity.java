package com.sunshine.mohan.findfriends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sunshine.mohan.findfriends.data.AppConfig;
import com.sunshine.mohan.findfriends.data.AppController;
import com.sunshine.mohan.findfriends.helper.FetchCurrentLocation;
import com.sunshine.mohan.findfriends.helper.SQLiteHandler;
import com.sunshine.mohan.findfriends.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private EditText emailTF, passwordTF;
    private Button loginBtn, gotoRegBtn;
    private ProgressDialog progressDialog;

    //our own class
    private SessionManager session;
    private SQLiteHandler db;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        emailTF = (EditText) findViewById(R.id.emailSF);
        passwordTF = (EditText) findViewById(R.id.passwordSF);
        loginBtn = (Button) findViewById(R.id.signin_btn);
        gotoRegBtn = (Button) findViewById(R.id.notreg_btn);

        progressDialog = new ProgressDialog(this);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        //if the session is loged in then i am moving the user to the detail activity
        if (session.isLoggedIn()) {
            Log.d(LOG_TAG,"pref email in on loggin    "+ session.getPrefName());


            /************************************************************************************************************************************************************/
            FetchCurrentLocation updateLocation = new FetchCurrentLocation();
            updateLocation.updateLocationTable(session.getPrefName().toString().trim(),LoginActivity.this);


            Intent i = new Intent(this, DetailsActivity.class);
            startActivity(i);
            finish();
        }

        //if register is clicked
        gotoRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTF.getText().toString();
                String password = passwordTF.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    //login validation
                    checkLogin(email, password);

                    /************************************************************************************************************************************************************/
                    FetchCurrentLocation updateLocation = new FetchCurrentLocation();
                    updateLocation.updateLocationTable(session.getPrefName().toString().trim(), LoginActivity.this);

                } else {
                    Toast.makeText(LoginActivity.this, "Please Enter Details",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);
    }


    public void checkLogin(String emailFromUser, final String passwordFromUser) {
        final String email = emailFromUser.toString().trim();
        final String password = passwordFromUser.toString().trim();

        String tag_req = "login_request";
        progressDialog.setMessage("Logging In....");
        showDialog();

        //method type,url,response
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                AppConfig.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            if(!error){
                                String uid = jsonObject.getString("uid");

                                JSONObject userJsonObject = jsonObject.getJSONObject("user");
                                String name = userJsonObject.getString("name");
                                String email = userJsonObject.getString("email");
                                String created_at = userJsonObject.getString("created_at");
                                //db.addUser(name,email,uid,created_at);

                                session.setLogin(true);
                                session.setPrefName(email);
                                Log.d(LOG_TAG,"pref email    "+ session.getPrefName());

                                Intent i = new Intent(LoginActivity.this, DetailsActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                String error_msg = jsonObject.getString("error_msg");
                                Toast.makeText(LoginActivity.this,error_msg,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "error response");

                    }
                }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "login");
                p.put("email", email);
                p.put("password", password);
                return p;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_req);

    }

    public void showDialog(){
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    public void hideDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }






//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.sunshine.mohan.findfriends/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.sunshine.mohan.findfriends/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.login_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch(id){
//            case R.id.action_logout:
//                return true;
//            case R.id.action_help:
//                return true;
//            case R.id.sction_refresh:
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
