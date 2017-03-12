package pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AZN on 2016-12-19.
 */

public class Message {

    @SerializedName("username")
    String username;
    @SerializedName("message")
    String message;

    public Message(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}