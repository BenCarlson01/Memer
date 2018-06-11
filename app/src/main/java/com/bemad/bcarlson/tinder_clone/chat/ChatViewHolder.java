package com.bemad.bcarlson.tinder_clone.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bemad.bcarlson.tinder_clone.R;
import com.bumptech.glide.Glide;

/**
 * Created by bcarlson on 6/8/18.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView messageField;
    public ImageView matchImg, userImg;
    public LinearLayout mainLayout, containerLayout;

    public ChatViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        messageField = itemView.findViewById(R.id.message);
        matchImg = itemView.findViewById(R.id.matchImg);
        userImg = itemView.findViewById(R.id.userImg);
        mainLayout = itemView.findViewById(R.id.layout);
        containerLayout = itemView.findViewById(R.id.container);
    }

    @Override
    public void onClick(View view) {

    }

}
