package com.bemad.bcarlson.meme_r.matches;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bemad.bcarlson.meme_r.R;

import java.util.ArrayList;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter matchesAdapter;
    private RecyclerView.LayoutManager matchesLayoutManager;
    private ArrayList<MatchesObject> resultsMatches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        matchesLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        recyclerView.setLayoutManager(matchesLayoutManager);

        matchesAdapter = new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);
        recyclerView.setAdapter(matchesAdapter);

        MatchesObject obj = null;
        for (int i = 0; i < 100; i++) {
            obj = new MatchesObject("Temp UserID #" + i);
            resultsMatches.add(obj);
        }
        matchesAdapter.notifyDataSetChanged();
    }

    private ArrayList<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }
}
