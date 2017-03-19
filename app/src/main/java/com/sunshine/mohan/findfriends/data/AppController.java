package com.sunshine.mohan.findfriends.data;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by FEBIELGIVA on 6/7/2016.
 */

//Responsble in get and put request
public class AppController extends Application{

    public static final String LOG_TAG = AppController.class.getSimpleName();
    private RequestQueue mRequest;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


    public static  synchronized  AppController getInstance(){
        return mInstance;
    }


    //get the request
    public RequestQueue getmRequestQueue(){
        if(mRequest == null){
            mRequest = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequest;
    }

    //to add the request
    public <T> void addToRequestQueue(Request<T> req,String tag){
        //if request is empty display the LOG_TAG or else the actual tag
        req.setTag(TextUtils.isEmpty(tag) ? LOG_TAG : tag);
        getmRequestQueue().add(req);
    }


    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(LOG_TAG);
        getmRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag){
        if(mRequest != null){
            mRequest.cancelAll(tag);
        }
    }

}




