package com.bemad.bcarlson.tinder_clone.cards;

/**
 * Created by bcarlson on 6/7/18.
 */

public class Card {
    private String userID;
    private String name;
    private String profileImgUrl;

    public Card(String userID, String name, String profileImageUrl) {
        this.userID = userID;
        this.name = name;
        this.profileImgUrl = profileImageUrl;
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

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }
}
