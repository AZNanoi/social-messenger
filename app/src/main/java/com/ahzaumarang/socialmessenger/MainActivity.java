package com.ahzaumarang.socialmessenger;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;

import callback.CustomCallback;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements CustomCallback {

    private static final String TAG = "MainActivity:";

    // Firebase instance variables
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    FirebaseAuth.AuthStateListener mAuthListener;

    SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("details", MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        changeLogin();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void changeLogin() {
        String s = getIntent().getStringExtra("SELECTED_CONTACT");
        if (sharedPreferences.getString("username", null) == null) {
            getSupportActionBar().hide();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new Login(), "Login");
            fragmentTransaction.commit();
        } else if(s!= null) {
            getSupportActionBar().show();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new ChatFragment(), "Chat");
            fragmentTransaction.commit();
        } else {
            getSupportActionBar().show();
            Intent k = new Intent(MainActivity.this, Contacts.class);
            startActivity(k);
        }
    }

    public void loginActivity(int LOGIN_STATE) {
        if (LOGIN_STATE == 0) {
            getSupportActionBar().show();
            Intent k = new Intent(MainActivity.this, Contacts.class);
            startActivity(k);
            //fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.replace(R.id.container, new ChatFragment(), "Chat");
            //fragmentTransaction.commit();
        }else if(LOGIN_STATE == 2) {
            getSupportActionBar().show();
            Intent k = new Intent(MainActivity.this, SignUp.class);
            startActivity(k);
        } else {
            getSupportActionBar().hide();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new Login(), "Login");
            fragmentTransaction.commit();
        }
    }
}
