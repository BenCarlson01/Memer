package com.bemad.bcarlson.meme_r.chat;

/**
 * Created by bcarlson on 6/8/18.
 */

public class ChatObject {

    private String message;
    private boolean currentUser;

    public ChatObject(String message, boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        this.currentUser = currentUser;
    }
}
