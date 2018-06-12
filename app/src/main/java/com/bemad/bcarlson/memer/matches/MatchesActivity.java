package com.bemad.bcarlson.memer.matches;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bemad.bcarlson.memer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter matchesAdapter;
    private RecyclerView.LayoutManager matchesLayoutManager;
    private ArrayList<MatchesObject> resultsMatches = new ArrayList<>();

    private String currentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        currentID = FirebaseAuth.getInstance()
                .getCurrentUser()
                .getUid();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        matchesLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        recyclerView.setLayoutManager(matchesLayoutManager);

        matchesAdapter = new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);
        recyclerView.setAdapter(matchesAdapter);

        getUserMatchID();
    }

    private ArrayList<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }

    private void getUserMatchID() {
        DatabaseReference matchDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(currentID)
                .child("connections")
                .child("matches");
        matchDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot match : dataSnapshot.getChildren()) {
                        fetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchMatchInformation(String key) {
        DatabaseReference userDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(key);
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userID = dataSnapshot.getKey();
                    String name = "";
                    String profileImgUrl = "";

                    if (dataSnapshot.child("name").getValue() != null) {
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if (dataSnapshot.child("profileImgUrl").getValue() != null) {
                        profileImgUrl = dataSnapshot.child("profileImgUrl").getValue().toString();
                    }

                    MatchesObject obj = new MatchesObject(userID, name, profileImgUrl);
                    resultsMatches.add(obj);
                    matchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
