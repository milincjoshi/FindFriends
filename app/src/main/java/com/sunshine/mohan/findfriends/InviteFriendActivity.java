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
import com.sunshine.mohan.findfriends.data.AppConfig;
import com.sunshine.mohan.findfriends.data.AppController;
import com.sunshine.mohan.findfriends.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InviteFriendActivity extends AppCompatActivity {
    private Button inviteFriendButton;
    private EditText emailFriend;

    public static final String LOG_TAG = InviteFriendActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private SessionManager session;

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
        setContentView(R.layout.activity_invite_friend);
        emailFriend = (EditText) findViewById(R.id.friendEmail);
        inviteFriendButton = (Button) findViewById(R.id.inviteFriendButton);
        progressDialog = new ProgressDialog(this);
        session = new SessionManager(getApplicationContext());
        inviteFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, session.getPrefName().toString());
                addFriendEmail(emailFriend.getText().toString().trim(), session.getPrefName().toString().trim());
                Log.d(LOG_TAG, emailFriend.getText().toString());

                Intent i = new Intent(InviteFriendActivity.this, DetailsActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    private void addFriendEmail(final String emailFriend,final String myEmail) {
        String tag_req = "invite_request";
        progressDialog.setMessage("Inviting Friend In....");
        showDialog();

        //method type,url,response
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                AppConfig.FRIEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");

                            if(!error){
                                String uid = jsonObject.getString("uid");
                                Log.d(LOG_TAG, "friend email id present" + uid);
                            }
                            else{
                                String error_msg = jsonObject.getString("error_msg");
                                Log.e(LOG_TAG, "friend invite some error" + error_msg);
                                Toast.makeText(InviteFriendActivity.this, error_msg, Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Friend invite exception" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "error in friend invite" +error.getMessage());

                    }
                }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "invite");
                p.put("emailfriend", emailFriend);
                p.put("emailmy", myEmail);
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
}
