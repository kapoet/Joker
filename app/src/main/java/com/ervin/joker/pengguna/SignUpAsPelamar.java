package com.ervin.joker.pengguna;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ervin.joker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by ervin on 5/30/2017.
 */

public class SignUpAsPelamar extends AppCompatActivity {
    private static final String TAG = "SignUpAsPersonalia";
    EditText edtEmail, edtPassword, edtNama, edtDescription, edtVideo;
    Button btnRegister;
    ImageView ivGambarProfile, ivUploadVideo;
    Uri rawPathImage=null;
    String pathImage;
    String linkPhoto;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private StorageReference mStorageRef;
    DatabaseReference myRef;
    private static final int PICK_IMAGE = ConstantsCustomGallery.REQUEST_CODE;
    private static final int GET_DATA = 7;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String jenis_pengguna = "Pelamar_pekerjaan";
    boolean gambar = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ervin.joker.R.layout.layout_sign_up_pelamar);
        edtEmail = (EditText) findViewById(R.id.edt_sign_up_pelamar_email);
        edtNama = (EditText) findViewById(R.id.edt_sign_up_pelamar_nama);
        edtPassword = (EditText) findViewById(R.id.edt_sign_up_pelamar_password);
        btnRegister = (Button) findViewById(R.id.btn_signup_pelamar);
        ivGambarProfile = (ImageView) findViewById(R.id.iv_sign_up_pelamar_gambar);
        ivUploadVideo = (ImageView) findViewById(R.id.iv_sign_up_personaloa_upload_video);
        progressDialog = new ProgressDialog(SignUpAsPelamar.this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                String email = edtEmail.getText().toString();
                final String nama = edtNama.getText().toString();
                String password = edtPassword.getText().toString();
                final String deskripsi = "khkh";
                final String video = "gkgkgkgkgkg";
                if (email.isEmpty() || password.isEmpty() || nama.isEmpty() || gambar==false) {
                    Toast.makeText(SignUpAsPelamar.this, "Isikan semua field yang ada",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpAsPelamar.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpAsPelamar.this, "Akun tersebut telah terdaftar",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        //Uri file = Uri.fromFile(new File(pathImage));
                                        //String filename=pathImage.substring(pathImage.lastIndexOf("/")+1);
                                        final FirebaseUser user = mAuth.getCurrentUser();
                                        StorageReference riversRef = mStorageRef.child("images/" + user.getUid() + "/" + "phto_profile.jpg");
                                        riversRef.putFile(rawPathImage)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        // Get a URL to the uploaded content
                                                        @SuppressWarnings("VisibleForTests")
                                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                        linkPhoto = String.valueOf(downloadUrl);
                                                        User pelamar = new User(nama, linkPhoto, jenis_pengguna);
                                                        myRef.child("User").child(user.getUid()).setValue(pelamar);
                                                        progressDialog.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        mAuth.signOut();
                                                        progressDialog.dismiss();
                                                        // Handle unsuccessful uploads
                                                        // ...
                                                    }
                                                });

                                    }

                                    // ...
                                }
                            });
                }
            }
        });

        ivGambarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFilePermissions();
                Intent intent = new Intent(SignUpAsPelamar.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
            }
        });



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(SignUpAsPelamar.this, com.ervin.joker.MainActivity.class);
                    intent.putExtra("pengguna",jenis_pengguna);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_IMAGE:
                if(resultCode == Activity.RESULT_OK && data != null){
                    ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

                    for (int i = 0; i < images.size(); i++) {
                        Uri uri = Uri.fromFile(new File(images.get(i).path));
                        // start play with image uri
                        rawPathImage= uri;
                    }
                    // pathImage = rawPathImage.replaceAll("file://","");// untuk menghilangkan "file//" pada path gambar

                    Glide.with(this).load(rawPathImage).into(ivGambarProfile);
                    gambar=true;
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

    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck =SignUpAsPelamar.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck += SignUpAsPelamar.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            }
            if (permissionCheck != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
                }
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
