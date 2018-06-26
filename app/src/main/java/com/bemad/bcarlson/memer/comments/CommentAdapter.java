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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by bcarlson on 6/20/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>{

    private ArrayList<CommentObject> commentList;
    private Context context;
    private DatabaseReference userDB;

    public CommentAdapter(ArrayList<CommentObject> matchesList, Context context,
                          DatabaseReference userDB) {
        this.commentList = matchesList;
        this.context = context;
        this.userDB = userDB;
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
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, int position) {
        final CommentObject comment = commentList.get(position);
        holder.commentField.setText(comment.getComment());
        String likes = "" + comment.getLikes();
        holder.likesField.setText(likes);
        String dislikes = "" + comment.getLikes();
        holder.dislikesField.setText(dislikes);
        Glide.with(context)
                .load(R.mipmap.ic_launcher)
                .into(holder.commentImage);
        comment.setReact("none");
        userDB.child("likes").orderByKey().equalTo(comment.getCommentID()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.getValue().toString().equals("liked")) {
                                comment.setReact("liked");
                                holder.likesButton.setImageDrawable(ContextCompat
                                        .getDrawable(context, R.drawable.thumbs_up_full));
                            } else if (dataSnapshot.getValue().toString().equals("disliked")) {
                                comment.setReact("disliked");
                                holder.dislikesButton.setImageDrawable(ContextCompat
                                        .getDrawable(context, R.drawable.thumbs_down_full));
                            } else {
                                //Database Error
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
        holder.likesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference commentDB = comment.getDB();
                switch (comment.getReact()) {
                    case "disliked":
                        commentDB.child("num_dislikes").setValue(comment.getDislikes() - 1);
                        String dislikes = "" + (comment.getDislikes() - 1);
                        holder.dislikesField.setText(dislikes);
                        holder.dislikesButton.setImageDrawable(ContextCompat
                                .getDrawable(context, R.drawable.thumbs_down_empty));
                        break;
                    case "none":
                        commentDB.child("num_likes").setValue(comment.getLikes() + 1);
                        userDB.child("likes").child(comment.getCommentID())
                                .setValue("liked");
                        String likes = "" + (comment.getLikes() + 1);
                        holder.likesField.setText(likes);
                        holder.likesButton.setImageDrawable(ContextCompat
                                .getDrawable(context, R.drawable.thumbs_up_full));
                        break;
                    case "liked":
                        //Does nothing
                        break;
                    default:
                        //Error
                }
            }
        });
        holder.dislikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference commentDB = comment.getDB();
                switch (comment.getReact()) {
                    case "liked":
                        commentDB.child("num_likes").setValue(comment.getLikes() - 1);
                        String likes = "" + (comment.getLikes() - 1);
                        holder.likesField.setText(likes);
                        holder.likesButton.setImageDrawable(ContextCompat
                                .getDrawable(context, R.drawable.thumbs_up_empty));
                        break;
                    case "none":
                        commentDB.child("num_dislikes").setValue(comment.getDislikes() + 1);
                        userDB.child("likes").child(comment.getCommentID())
                                .setValue("disliked");
                        String dislikes = "" + (comment.getDislikes() + 1);
                        holder.dislikesField.setText(dislikes);
                        holder.dislikesButton.setImageDrawable(ContextCompat
                                .getDrawable(context, R.drawable.thumbs_down_full));
                        break;
                    case "disliked":
                        //Does nothing
                        break;
                    default:
                        //Error
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}