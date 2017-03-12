package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahzaumarang.socialmessenger.R;

/**
 * Created by AZN on 2016-12-19.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout chatHolder;
    TextView username;
    TextView message;

    public ChatViewHolder(View itemView) {
        super(itemView);

        username = (TextView) itemView.findViewById(R.id.username);
        message = (TextView) itemView.findViewById(R.id.message);
        chatHolder = (RelativeLayout) itemView.findViewById(R.id.chatholder);
    }
}