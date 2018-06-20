package com.bemad.bcarlson.memer.comments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bemad.bcarlson.memer.R;

/**
 * Created by bcarlson on 6/20/18.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView commentField, likesField, dislikesField;
    public ImageView commentImage,likesButton, dislikesButton;
    public LinearLayout fullLayout, subLayout, likeLayout;

    public CommentViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        fullLayout = itemView.findViewById(R.id.commentFullLayout);
        subLayout = itemView.findViewById(R.id.commentSubLayout);
        likeLayout = itemView.findViewById(R.id.commentLikeLayout);
        commentImage = itemView.findViewById(R.id.commentImage);
        likesButton = itemView.findViewById(R.id.commentLikeButton);
        dislikesButton = itemView.findViewById(R.id.commentDislikeButton);
        commentField = itemView.findViewById(R.id.commentMessage);
        likesField = itemView.findViewById(R.id.commentNumLikes);
        dislikesField = itemView.findViewById(R.id.commentNumDislikes);
    }

    @Override
    public void onClick(View view) {

    }
}