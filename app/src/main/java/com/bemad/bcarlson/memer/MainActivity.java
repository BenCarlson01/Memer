package com.bemad.bcarlson.memer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bemad.bcarlson.memer.cards.Card;
import com.bemad.bcarlson.memer.matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    /**
     * Made following TinderClone Tutorial by SimCoder:
     *      Channel: https://www.youtube.com/channel/UCQ5xY26cw5Noh6poIE-VBog
     *      TinderClone Tutorial Videos: https://www.youtube.com/watch?v=lMhkxJIDv44&list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf
     *      Watched Episode 2 to make this class
     * Created copying example code from Swipecards GitHub:
     *      https://github.com/Diolor/Swipecards
     * Uses Swipecards to get Tinder-like swiping motion
     */

    private MyArrayAdapter arrayAdapter;
    private FirebaseAuth auth;
    private DatabaseReference usersDB;

    private String currentUID;
    private String gender;
    private String preferredGender;

    private ArrayList<Card> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users");
        auth = FirebaseAuth.getInstance();
        currentUID = auth.getCurrentUser().getUid();

        checkUserPreference();
        rowItems = new ArrayList<>();

        arrayAdapter = new MyArrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                if (!rowItems.isEmpty()) {
                    rowItems.remove(0);
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
                Card card = (Card) dataObject;
                String userID = card.getUserID();
                usersDB.child(userID)
                        .child("connections")
                        .child("like")
                        .child(currentUID)
                        .removeValue();
                usersDB.child(userID)
                        .child("connections")
                        .child("dislike")
                        .child(currentUID)
                        .setValue(true);
                makeToast(MainActivity.this, "Dislike!");
            }

            /**
             * Right swipe means like
             */
            @Override
            public void onRightCardExit(Object dataObject) {
                Card card = (Card) dataObject;
                String userID = card.getUserID();
                usersDB.child(userID)
                        .child("connections")
                        .child("like")
                        .child(currentUID)
                        .setValue(true);
                usersDB.child(userID)
                        .child("connections")
                        .child("dislike")
                        .child(currentUID)
                        .removeValue();
                isMatch(userID);
                makeToast(MainActivity.this, "Like!");
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
                makeToast(MainActivity.this, "Clicked!");
            }
        });

    }

    private void isMatch(String userID) {
        DatabaseReference connectionDB = usersDB
                .child(currentUID)
                .child("connections")
                .child("like")
                .child(userID);
        connectionDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    makeToast(MainActivity.this, "New Match!");

                    String key = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Chat")
                            .push()
                            .getKey();
                    usersDB.child(dataSnapshot.getKey())
                            .child("connections")
                            .child("matches")
                            .child(currentUID)
                            .child("chatID")
                            .setValue(key);
                    usersDB.child(currentUID)
                            .child("connections")
                            .child("matches")
                            .child(dataSnapshot.getKey())
                            .child("chatID")
                            .setValue(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void checkUserPreference() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(user.getUid());
        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("gender").getValue() != null) {
                        gender = dataSnapshot.child("gender").getValue().toString();
                        //add more here for more genders
                        switch (gender) {
                            case "Male":
                                preferredGender = "Female";
                                break;
                            case "Female":
                                preferredGender = "Male";
                                break;
                            default:
                                System.out.println("Non-existent gender");
                        }
                        getPreferredGenderUsers();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getPreferredGenderUsers() {
        usersDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()
                        && dataSnapshot.child("gender").getValue() != null) {
                    if (!dataSnapshot.child("connections")
                                .child("dislike")
                                .hasChild(currentUID)
                        && !dataSnapshot.child("connections")
                                .child("like")
                                .hasChild(currentUID)
                        //Right now only works for male-female
                        && dataSnapshot.child("gender")
                                .getValue()
                                .toString()
                                .equals(preferredGender)) {
                        String profileImgUrl = "default";
                        if (!dataSnapshot.child("profileImgUrl").equals("default")) {
                            profileImgUrl = dataSnapshot.child("profileImgUrl").getValue().toString();
                        }
                        Card item = new Card(dataSnapshot.getKey(),
                                dataSnapshot.child("name").getValue().toString(),
                                profileImgUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    //Delete this "else if" if you only want to see each thing once
                    } else if (dataSnapshot.child("connections")
                                .child("like")
                                .hasChild(currentUID)
                            && dataSnapshot.child("gender")
                                    .getValue()
                                    .toString()
                                    .equals(preferredGender)) {
                        String profileImgUrl = "default";
                        if (!dataSnapshot.child("profileImgUrl").equals("default")) {
                            profileImgUrl = dataSnapshot.child("profileImgUrl").getValue().toString();
                        }
                        Card item = new Card(dataSnapshot.getKey(),
                                "Liked:" + dataSnapshot.child("name").getValue().toString(),
                                profileImgUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    } else if (dataSnapshot.child("gender")
                            .getValue()
                            .toString()
                            .equals(preferredGender)){
                        String profileImgUrl = "default";
                        if (!dataSnapshot.child("profileImgUrl").equals("default")) {
                            profileImgUrl = dataSnapshot.child("profileImgUrl").getValue().toString();
                        }
                        Card item = new Card(dataSnapshot.getKey(),
                                "Disliked:" + dataSnapshot.child("name").getValue().toString(),
                                profileImgUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
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

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public void signOut(View view) {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginRegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
    }
}