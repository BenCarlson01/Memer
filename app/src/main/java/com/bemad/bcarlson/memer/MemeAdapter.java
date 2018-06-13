package com.bemad.bcarlson.memer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bemad.bcarlson.memer.cards.Card;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bcarlson on 6/12/18.
 */

public class MemeAdapter  extends ArrayAdapter<Meme> {
    private Context context;

    public MemeAdapter(Context context, int resourceID, ArrayList<Meme> items) {
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
        if (cardItem.getProfileImgUrl().equals("default")) {
            Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
        } else {
            Glide.with(convertView.getContext()).load(cardItem.getProfileImgUrl()).into(image);
        }
        return convertView;
    }
}
