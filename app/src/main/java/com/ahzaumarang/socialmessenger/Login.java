package com.ahzaumarang.socialmessenger;

import android.app.ActionBar;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import callback.CustomCallback;
import pojo.Contact;

import android.view.WindowManager;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by AZN on 2016-12-19.
 */

public class Login extends Fragment implements View.OnClickListener{

    private static final String TAG = "EmailPassword";

    Button loginButton;
    EditText usernameEdit;
    EditText passwordEdit;
    SharedPreferences sharedPreferences;
    Context context;
    CustomCallback callback;
    TextView signUp;

    // [START declare_auth]
    FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]


    private DatabaseReference mDatabase;
    private DatabaseReference mContactsReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);

        usernameEdit = (EditText) view.findViewById(R.id.username);
        loginButton = (Button) view.findViewById(R.id.login);
        loginButton.setOnClickListener(this);

        passwordEdit = (EditText) view.findViewById(R.id.password);

        signUp = (TextView) view.findViewById(R.id.newUser);
        signUp.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        context = getActivity();
        callback = (CustomCallback) context;
        sharedPreferences = context.getSharedPreferences("details", Context.MODE_PRIVATE);

        //getActivity().setTitle("Login");

        return view;
    }

    @Override
    public void onClick(View view) {
        String username = usernameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        if (loginButton.getId() == view.getId()) {
            if (username.length() != 0) {
                signIn(username, password);
            } else {
                Toast.makeText(context, "Enter username", Toast.LENGTH_SHORT).show();
            }
        }else if (view.getId() == R.id.newUser) {
            callback.loginActivity(2);
            Toast.makeText(context, "Sign Up", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        //if (!validateForm()) {
        //    return;
        //}

        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(context, "signInWithEmail:Failed",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            signinSuccess(task.getResult().getUser().getUid());
                        }

                        // [START_EXCLUDE]
                        //if (!task.isSuccessful()) {
                        //    mStatusTextView.setText(R.string.auth_failed);
                        //}
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signinSuccess(String userUid){
        mContactsReference = FirebaseDatabase.getInstance().getReference().child("users").child(userUid);
        ValueEventListener contactsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot result) {
                // Get Post object and use the values to update the UI
                //Post post = dataSnapshot.getValue(Post.class);
                // [START_EXCLUDE]
                //mAuthorView.setText(post.author);
                // [END_EXCLUDE

                try{
                    System.out.println(result.getValue());
                    Map<String,Object> value = (Map<String, Object>) result.getValue();

                    String userFullname = String.valueOf(value.get("username"));
                    Log.d(TAG, "Login userFullname>>>>>>>:" + userFullname);
                    sharedPreferences.edit().putString("username", userFullname).apply();

                    callback.loginActivity(0);
                } catch (Throwable e) {
                    Log.d(TAG, "Error failed:" + e.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        };

        mContactsReference.addListenerForSingleValueEvent(contactsListener);
    }

}
