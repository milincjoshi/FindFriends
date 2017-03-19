package com.sunshine.mohan.findfriends;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.sunshine.mohan.findfriends.helper.FetchCurrentLocation;
import com.sunshine.mohan.findfriends.helper.SQLiteHandler;
import com.sunshine.mohan.findfriends.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/* mohan */
public class RegisterActivity extends Activity {

    private EditText nameTF, emailTF, passwordTF;
    private Button registerBtn, gotoLogBtn;
    private ProgressDialog progressDialog;
    //our own class
    private SessionManager session;
    private SQLiteHandler db;

    public static final String LOG_TAG = RegisterActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameTF = (EditText) findViewById(R.id.fullnameRF);
        emailTF = (EditText) findViewById(R.id.emailRF);
        passwordTF = (EditText) findViewById(R.id.passwordRF);
        registerBtn = (Button) findViewById(R.id.register_btn);
        gotoLogBtn = (Button) findViewById(R.id.signinto_btn);

        progressDialog = new ProgressDialog(this);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        //if the session is loged in then i am moving the user to the detail activity
        if (session.isLoggedIn()) {
            Intent i = new Intent(this, DetailsActivity.class);
            startActivity(i);
            finish();
        }


        //if register is clicked
        gotoLogBtn.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {
                                              Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                              startActivity(i);
                                              finish();
                                          }
                                      }

        );


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTF.getText().toString();
                String email = emailTF.getText().toString();
                String password = passwordTF.getText().toString();
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    //login validation
                    registerCheck(name, email, password);

                    //************************************************************************************************************************************************************/
                    FetchCurrentLocation updateLocation = new FetchCurrentLocation();
                    updateLocation.updateLocationTable(email.trim(),RegisterActivity.this);

                } else {
                    Toast.makeText(RegisterActivity.this, "Please Enter Details",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void registerCheck(final String name,final String email, final String password){
        String tag_req = "register_request";
        progressDialog.setMessage("Registering In....");
        showDialog();

        //method type,url,response
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                AppConfig.REGISTER_URL,
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
                                db.addUser(name,email,uid,created_at);

                                session.setLogin(true);
                                session.setPrefName(email);


                                Intent i = new Intent(RegisterActivity.this, DetailsActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                String error_msg = jsonObject.getString("error_msg");
                                Toast.makeText(RegisterActivity.this,error_msg,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, error.getMessage());

                    }
                }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "register");
                p.put("name", name);
                p.put("email", email);
                p.put("password", password);
                return p;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest,tag_req);
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



























