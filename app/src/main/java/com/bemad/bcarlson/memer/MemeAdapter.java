package com.bemad.bcarlson.memer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by bcarlson on 6/12/18.
 */

public class MemeAdapter extends ArrayAdapter<Meme>{
    private Context context;

    public MemeAdapter(Context context, int resourceID, ArrayList<Meme> items) {
        super(context, resourceID, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Meme meme = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.image);
        System.out.println("Meme URL: " + meme.getMemeUrl());
        new Helper.DownloadImageTask(image).execute(meme.getMemeUrl());
        //Glide.with(convertView.getContext()).load(meme.getMemeUrl()).into(image);
        return convertView;
    }
}
