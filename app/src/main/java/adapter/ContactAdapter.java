package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.ahzaumarang.socialmessenger.ChatFragment;
import com.ahzaumarang.socialmessenger.MainActivity;
import com.ahzaumarang.socialmessenger.SignUp;
import com.google.gson.Gson;

import java.util.ArrayList;

import pojo.Message;
import com.ahzaumarang.socialmessenger.R;
import callback.CustomCallback;
import com.ahzaumarang.socialmessenger.Contacts;
import com.ahzaumarang.socialmessenger.ChatFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

/**
 * Created by AZN on 2016-12-19.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Context context;
    private ArrayList<ArrayList<String>> mDataset;
    Gson gson = new Gson();
    String message;
    String username;
    String myUsername;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView usernameEdit;

        public ViewHolder(View v) {
            super(v);
            usernameEdit = (TextView) v.findViewById(R.id.username);
        }
    }

    public void add(int position, ArrayList<String> item) {
        mDataset.add(item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactAdapter(ArrayList<ArrayList<String>> chatMessageList) {
        this.mDataset = chatMessageList;
        //this.myUsername = myUsername;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();


        // create a new view
        View v = LayoutInflater.from(context).inflate(R.layout.contacts_item_layout, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final ArrayList<String> name = mDataset.get(position);
        holder.usernameEdit.setText(name.get(0));
        holder.usernameEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove(name);
                //Contacts contacts = new Contacts();

                Bundle bundle = new Bundle();
                bundle.putString("friend", name.get(0) );
                ChatFragment fragInfo = new ChatFragment();
                fragInfo.setArguments(bundle);

                Intent k = new Intent(context, MainActivity.class);
                k.putExtra("SELECTED_CONTACT", name.get(0));
                k.putExtra("SELECTED_CONTACT_email", name.get(1));
                k.putExtra("SELECTED_CONTACT_channel", name.get(2));

                context.startActivity(k);
                /**
                Contacts myActivity = (Contacts)context;
                fragmentManager = myActivity.getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.contactContainer, fragInfo, "FriendChat");
                fragmentTransaction.commit();
                 */
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
