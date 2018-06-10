package com.bemad.bcarlson.meme_r.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bemad.bcarlson.meme_r.R;

/**
 * Created by bcarlson on 6/8/18.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView chatIDField, chatNameField;
    public ImageView chatImage;

    public ChatViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

}
