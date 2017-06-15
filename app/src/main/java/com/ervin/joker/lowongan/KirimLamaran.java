package com.ervin.joker.lowongan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ervin.joker.R;
import com.ervin.joker.dokumen.UnggahDokumen;
import com.ervin.joker.berkas.BerkasLamaran;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    String link;
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
                startActivityForResult(intetn, GET_DOKUMEN);
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
                final String link_video = etVideo.getText().toString();
                final String id_lowongan = getIntent().getStringExtra("lowongan_id");
                final boolean tanda = false;
                final String lowonganId_false = id_lowongan+"_"+tanda;
                BerkasLamaran berkas = new BerkasLamaran(link_video,aa,id_lowongan,link,userID,tanda,lowonganId_false);
                myRef.child("berkas").child(id_lowongan+"_"+userID).setValue(berkas);
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GET_DOKUMEN:
                if(resultCode == RESULT_OK && data != null) {
                    link = data.getStringExtra("link");
                    String nama = data.getStringExtra("nama");
                    etDokumen.setText(nama);
                } break;
            case GET_VIDEO:
                if(resultCode == RESULT_OK && data != null){
                    String youtubeId = data.getStringExtra("ancok");
                    etVideo.setText(youtubeId);
                } break;
        }

    }

}
