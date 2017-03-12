package pojo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by AZN on 2016-12-21.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String phone;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

}