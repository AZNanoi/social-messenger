package com.ahzaumarang.socialmessenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import callback.CustomCallback;
import android.util.Log;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pojo.User;

/**
 * Created by AZN on 2016-12-20.
 */

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "EmailPassword";

    Button loginButton;
    EditText usernameEdit;
    EditText emailEdit;
    EditText passwordEdit;
    EditText phoneEdit;
    SharedPreferences sharedPreferences;
    Context context;
    CustomCallback callback;


    // [START declare_auth]
    FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]


    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //View view = inflater.inflate(R.layout.sign_up, container, false);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        usernameEdit = (EditText) findViewById(R.id.username);
        emailEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);
        phoneEdit = (EditText) findViewById(R.id.phone);
        loginButton = (Button) findViewById(R.id.signUp);
        loginButton.setOnClickListener(this);

        context = this;

        sharedPreferences = context.getSharedPreferences("details", Context.MODE_PRIVATE);

        //getActivity().setTitle("Login");

        mAuth = FirebaseAuth.getInstance();


    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        //if (!validateForm()) {
        //    return;
        //}

        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Failed=" + task.getException().getMessage());

                            Toast.makeText(context, "Failed",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            createNewUser(task.getResult().getUser());

                        }
                        getSupportActionBar().show();
                        Intent k = new Intent(SignUp.this, Contacts.class);
                        startActivity(k);

                        //callback.loginActivity(0);
                        //sharedPreferences.edit().putString("username", username).apply();

                        //hideProgressDialog();

                    }
                });
        // [END create_user_with_email]
    }

    public void createNewUser(FirebaseUser firebaseUser){
        String username = usernameEdit.getText().toString().trim();
        String email = firebaseUser.getEmail();
        String userId = firebaseUser.getUid();
        String phone = phoneEdit.getText().toString().trim();

        User user = new User(username, email, phone);

        mDatabase.child("users").child(userId).setValue(user);

    }

    @Override
    public void onClick(View view) {
        String username = usernameEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String phone = phoneEdit.getText().toString().trim();
        if (loginButton.getId() == view.getId()) {
            if (username.length() != 0) {
                createAccount(email, password);
                //callback.loginActivity(0);
                //sharedPreferences.edit().putString("username", username).apply();
            } else {
                Toast.makeText(context, "Enter username", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
