package com.bemad.bcarlson.meme_r.matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bemad.bcarlson.meme_r.R;
import com.bemad.bcarlson.meme_r.chat.ChatActivity;

/**
 * Created by bcarlson on 6/8/18.
 */

public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView matchIDField, matchNameField;
    public ImageView matchImage;

    public MatchesViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        matchIDField = itemView.findViewById(R.id.matchID);
        matchNameField = itemView.findViewById(R.id.matchName);
        matchImage = itemView.findViewById(R.id.matchImage);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchID", matchIDField.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }

}
