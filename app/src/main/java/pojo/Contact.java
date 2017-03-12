package pojo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by AZN on 2016-12-21.
 */
@IgnoreExtraProperties
public class Contact {

    public String email;
    public String channel;

    public Contact() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Contact(String email, String channel) {
        this.email = email;
        this.channel = channel;
    }
}
