package com.ervin.joker.pengguna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ervin.joker.MainActivityPersonalia;
import com.ervin.joker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by ervin on 6/8/2017.
 */

public class EditProfilePersonalia extends AppCompatActivity {
    private static final String TAG = "SignUpAsPersonalia";
    EditText edtEmail, edtPassword, edtNama, edtDescription, edtVideo;
    Button btnRegister;
    ImageView ivGambarProfile, ivUploadVideo;
    Uri rawPathImage;
    String pathImage;
    String linkPhoto;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private StorageReference mStorageRef;
    DatabaseReference myRef;
    private static final int PICK_IMAGE = ConstantsCustomGallery.REQUEST_CODE;
    private static final int GET_DATA = 7;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String jenis_pengguna = "Personalia";
    boolean gambar = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_profile_personalia);

        edtEmail = (EditText) findViewById(R.id.edt_edit_profile_email);
        edtNama = (EditText) findViewById(R.id.edt_edit_profile_nama);
        edtPassword = (EditText) findViewById(R.id.edt_edit_profile_password);
        edtDescription = (EditText) findViewById(R.id.edt_edit_profile_deskripsi);
        edtVideo = (EditText) findViewById(R.id.ed_edit_profile_videoid);
        btnRegister = (Button) findViewById(R.id.btn_edit_profile_personaloa);
        ivGambarProfile = (CircleImageView) findViewById(R.id.iv_edit_profile_photo);
        ivUploadVideo = (ImageView) findViewById(R.id.iv_edit_profile_upload_video);
        progressDialog = new ProgressDialog(EditProfilePersonalia.this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final FirebaseUser personaliaa = mAuth.getCurrentUser();
        myRef.child("User").child(personaliaa.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User personalia = dataSnapshot.getValue(User.class);
                String sgetNama =personalia.getNama();
                String sgetGambar =personalia.getPhoto_profil();
                String deskripsi = personalia.getDeskripsi();
                String videoID = personalia.getVideo_id();
                edtEmail.setText(personaliaa.getEmail());
                //edtNama.setText(sgetGambar);
                edtDescription.setText(deskripsi);
                edtVideo.setText(videoID);
                edtNama.setText(sgetNama);
                Log.d(TAG, "User password updated." + sgetGambar);
                if(!sgetGambar.isEmpty()){
                    gambar=true;
                    rawPathImage= Uri.parse(sgetGambar);
                }
                Glide.with(EditProfilePersonalia.this).load(sgetGambar).placeholder(R.drawable.ic_menu_gallery).dontAnimate().into(ivGambarProfile);
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                String email = edtEmail.getText().toString();
                final String nama = edtNama.getText().toString();
                String password = edtPassword.getText().toString();
                final String deskripsi = edtDescription.getText().toString();
                final String video = edtVideo.getText().toString();
                final FirebaseUser user = mAuth.getCurrentUser();
                if(email.isEmpty()||password.isEmpty()||nama.isEmpty()||gambar==false||video.isEmpty()||deskripsi.isEmpty()) {
                    Toast.makeText(EditProfilePersonalia.this, "Isikan semua field yang ada",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else {
                    user.updateEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User email address updated.");
                                    }
                                }
                            });
                    user.updatePassword(password)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                    }
                                }
                            });
                    StorageReference riversRef = mStorageRef.child("images/" + user.getUid() + "/" + "phto_profile.jpg");
                    riversRef.putFile(rawPathImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    @SuppressWarnings("VisibleForTests")
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    linkPhoto = String.valueOf(downloadUrl);
                                    User pelamar = new User(nama, linkPhoto, jenis_pengguna, deskripsi, video);
                                    myRef.child("User").child(user.getUid()).setValue(pelamar);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {

                                    // Handle unsuccessful uploads
                                    // ...
                                }
                            });
                    progressDialog.dismiss();
                    Intent intent = new Intent(EditProfilePersonalia.this,MainActivityPersonalia.class);
                    startActivity(intent);
                }
            }
        });

        ivGambarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfilePersonalia.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
            }
        });

        ivUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfilePersonalia.this, com.ervin.joker.video.MainActivity.class);
                startActivityForResult(intent, GET_DATA);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_IMAGE:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

                    for (int i = 0; i < images.size(); i++) {
                        Uri uri = Uri.fromFile(new File(images.get(i).path));
                        // start play with image uri
                        rawPathImage= uri;
                    }
                    // pathImage = rawPathImage.replaceAll("file://","");// untuk menghilangkan "file//" pada path gambar
                    Glide.with(EditProfilePersonalia.this).load(rawPathImage).dontAnimate().into(ivGambarProfile);
                    gambar = true;
                }
                break;
            case GET_DATA:
                if(resultCode == RESULT_OK && data != null){
                    String youtubeId = data.getStringExtra("ancok");
                    edtVideo.setText(youtubeId);
                    break;
                }
        }

    }
}
