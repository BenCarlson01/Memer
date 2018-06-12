package com.bemad.bcarlson.memer.matches;

/**
 * Created by bcarlson on 6/8/18.
 */

public class MatchesObject {
    private String userID, name, profileImgUrl;

    public MatchesObject(String userID, String name, String profileImgUrl) {
        this.userID = userID;
        this.name = name;
        this.profileImgUrl = profileImgUrl;
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
