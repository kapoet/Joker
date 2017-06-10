package com.ervin.joker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

/**
 * Created by ervin on 6/5/2017.
 */

public class KirimLamaran extends AppCompatActivity {
    private static final String TAG = "KirimLamaran";
    Button btnKirim;
    EditText etVideo, etDokumen;
    ImageView ivVideo,ivDokumen;
    String filePath;
    private static final int GET_VIDEO = 7;
    private static final int GET_DOKUMEN = 8;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kirim_berkas_lamaran);
        etDokumen = (EditText) findViewById(R.id.et_kirim_berkas_lamaran_dokumen);
        etVideo = (EditText) findViewById(R.id.et_kirim_berkas_lamaran_video);
        btnKirim = (Button) findViewById(R.id.btn_kirim_berkas_kirim);
        ivDokumen = (ImageView) findViewById(R.id.iv_kirim_berkas_lamaran_dokumen);
        ivVideo = (ImageView) findViewById(R.id.iv_kirim_berkas_lamaran_video);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        final String aa= user.getEmail();
        Log.d(TAG, "ini adalah user dengan emai "+aa);
        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KirimLamaran.this, com.ervin.joker.video.MainActivity.class);
                startActivityForResult(intent, GET_VIDEO);
            }
        });

        ivDokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intetn = new Intent(KirimLamaran.this, UnggahDokumen.class);
                startActivity(intetn);
//                new MaterialFilePicker()
//                        .withActivity(KirimLamaran.this)
//                        .withRequestCode(GET_DOKUMEN)
//                        .start();
//                Intent intent = ne
            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = etDokumen.getText().toString();
                Uri file = Uri.fromFile(new File(filePath));
                StorageReference riversRef = mStorageRef.child("dokumen/"+userID+"/"+ nama);

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String link_dokumen = String.valueOf(downloadUrl);
                                String email = aa;
                                String link_video = etVideo.getText().toString();
                                String id_lowongan = getIntent().getStringExtra("lowongan_id");
                                boolean tanda = false;
                                String lowonganId_false = id_lowongan+"_"+tanda;
                                BerkasLamaran berkas = new BerkasLamaran(link_video,aa,id_lowongan,link_dokumen,userID,tanda,lowonganId_false);
                                myRef.child("berkas").push().setValue(berkas);
                                finish();
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
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GET_DOKUMEN:
                if(resultCode == RESULT_OK && data != null) {
                    filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    String Path=filePath;
                    String filename=Path.substring(Path.lastIndexOf("/")+1);
                    etDokumen.setText(filename);
                } break;
            case GET_VIDEO:
                if(resultCode == RESULT_OK && data != null){
                    String youtubeId = data.getStringExtra("ancok");
                    etVideo.setText(youtubeId);
                } break;
        }

    }
}
