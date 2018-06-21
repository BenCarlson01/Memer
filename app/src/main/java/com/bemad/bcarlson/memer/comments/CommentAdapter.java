package com.bemad.bcarlson.memer.comments;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bemad.bcarlson.memer.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bcarlson on 6/20/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>{

    private ArrayList<CommentObject> commentList;
    private Context context;

    public CommentAdapter(ArrayList<CommentObject> matchesList, Context context) {
        this.commentList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_comment, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new CommentViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.commentField.setText(commentList.get(position).getComment());
        String likes = "" + commentList.get(position).getLikes();
        holder.likesField.setText(likes);
        String dislikes = "" + commentList.get(position).getLikes();
        holder.dislikesField.setText(dislikes);
        Glide.with(context)
                .load(R.mipmap.ic_launcher)
                .into(holder.commentImage);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
