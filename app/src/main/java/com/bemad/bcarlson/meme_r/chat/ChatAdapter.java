package com.bemad.bcarlson.meme_r.chat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bemad.bcarlson.meme_r.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bcarlson on 6/8/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{

    private ArrayList<ChatObject> chatList;
    private Context context;

    public ChatAdapter(ArrayList<ChatObject> matchesList, Context context) {
        this.chatList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolder rcv = new ChatViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.messageField.setText(chatList.get(position).getMessage());
        if (chatList.get(position).getCurrentUser()) {
            holder.messageField.setGravity(Gravity.END);
            holder.messageField.setTextColor(Color.WHITE);
            holder.containerLayout.setBackgroundColor(Color.BLUE);
        } else {
            holder.messageField.setGravity(Gravity.START);
            holder.messageField.setTextColor(Color.BLACK);
            holder.containerLayout.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
