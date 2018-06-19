package com.bemad.bcarlson.memer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Meme> memeList;
    private MemeAdapter arrayAdapter;

    private DatabaseReference userDB, memeDB;
    private String userID, userCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDB = FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userID);

        memeList = new ArrayList<>();
        arrayAdapter = new MemeAdapter(this, R.layout.item_meme, memeList);
        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                // Log.d("LIST", "removed object!");
                if (!memeList.isEmpty()) {
                    memeList.remove(0);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            /**
             * Left swipe means dislike
             */
            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Meme meme = (Meme) dataObject;
                updateMemeDatabase("dislikes", meme);
                Toast.makeText(MainActivity.this, "Dislike!", Toast.LENGTH_SHORT).show();
            }

            /**
             * Right swipe means like
             */
            @Override
            public void onRightCardExit(Object dataObject) {
                Meme meme = (Meme) dataObject;
                updateMemeDatabase("likes", meme);
                Toast.makeText(MainActivity.this, "Like!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
//                al.add("XML ".concat(String.valueOf(i)));
//                arrayAdapter.notifyDataSetChanged();
//                Log.d("LIST", "notified");
//                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        getNewMemes();
    }

    /**
     * Loads new memes into ArrayAdapter for the user to view
     * UNIMPLEMENTED - Users can only view memes that they haven't seen before
     * UNIMPLEMENTED - Users only load at most 100 or some number of memes at a time
     */
    private void getNewMemes() {
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Enter: " + dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    System.out.println("Enter if: " + dataSnapshot.toString());
                    String userCountry = dataSnapshot.child("country").getValue().toString();
                    memeDB = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("country")
                            .child(userCountry)
                            .child("memes");
                    memeDB.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            System.out.println("Enter: " + dataSnapshot.toString());
                            if (dataSnapshot.exists()) {
                                System.out.println("Enter if: " + dataSnapshot.toString());
                                Meme meme = new Meme(dataSnapshot.getKey(),
                                        dataSnapshot.child("download").getValue().toString());
                                memeList.add(meme);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Updates meme's likes/dislikes in its database location
     * Made this method because I didn't want to type the same thing twice
     *
     * @param type: must be "like" or "dislike" - determines if updating likes or dislikes
     * @param meme: Meme that we are updating
     */
    private void updateMemeDatabase(final String type, final Meme meme) {
        memeDB.child(meme.getMemeID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            long count = (long) dataSnapshot.child("num_" + type).getValue();
                            memeDB.child(meme.getMemeID())
                                    .child(type)
                                    .child("" + count)
                                    .setValue(userID);
                            memeDB.child(meme.getMemeID())
                                    .child("num_" + type)
                                    .setValue(count + 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long count = (long) dataSnapshot.child("num_seen").getValue();
                    userDB.child("seen")
                            .child("" + count)
                            .setValue(meme.getMemeID());
                    userDB.child("num_seen")
                            .setValue(count + 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Adds MainActivity's toolbar to MainActivity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_toolbar, menu);
        return true;
    }

    /**
     * Adds onClickListeners to MainActivity's toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.main_toolbar_add_meme:
                intent = new Intent(this, SubmitMemeActivity.class);
                startActivity(intent);
                break;
            case R.id.main_toolbar_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.main_toolbar_signout:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                return false;
        }
        return true;
    }
}
