package com.ervin.joker.lowongan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.ervin.joker.berkas.FavoritList;
import com.ervin.joker.R;
import com.ervin.joker.berkas.BerkasLamaranList;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailLowongan extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    private static final int KIRIM_LAMARAN = 12;
    private YouTubePlayerFragment youTubeView;
    private FirebaseAuth mAuth;
    TextView tv_posisi_lowongan, tv_nama_perusahaan, tv_tanggal_terbit, tv_batas_pengiriman, tv_tentang_perusahaan
            ,tv_deskripsi,tv_deskripsi_pekerjaan;
    Button btn_kirim_lamaran,btn_lihat_berkas, btnFavorit;
    String tentang_perusahaan = "gambar_perusahaan",
            tanggal_terbit ="tanggal_terbit",
            batas_pengiriman ="batas_kiriman",
            nama_perusahaan ="nama_perusahaan",
            posisi_lowongan ="posisi_lowong",
            deskripsi_lowongan ="deskripsi_lowongan",
            link_video ="link_video",
            id_lowongan ="lowongan_id" ;
    String video;
    String lwonganID;
    boolean sudah = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail_lowongan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String jenis_pengguna=getIntent().getStringExtra("jenis_pengguna");
        Log.d("ini adalah", "hasilnya pengguna" + jenis_pengguna);
        video = getIntent().getStringExtra(link_video);
        if(jenis_pengguna.equals("Personalia")){
            findViewById(R.id.btn_detail_lowongan_kirim_lamaran).setVisibility(View.INVISIBLE);
            findViewById(R.id.berkas).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.btn_detail_lowongan_kirim_lamaran).setVisibility(View.VISIBLE);
            findViewById(R.id.berkas).setVisibility(View.INVISIBLE);
        }
        youTubeView = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.yv_detail_lowongan_youtube_view);
        youTubeView.initialize(this.getResources().getString(R.string.google_api_key), this);
        tv_posisi_lowongan = (TextView) findViewById(R.id.tv_detail_lowongan_posisi_lowongan);
        tv_nama_perusahaan = (TextView) findViewById(R.id.tv_detail_lowongan_nama_perusahaan);
        tv_tanggal_terbit = (TextView) findViewById(R.id.tv_detail_lowongan_tanggal_terbit);
        tv_batas_pengiriman = (TextView) findViewById(R.id.tv_detail_lowongan_batas_pengiriman);
        tv_deskripsi = (TextView) findViewById(R.id.tv_detail_lowongan_about_company);
        tv_deskripsi_pekerjaan = (TextView) findViewById(R.id.tv_detail_lowongan_deskripsi_lowongan);
        btn_kirim_lamaran = (Button) findViewById(R.id.btn_detail_lowongan_kirim_lamaran);
        btn_lihat_berkas = (Button) findViewById(R.id.btn_detail_lowongan_lihat_berkas);
        btnFavorit = (Button) findViewById(R.id.btn_detail_lowongan_favorit);
        tv_posisi_lowongan.setText(getIntent().getStringExtra(posisi_lowongan));
        tv_nama_perusahaan.setText(getIntent().getStringExtra(nama_perusahaan));
        tv_tanggal_terbit.setText(":" +getIntent().getStringExtra(tanggal_terbit));
        tv_batas_pengiriman.setText(":" +getIntent().getStringExtra(batas_pengiriman));
        tv_deskripsi.setText(getIntent().getStringExtra(tentang_perusahaan));
        tv_deskripsi_pekerjaan.setText(getIntent().getStringExtra(deskripsi_lowongan));
        final String lowongan_ID = getIntent().getStringExtra(id_lowongan);
        lwonganID=lowongan_ID;
        Log.d("ini adalah", "hasilnya" + lowongan_ID);
        if(jenis_pengguna=="Personalia"){
            btn_lihat_berkas.setVisibility(View.VISIBLE);
        }
        btn_kirim_lamaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();
                Log.d("afafaf", "tanggal hari ini: " + lowongan_ID+"_"+user.getUid());
                myRef.child("berkas").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        if (dataSnapshot.hasChild(lowongan_ID + "_" + user.getUid())) {
                            sudah=true;
                            AlertDialog diaBox = AskOption();
                            diaBox.show();
                        } else {
                            Intent intent = new Intent(DetailLowongan.this, KirimLamaran.class);
                            intent.putExtra(id_lowongan,lowongan_ID);
                            startActivityForResult(intent,KIRIM_LAMARAN);
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

            }
        });
        btn_lihat_berkas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailLowongan.this, BerkasLamaranList.class);
                intent.putExtra(id_lowongan,lowongan_ID);
                startActivity(intent);
            }
        });
        btnFavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailLowongan.this, FavoritList.class);
                intent.putExtra(id_lowongan,lowongan_ID);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(video); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
//            String error = String.format(getString(R.string.player_error), errorReason.toString());
//            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RECOVERY_REQUEST:
                getYouTubePlayerProvider().initialize(this.getResources().getString(R.string.google_api_key), this); break;
            case KIRIM_LAMARAN:
                if(resultCode==RESULT_OK){
                    Toast.makeText(DetailLowongan.this, "Berkas Lamaran Berhasil Terkirim",
                            Toast.LENGTH_SHORT).show();
                }

        }


    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(DetailLowongan.this)
                //set message, title, and icon
                .setTitle("Beras sudah terkirim")
                .setMessage("Apakah anda ingin mengirim lowongan lagi?")
                //.setIcon(R.drawable.delete)

                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(DetailLowongan.this, KirimLamaran.class);
                        intent.putExtra(id_lowongan,lwonganID);
                        startActivityForResult(intent,KIRIM_LAMARAN);
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}