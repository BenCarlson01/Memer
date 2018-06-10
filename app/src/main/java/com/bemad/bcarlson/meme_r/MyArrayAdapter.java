package com.bemad.bcarlson.meme_r;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bemad.bcarlson.meme_r.cards.Card;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bcarlson on 6/7/18.
 */

public class MyArrayAdapter extends ArrayAdapter<Card> {
    private Context context;

    public MyArrayAdapter(Context context, int resourceID, ArrayList<Card> items) {
        super(context, resourceID, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Card cardItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);

        name.setText(cardItem.getName());
        switch(cardItem.getProfileImgUrl()) {
            case "default":
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                break;
            default:
                Glide.with(convertView.getContext()).load(cardItem.getProfileImgUrl()).into(image);
        }
        return convertView;
    }
}
