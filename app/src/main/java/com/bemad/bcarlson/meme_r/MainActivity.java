package com.bemad.bcarlson.meme_r;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    private Button signOutButton;
    private Card cardsData[];
    private MyArrayAdapter arrayAdapter;
    private FirebaseAuth auth;
    private String currentUID;
    private DatabaseReference usersDB;

    private ListView listItems;
    private ArrayList<Card> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signOutButton = findViewById(R.id.signOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        usersDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users");
        auth = FirebaseAuth.getInstance();
        currentUID = auth.getCurrentUser().getUid();

        checkUserGender();
        rowItems = new ArrayList<>();

        arrayAdapter = new MyArrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
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
                usersDB.child(preferredGender)
                        .child(userID)
                        .child("connections")
                        .child("like")
                        .child(currentUID)
                        .removeValue();
                usersDB.child(preferredGender)
                        .child(userID)
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
                usersDB.child(preferredGender)
                        .child(userID)
                        .child("connections")
                        .child("like")
                        .child(currentUID)
                        .setValue(true);
                usersDB.child(preferredGender)
                        .child(userID)
                        .child("connections")
                        .child("dislike")
                        .child(currentUID)
                        .removeValue();
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

    private String preferredGender;

    public void checkUserGender() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference maleDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child("Male");
        maleDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    preferredGender = "Female";
                    getpreferredGenderUsers();
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

        DatabaseReference femaleDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child("Female");
        femaleDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    preferredGender = "Male";
                    getpreferredGenderUsers();
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

    public void getpreferredGenderUsers() {
        DatabaseReference preferredGenderDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(preferredGender);
        preferredGenderDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()
                        && !dataSnapshot.child("connections")
                                .child("dislike")
                                .hasChild(currentUID)
                        && !dataSnapshot.child("connections")
                                .child("like")
                                .hasChild(currentUID)) {
                    Card item = new Card(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString());
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                } else if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("connections")
                            .child("like")
                            .hasChild(currentUID)) {
                        Card item = new Card(dataSnapshot.getKey(),
                                "Liked:\n" + dataSnapshot.child("name").getValue().toString());
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        Card item = new Card(dataSnapshot.getKey(),
                                "Disliked:\n" + dataSnapshot.child("name").getValue().toString());
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

    public void signOutMethod(View view) {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginRegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}