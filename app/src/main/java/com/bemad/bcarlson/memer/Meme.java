package com.bemad.bcarlson.memer;

/**
 * Created by bcarlson on 6/12/18.
 */

public class Meme {
    private String memeID, memeUrl;

    public Meme(String memeID, String memeUrl) {
        this.memeID = memeID;
        this.memeUrl = memeUrl;
    }

    public String getMemeID() {
        return memeID;
    }

    public void setMemeID(String memeID) {
        this.memeID = memeID;
    }

    public String getMemeUrl() {
        return memeUrl;
    }

    public void setMemeUrl(String memeUrl) {
        this.memeUrl = memeUrl;
    }
}