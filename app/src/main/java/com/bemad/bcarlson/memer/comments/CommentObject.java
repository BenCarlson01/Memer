package com.bemad.bcarlson.memer.comments;


import com.google.firebase.database.DatabaseReference;

/**
 * Created by bcarlson on 6/20/18.
 */

public class CommentObject {

    private String userID, comment, commentID, react;
    private long likes, dislikes;
    private DatabaseReference db;

    public CommentObject(String userID, String comment, String commentID,
            long likes, long dislikes, DatabaseReference db) {
        this.userID = userID;
        this.comment = comment;
        this.commentID = commentID;
        this.likes = likes;
        this.dislikes = dislikes;
        this.db = db;
        react = "none";
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

    public String getCommentID() {
        return commentID;
    }

    public DatabaseReference getDB() {
        return db;
    }

    public void setReact(String react) {
        this.react = react;
    }

    public String getReact() {
        return react;
    }
}
