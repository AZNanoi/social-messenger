package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;

import pojo.Message;
import com.ahzaumarang.socialmessenger.R;

/**
 * Created by AZN on 2016-12-19.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    ArrayList<String> chatMessageList;
    Gson gson = new Gson();
    String message;
    String username;
    String myUsername;
    Context context;

    public ChatAdapter(ArrayList<String> chatMessageList, String myUsername) {
        this.chatMessageList = chatMessageList;
        this.myUsername = myUsername;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {

        message = chatMessageList.get(position);
        Message messageObject = gson.fromJson(message, Message.class);

        username = messageObject.getUsername();
        holder.username.setText(messageObject.getUsername());
        if (!(username.equals(myUsername))) {
            holder.message.setBackground(context.getResources().getDrawable(R.drawable.received_message_container));
            holder.message.setTextColor(context.getResources().getColor(R.color.black));

            RelativeLayout.LayoutParams message_params = (RelativeLayout.LayoutParams)holder.message.getLayoutParams();
            message_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            message_params.setMarginStart((int)context.getResources().getDimension(R.dimen.message_container_mg));
            message_params.setMarginEnd(0);
            holder.message.setLayoutParams(message_params);

            RelativeLayout.LayoutParams username_params = (RelativeLayout.LayoutParams)holder.username.getLayoutParams();
            username_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            holder.username.setLayoutParams(username_params);

            //holder.chatHolder.setBackgroundColor(context.getResources().getColor(R.color.skyBlue_light));
        }
        holder.message.setText(messageObject.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }
}
