package com.ahzaumarang.socialmessenger;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ChatAdapter;
import adapter.ContactAdapter;
import keys.PubnubKeys;
import pojo.Message;
import callback.CustomCallback;

import android.util.Log;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pojo.User;
import pojo.Contact;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static java.security.AccessController.getContext;

/**
 * Created by AZN on 2016-12-20.
 */

public class Contacts extends AppCompatActivity {
    String TAG = "ContactsFragment:";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ContactAdapter contactAdapter;

    ArrayList<ArrayList<String>> myDataset;

    // [START declare_auth]
    FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private DatabaseReference mDatabase;
    private DatabaseReference mContactsReference;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.contactList);

        mAuth = FirebaseAuth.getInstance();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Showing default app icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize Database
        String userUid = mAuth.getCurrentUser().getUid();
        Log.d(TAG, userUid);
        mContactsReference = FirebaseDatabase.getInstance().getReference().child("users").child(userUid).child("contacts");

        myDataset = new ArrayList<ArrayList<String>>();
        contactAdapter = new ContactAdapter(myDataset);
        mRecyclerView.setAdapter(contactAdapter);

        setTitle(R.string.app_name);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener contactsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot result) {
                // Get Post object and use the values to update the UI
                //Post post = dataSnapshot.getValue(Post.class);
                // [START_EXCLUDE]
                //mAuthorView.setText(post.author);
                // [END_EXCLUDE
                System.out.println(result.getValue());
                /**
                Log.d(TAG, String.valueOf(result.getChildren()));
                for(DataSnapshot dsp : result.getChildren()){
                    myDataset.add(String.valueOf(dsp.getKey())); //add result into array list
                    Log.d(TAG, String.valueOf(dsp.getKey()));
                    Log.e(TAG, myDataset.toString());
                    contactAdapter.add(myDataset.size() - 1, String.valueOf(dsp.getKey()));
                }
                 */
                try{
                    for(DataSnapshot dsp : result.getChildren()){
                        ArrayList<String> userData = new ArrayList<String>();
                        userData.add(0,String.valueOf(dsp.getKey()));
                        Contact c = dsp.getValue(Contact.class);
                        userData.add(1,String.valueOf(c.email));
                        userData.add(2,String.valueOf(c.channel));
                        contactAdapter.add(myDataset.size() - 1, userData);
                    }
                } catch (Throwable e) {
                    Log.d(TAG, "Error failed:" + e.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(Contacts.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };

        mContactsReference.addListenerForSingleValueEvent(contactsListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        //mPostListener = postListener;

        // Listen for comments
        //mAdapter = new CommentAdapter(this, mCommentsReference);
        //mCommentsRecycler.setAdapter(mAdapter);

        // specify an adapter (see also next example)
        //mAdapter = new ContactAdapter(myDataset);

    }

    public void displayChat(String friend){
        Log.w(TAG, "displayChat:" + friend);
        FragmentActivity activity = (FragmentActivity) this;
        if (!isFinishing() && !isDestroyed()) {
            fragmentManager = activity.getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, new ChatFragment(), "Chat");
            fragmentTransaction.commit();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


}
