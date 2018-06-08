package com.bemad.bcarlson.meme_r;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private EditText nameField, phoneField;
    private Button confirmButton;
    private ImageView profileImg;

    private FirebaseAuth auth;
    private DatabaseReference genderDB;

    private String userID, name, phone, profileImgUrl;

    private Uri resultUri;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String gender = getIntent().getExtras().getString("gender");
        nameField = findViewById(R.id.name);
        phoneField = findViewById(R.id.phone);
        profileImg = findViewById(R.id.profile);
        confirmButton = findViewById(R.id.confirm);

        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        genderDB = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(gender)
                .child(userID);

        getUserInfo();
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

    }

    private void getUserInfo() {
        genderDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    HashMap<String, Object> info = (HashMap) dataSnapshot.getValue();
                    if (info.get("name") != null) {
                        name = info.get("name").toString();
                        nameField.setText(name);
                    }
                    if (info.get("phone") != null) {
                        phone = info.get("phone").toString();
                        phoneField.setText(phone);
                    }
                    if (info.get("profileImgUrl") != null) {
                        profileImgUrl = info.get("profileImgUrl").toString();
                        Glide.with(getApplication()).load(profileImgUrl).into(profileImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveUserInformation(){
        name = nameField.getText().toString();
        phone = phoneField.getText().toString();
        HashMap<String, Object> info = new HashMap<>();
        info.put("name", name);
        info.put("phone", phone);
        genderDB.updateChildren(info);

        if (resultUri != null) {
            final StorageReference filePath = FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("profileImages")
                    .child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            HashMap<String, Object> info = new HashMap<>();
                            info.put("profileImgUrl", downloadUrl.toString());
                            genderDB.updateChildren(info);
                            finish();
                        }
                    });
                }
            });
        } else {
            finish();
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            profileImg.setImageURI(resultUri);
        }
    }
}
