package com.ervin.joker.pengguna;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;



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
                Glide.with(EditProfilePelamar.this).load(sgetGambar).placeholder(R.drawable.ic_menu_gallery).dontAnimate().into(ivPhoto_profile);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        ivPhoto_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(EditProfilePelamar.this)
                        .folderMode(false) // folder mode (false by default)
                        .single() // single mode
                        .limit(1) // max images can be selected (999 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .start(5);
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
                    if(isValidEmail(email)&&Password.length()>7){
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
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfilePelamar.this, "Pastikan email dan password anda benar",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            Uri uri = Uri.fromFile(new File(images.get(0).getPath()));
            rawPathImage= uri;
            Glide.with(EditProfilePelamar.this).load(rawPathImage).dontAnimate().into(ivPhoto_profile);
            gambar=true;
        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
