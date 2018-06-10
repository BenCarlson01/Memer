package com.bemad.bcarlson.meme_r.matches;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bemad.bcarlson.meme_r.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bcarlson on 6/8/18.
 */

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolder>{

    private ArrayList<MatchesObject> matchesList;
    private Context context;

    public MatchesAdapter(ArrayList<MatchesObject> matchesList, Context context) {
        this.matchesList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_matches, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MatchesViewHolder rcv = new MatchesViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolder holder, int position) {
        holder.matchIDField.setText(matchesList.get(position).getUserID());
        holder.matchNameField.setText(matchesList.get(position).getName());
        if (matchesList.get(position).getProfileImgUrl().equals("default")) {
            Glide.with(context)
                    .load(R.mipmap.ic_launcher)
                    .into(holder.matchImage);
        } else {
            Glide.with(context)
                    .load(matchesList.get(position).getProfileImgUrl())
                    .into(holder.matchImage);
        }
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
