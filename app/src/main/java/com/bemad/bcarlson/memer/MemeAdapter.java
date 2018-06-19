package com.bemad.bcarlson.memer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bcarlson on 6/12/18.
 */

public class MemeAdapter extends ArrayAdapter<Meme> {
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
        Glide.with(convertView.getContext()).load(meme.getMemeUrl()).into(image);
        return convertView;
    }
}
