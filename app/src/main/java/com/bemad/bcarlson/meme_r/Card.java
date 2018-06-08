package com.bemad.bcarlson.meme_r;

/**
 * Created by bcarlson on 6/7/18.
 */

public class Card {
    private String userID;
    private String name;
    private String profileImageUrl;

    public Card(String userID, String name, String profileImageUrl) {
        this.userID = userID;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrlame(String name) {
        this.name = profileImageUrl;
    }
}
