package com.bemad.bcarlson.memer.comments;


/**
 * Created by bcarlson on 6/20/18.
 */

public class CommentObject {

    private String userID, comment;
    private long likes, dislikes;

    public CommentObject(String userID, String comment, long likes, long dislikes) {
        this.userID = userID;
        this.comment = comment;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getLikes() {
        return likes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public long getDislikes() {
        return dislikes;
    }
}
