package com.ahzaumarang.socialmessenger;

import android.content.Intent;
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

import com.google.gson.Gson;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.ChatAdapter;
import keys.PubnubKeys;
import pojo.Contact;
import pojo.Message;
import callback.CustomCallback;

/**
 * Created by AZN on 2016-12-19.
 */

public class ChatFragment extends Fragment implements View.OnClickListener{
    String TAG = "ChatFragment";
    SharedPreferences sharedPreferences;
    CustomCallback callback;
    Context context;
    Pubnub pubnub;
    EditText chatMessage;
    Button send;
    RecyclerView chatList;
    ArrayList<String> chatMessageList;
    ChatAdapter chatAdapter;
    Gson gson;
    JSONObject messageObject;
    String username;
    String friend;
    String selectedContact;
    String selectedContactEmail;
    String selectedContactChannel;

    /**
    public static ChatFragment newInstance(String someInt) {
        ChatFragment myFragment = new ChatFragment();

        Bundle args = new Bundle();
        args.putString("someInt", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }


     * During creation, if arguments have been supplied to the fragment
     * then parse those out.
     *
     /**
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("savedInstanceState::", savedInstanceState.toString());

        Bundle args = getArguments();
        if (args != null) {
            //mLabel = args.getCharSequence("label", mLabel);
        }
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("successCallback::::", "Test ");
        Bundle args = getArguments();
        if (args!= null){
            String myValue = args.getString("friend");
            Log.d("successCallback::::", "message " + myValue);
        }

        selectedContact = getActivity().getIntent().getStringExtra("SELECTED_CONTACT");

        if (selectedContact!= null){
            Log.d(TAG, "ChatFragment::::" + selectedContact);
        }


        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        setHasOptionsMenu(true);

        context = getActivity();
        callback = (CustomCallback) context;
        sharedPreferences = context.getSharedPreferences("details", Context.MODE_PRIVATE);
        gson = new Gson();

/**
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.drawable.blank_photo);

        imageView.setBackground(context.getResources().getDrawable(R.drawable.rounded_image));
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.LEFT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 0;
        layoutParams.width = 120;
        layoutParams.height = 120;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
*/
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.drawable.blank_photo);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Showing default app icon
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Add custom icon in the ActionBar
        //View viewIcon = getActivity().getLayoutInflater().inflate(R.layout.actionbar_icons, null);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(viewIcon);

        username = sharedPreferences.getString("username", null);
        Log.d("username>>>>>: ", username);

        getActivity().setTitle(selectedContact);

        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessageList, username);

        chatList = (RecyclerView) view.findViewById(R.id.chatlist);
        chatList.setLayoutManager(new LinearLayoutManager(context));
        chatList.setAdapter(chatAdapter);

        chatMessage = (EditText) view.findViewById(R.id.message);
        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(this);

        pubnub = new Pubnub(PubnubKeys.PUBLISH_KEY, PubnubKeys.SUBSCRIBE_KEY);

        selectedContactEmail = getActivity().getIntent().getStringExtra("SELECTED_CONTACT_email");
        selectedContactChannel = getActivity().getIntent().getStringExtra("SELECTED_CONTACT_channel");

        Toast.makeText(context, username, Toast.LENGTH_SHORT).show();

        try {
            pubnub.subscribe(selectedContactChannel, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    super.successCallback(channel, message);

                    chatMessageList.add(message.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatAdapter.notifyItemInserted(chatMessageList.size() - 1);
                            chatList.scrollToPosition(chatMessageList.size() - 1);
                        }
                    });

                }

                @Override
                public void successCallback(String channel, Object message, String timetoken) {
                    super.successCallback(channel, message, timetoken);
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    super.errorCallback(channel, error);
                    Log.d("errorCallback", "error " + error);
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    super.connectCallback(channel, message);
                    Log.d("connectCallback", "message " + message);
                }

                @Override
                public void reconnectCallback(String channel, Object message) {
                    super.reconnectCallback(channel, message);
                    Log.d("reconnectCallback", "message " + message);
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    super.disconnectCallback(channel, message);
                    Log.d("disconnectCallback", "message " + message);
                }
            });
        } catch (PubnubException pe) {
            Log.d(TAG, pe.toString());
        }

        //History
        Callback callbackHistory = new Callback() {
            public void successCallback(String channel, Object response) {

                System.out.println("callbackHistory::::" + response.toString());
                /**
                chatMessageList.add(response.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatAdapter.notifyItemInserted(chatMessageList.size() - 1);
                        chatList.scrollToPosition(chatMessageList.size() - 1);
                    }
                });
                 */
                try {
                    JSONArray json = (JSONArray) response;
                    final JSONArray messages = json.getJSONArray(0);

                    for (int i = 0; i < messages.length(); i++) {
                        try {
                            JSONObject jsonMsg = messages.getJSONObject(i);
                            String name = jsonMsg.getString("username");
                            String msg = jsonMsg.getString("message");
                            String fullName;
                            if((name.equals(selectedContactEmail))){
                                fullName = selectedContact;
                            }else{
                                fullName = username;
                            }

                            String message = gson.toJson(new Message(fullName, msg));

                            //messageObject = new JSONObject(message);

                            chatMessageList.add(message);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chatList.getRecycledViewPool().clear();
                                    chatAdapter.notifyDataSetChanged();
                                    chatList.scrollToPosition(chatMessageList.size() - 1);
                                }
                            });

                        } catch (JSONException e) { // Handle errors silently
                            e.printStackTrace();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
        pubnub.history(selectedContactChannel, 100, true, callbackHistory);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            callback.loginActivity(1);
            sharedPreferences.edit().remove("username").apply();
            return true;
        }else if(item.getItemId() == android.R.id.home){
            Toast.makeText(context, "Arrow buton pressed", Toast.LENGTH_SHORT).show();

            getActivity().getIntent().removeExtra("SELECTED_CONTACT");
            getActivity().getIntent().removeExtra("SELECTED_CONTACT_email");


            callback.loginActivity(0);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (send.getId() == view.getId()) {
            String message = chatMessage.getText().toString().trim();
            if (message.length() != 0) {
                message = gson.toJson(new Message(username, message));
                try {
                    messageObject = new JSONObject(message);
                } catch (JSONException je) {
                    Log.d(TAG, je.toString());
                }
                chatMessage.setText("");
                pubnub.publish(selectedContactChannel, messageObject, new Callback() {
                    @Override
                    public void successCallback(String channel, Object message) {
                        super.successCallback(channel, message);
                        Log.d("successCallback", "message>>>>>>>>>> " + message.toString());
                    }

                    @Override
                    public void errorCallback(String channel, PubnubError error) {
                        super.errorCallback(channel, error);
                        Log.d("errorCallback", "error " + error);
                    }
                });
            } else {
                Toast.makeText(context, "Please enter message", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pubnub.unsubscribe(PubnubKeys.CHANNEL_NAME);
        Log.d(TAG, "Un subscribed");
    }
}
