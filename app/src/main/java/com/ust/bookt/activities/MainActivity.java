package com.ust.bookt.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
//import com.ust.bookt.bean.Config;
//import com.pushbots.push.Pushbots;
import com.ust.bookt.bean.User;
import com.ust.bookt.fragments.MyBooksFragment;
import com.ust.bookt.fragments.PostFragment;
import com.ust.bookt.R;
import com.ust.bookt.fragments.ReservedBooksFragment;
import com.ust.bookt.fragments.TabFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.ust.bookt.bean.Config.FIREBASE_USER_RETRIEVE;

public class MainActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    boolean doubleBackToExitPressedOnce = false;
    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


         String display, contactNumber;
        final SharedPreferences preferences = getApplicationContext().getSharedPreferences("IDOFUSER",MODE_PRIVATE) ;
        final String uid = preferences.getString("id","");
        final String email = preferences.getString("email","");
        final ArrayList<String> uids = new ArrayList<String>();
        final ArrayList<String> nameAL = new ArrayList<String>();
        final ArrayList<String> contactNumberAL = new ArrayList<String>();


        super.onCreate(savedInstanceState);

        OneSignal.startInit(this).setNotificationOpenedHandler(new ExampleNotificationOpenedHandler()).init();

        JSONObject tags = new JSONObject();
        try {
            Log.d("e",email);
            tags.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OneSignal.sendTags(tags);


        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "User:" + userId);
                if (registrationId != null)
                    Log.d("debug", "registrationId:" + registrationId);
            }
        });




//
//        Pushbots.sharedInstance().init(this);

        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(getApplicationContext());
        final Firebase ref = new Firebase(FIREBASE_USER_RETRIEVE);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = new User();

                boolean match = false;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    user = postSnapshot.getValue(User.class);
                    if(user != null) {
                        uids.add(user.getUid());
                        nameAL.add(user.getDisplayName());
                        contactNumberAL.add(user.getContactNumber());
                        Log.d("sample", user.getUid()+user.getContactNumber()+user.getDisplayName());
                    }
                }

                    for(int i = 0; i < uids.size(); i++){

                        Log.d("run", uid + " " + uids.get(i));
                        if((uids.get(i) != null || uids.get(i).equals("")) && uids.get(i).equals(uid)){
                            match = true;
                            break;
                        }
                    }


                if (!match){
                    user.setDisplayName(preferences.getString("name",""));
                    user.setUid(uid);
                    user.setContactNumber(" ");

                    Firebase newRef = ref.child(uid);
                    newRef.setValue(user);
                }
                ref.removeEventListener(this);

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_post);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_bar) ;

        if (!fab.isShown()) {
            fab.show();
        }

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabFragment()).addToBackStack(null).commit();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (!fab.isShown()) {
                    fab.show();
                }

                if (menuItem.getItemId() == R.id.nav_item_home) {
                    toolbar.setTitle("Home");

                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new TabFragment()).addToBackStack(null).commit();
                }

                if (menuItem.getItemId() == R.id.nav_item_my_books) {
                    toolbar.setTitle("My Books");

                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new MyBooksFragment()).addToBackStack(null).commit();

                }

                if (menuItem.getItemId() == R.id.nav_item_reserved_books) {
                    toolbar.setTitle("Reserved Books");

                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new ReservedBooksFragment()).addToBackStack(null).commit();
                }


                if (menuItem.getItemId() == R.id.nav_item_sign_out) {
                    toolbar.setTitle("Signing Out");

//                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                            .requestEmail()
//                            .build();
//                    mGoogleApiClient = new GoogleApiClient.Builder(this)
//                            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                            .build();
//                    mGoogleApiClient.connect();
//                    super.onStart();

//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    intent.putExtra("status", "logout");
//                    startActivity(intent);

                }

                if (menuItem.getItemId() == R.id.nav_item_edit_profile) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                    alert.setTitle("Edit Profile");

                    LinearLayout container = new LinearLayout(MainActivity.this);
                    LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    container.setOrientation(LinearLayout.VERTICAL);
                    params.leftMargin = 40;
                    params.rightMargin = 40;
                    params.topMargin = 50;

                    final EditText name = new EditText(MainActivity.this);
                    name.setHint("Name");
                    name.setLayoutParams(params);
                    container.addView(name);

                    final EditText contact = new EditText(MainActivity.this);
                    contact.setHint("Contact Information");
                    contact.setLayoutParams(params);
                    contact.setInputType(InputType.TYPE_CLASS_PHONE);

                    container.addView(contact);
                    alert.setView(container);

                    if(!uids.isEmpty()){
                        for(int i = 0; i < uids.size(); i++){
                            if(uids.get(i).equals(uid)){
                                name.setText(nameAL.get(i));
                                contact.setText(contactNumberAL.get(i).trim());
                                break;
                            }
                        }
                    }

                    Log.d("name", nameAL.toString() + " " + contactNumberAL.toString());

                    alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            final String sName = name.getText().toString();
                            final String sContact = contact.getText().toString();

                            name.setText(sName);
                            contact.setText(sContact);

                            Firebase m_objFireBaseRef = new Firebase(FIREBASE_USER_RETRIEVE);
                            Firebase objRef = m_objFireBaseRef.child(uid);
                            objRef.child("contactNumber").setValue(sContact);
                            objRef.child("displayName").setValue(sName);

                            Toast.makeText(getApplicationContext(), "Update Successful!", Toast.LENGTH_SHORT).show();

                            name.setText(sName);
                            contact.setText(sContact);

                            Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(refresh);
                        }



                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {}
                    });

                    alert.show();



                }



                return false;
            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setTitle("Post");

                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView,new PostFragment()).addToBackStack(null).commit();

                fab.hide();
            }
        });

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
//                return;
//            }
//
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();
//
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce=false;
//                }
//            }, 2000);

            super.onBackPressed();
        }
        else {
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // This fires when a notification is opened by tapping on it or one is received while the app is running.
    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {


        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

        }
    }


}