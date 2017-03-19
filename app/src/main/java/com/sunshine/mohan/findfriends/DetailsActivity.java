package com.sunshine.mohan.findfriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunshine.mohan.findfriends.helper.DrawerListAdapter;
import com.sunshine.mohan.findfriends.helper.SQLiteHandler;
import com.sunshine.mohan.findfriends.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailsActivity extends Activity {

    //UI
    public class NavItem {
        public String mTitle;
        public String mSubtitle;
        public int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();


    public static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    private TextView emailTV, nameTV;
    private Button logoutBtn;
    private Button inviteButton,acceptButton,lisFriendsButton;
    private SessionManager session;
    private SQLiteHandler db;
    private TextView textView;

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

    //   Here, I took some help from the following // http://codetheory.in/android-navigation-drawer/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //UI
        mNavItems.add(new NavItem("Home", "Check out Freinds", R.drawable.app_icon1));
        mNavItems.add(new NavItem("Logout", "Say Bye to This App", R.drawable.app_icon2));
        mNavItems.add(new NavItem("About", "Get to know about us", R.drawable.app_icon3));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);


        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

//                textView = (TextView) findViewById(R.id.userName);
//                textView.setText(session.getPrefName());

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        checkTheLoginAndDisplayLogoutButton();
//        FetchCurrentLocation fetchCurrentLocation = new FetchCurrentLocation();
//        FetchCurrentLocation.excute();

    }
//
//        @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.detail_main,menu);
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

    public void checkTheLoginAndDisplayLogoutButton(){
//        nameTV = (TextView) findViewById(R.id.nameWF);
//        emailTV = (TextView) findViewById(R.id.emailWF);
        logoutBtn = (Button) findViewById(R.id.logout_btn);
        inviteButton = (Button) findViewById(R.id.invite_btn);
        acceptButton = (Button) findViewById(R.id.accept_btn);
        lisFriendsButton = (Button) findViewById(R.id.list_friends_btn);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        //if the session is loged in then i am moving the user to the detail activity
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String,String> userDeatils = db.getUserDetails();

        String name = userDeatils.get("name");
        String email = userDeatils.get("email");

//        emailTV.setText(email);
//        nameTV.setText(name);

        //if register is clicked
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, InviteFriendActivity.class);
                startActivity(i);
                finish();

            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, AcceptFriendActivity.class);
                startActivity(i);
                finish();

            }
        });

        lisFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity.this, ListFriends.class);
                startActivity(i);
                finish();

            }
        });
    }


    private void logoutUser(){
        session.setLogin(false);
        db.deleteUser();
        Intent i = new Intent(DetailsActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }



    //UI
    private void selectItemFromDrawer(int position) {
//        Fragment fragment = new PreferencesFragment();
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.mainContent, fragment)
//                .commit();
//
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mNavItems.get(position).mTitle);
//
//        // Close the drawer
//        mDrawerLayout.closeDrawer(mDrawerPane);

        switch (position){
            case 0: {
                Intent i = new Intent(DetailsActivity.this, DetailsActivity.class);
                startActivity(i);
                finish();
                break;
            }
            case 1:
            {
                logoutUser();
                break;
            }
//            case 2:
//            {
//                Intent i = new Intent(DetailsActivity.this, AboutActivity.class);
//                startActivity(i);
//                finish();
//                break;
//            }


        }

    }
}
