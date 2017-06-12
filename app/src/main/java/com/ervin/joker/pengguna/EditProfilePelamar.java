package com.ervin.joker.pengguna;

import android.app.Activity;
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
import com.ervin.joker.MainActivity;
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

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by ervin on 6/8/2017.
 */

public class EditProfilePelamar extends AppCompatActivity {
    ImageView ivPhoto_profile;
    Button btnEdit;
    EditText etEmail,etNama,etPassword;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private Uri rawPathImage;
    String jenis_pengguna = "Pelamar_pekerjaan";
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    boolean gambar = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_profile_pelamar);

        ivPhoto_profile = (ImageView) findViewById(R.id.iv_edit_profile_pelamar_gambar);
        btnEdit = (Button) findViewById(R.id.btn_edit_profile_pelamar_pelamar);
        etEmail = (EditText) findViewById(R.id.edt_edit_profile_pelamar_email);
        etNama = (EditText) findViewById(R.id.edt_edit_profile_pelamar_nama);
        etPassword = (EditText) findViewById(R.id.edt_edit_profile_pelamar_password);
        progressDialog = new ProgressDialog(this);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        myRef.child("User").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User pelamar = dataSnapshot.getValue(User.class);
                String sgetNama =pelamar.getNama();
                String sgetGambar =pelamar.getPhoto_profil();
               etNama.setText(sgetNama);
                etEmail.setText(user.getEmail());
                if(!sgetGambar.isEmpty()){
                    rawPathImage= Uri.parse(sgetGambar);
                    gambar=true;
                }
                Glide.with(EditProfilePelamar.this).load(sgetGambar).placeholder(R.drawable.user).dontAnimate().into(ivPhoto_profile);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        ivPhoto_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfilePelamar.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                String email = etEmail.getText().toString();
                String Password = etPassword.getText().toString();
                String nama = etNama.getText().toString();
                if(email.isEmpty()||Password.isEmpty()||nama.isEmpty()||gambar==false) {
                    Toast.makeText(EditProfilePelamar.this, "Isikan semua field yang ada",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    user.updateEmail(etEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("aaa", "User email address updated.");
                                        myRef.child("berkas").orderByChild("id_pelamar").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                // This method is called once with the initial value and again
                                                // whenever data at this location is updated.
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    snapshot.getRef().child("email_pelamar").setValue(user.getEmail());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                // Failed to read value

                                            }
                                        });
                                    }
                                }
                            });

                    user.updatePassword(etPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("aaa", "User password updated.");
                                    }
                                }
                            });
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            StorageReference mStorageRef;
                            mStorageRef = FirebaseStorage.getInstance().getReference();
                            Uri file = rawPathImage;
                            StorageReference riversRef = mStorageRef.child("images/" + user.getUid() + "/" + "photo_profile.jpg");

                            riversRef.putFile(file)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Get a URL to the uploaded content
                                            @SuppressWarnings("VisibleForTests")
                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                            User pengguna = new User(etNama.getText().toString(), String.valueOf(downloadUrl), jenis_pengguna);
                                            myRef.child("User").child(user.getUid()).setValue(pengguna);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                            // ...
                                        }
                                    });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    progressDialog.dismiss();
                    Intent intent = new Intent(EditProfilePelamar.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < images.size(); i++) {
                Uri uri = Uri.fromFile(new File(images.get(i).path));
                // start play with image uri
                rawPathImage= uri;
                gambar=true;
            }
            Glide.with(EditProfilePelamar.this).load(rawPathImage).dontAnimate().into(ivPhoto_profile);
        }
    }
}
