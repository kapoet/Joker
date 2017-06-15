package com.ervin.joker.berkas;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.ervin.joker.R;
import com.ervin.joker.dokumen.Dokumen;
import com.ervin.joker.pengguna.SignUpAsPelamar;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tonyodev.fetch.Fetch;
import com.tonyodev.fetch.listener.FetchListener;
import com.tonyodev.fetch.request.Request;


import java.io.File;


/**
 * Created by ervin on 6/6/2017.
 */

public class DetailBerkas extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    ImageView ivGambar;
    TextView tvNama, tvEmail, tvDokumen;
    Button btnKirimEmail;
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerFragment youTubeView;
    ToggleButton toggleButton;
    boolean standar;
    String name;
    DownloadManager mManager;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail_berkas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        String nama = getIntent().getStringExtra("nama_perusahaan");
        String gambar = getIntent().getStringExtra("gambar_perusahaan");
        String video = getIntent().getStringExtra("link_video");
        final String dokumen = getIntent().getStringExtra("link_dokumen");
        String email = getIntent().getStringExtra("email_pelamar");
        final boolean tanda = getIntent().getBooleanExtra("tanda",standar);
        final String berkasID = getIntent().getStringExtra("berkasID");
        final String lowongan_id = getIntent().getStringExtra("lowonganID");
        String pelamarID = getIntent().getStringExtra("pelamarID");
        progressDialog = new ProgressDialog(DetailBerkas.this);
        btnKirimEmail = (Button) findViewById(R.id.btn_detail_berkas_kirim_email);
        youTubeView = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.yv_detail_berkas_youtube_view);
        youTubeView.initialize(this.getResources().getString(R.string.google_api_key), this);
        ivGambar = (ImageView) findViewById(R.id.iv_detail_berkas_gambar_pelamar);
        tvDokumen = (TextView) findViewById(R.id.tv_detail_berkas_dokumen);
        tvEmail = (TextView) findViewById(R.id.tv_detail_berkas_email_pelamar);
        tvNama = (TextView) findViewById(R.id.tv_detail_berkas_nama_pelamar);
        Glide.with(this).load(gambar).placeholder(R.drawable.ic_menu_gallery).dontAnimate().centerCrop().into(ivGambar);
        tvNama.setText(nama);
        tvEmail.setText(email);
        toggleButton = (ToggleButton) findViewById(R.id.myToggleButton);
        toggleButton.setChecked(tanda);
        myRef.child("Dokumen").child(pelamarID).orderByChild("link_dokumen").equalTo(dokumen).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                    Dokumen pelamar = childSnapshot.getValue(Dokumen.class);
                    String nama_file = pelamar.getNama_dokumen();
                    Log.d("aaa", "Value nama file: " + nama_file);
                    name = nama_file;

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


        if(tanda==false){
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star));
        } else {
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_filled));
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star_filled));
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            myRef.child("berkas").child(berkasID).child("tanda").setValue(true);
                            myRef.child("berkas").child(berkasID).child("lowonganID_tanda").setValue(lowongan_id+"_"+true);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value

                        }
                    });
                } else {
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.star));
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            myRef.child("berkas").child(berkasID).child("tanda").setValue(false);
                            myRef.child("berkas").child(berkasID).child("lowonganID_tanda").setValue(lowongan_id+"_"+false);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value

                        }
                    });
                }
            }
        });

        tvDokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(dokumen), "application/pdf");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Intent newIntent = Intent.createChooser(intent, "Open File");
//                try {
//                    startActivity(newIntent);
//                } catch (ActivityNotFoundException e) {
//                    // Instruct the user to install a PDF reader here, or something
//                }
//
//                Fetch fetch = Fetch.getInstance(DetailBerkas.this);
//                String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
//                Request request = new Request(dokumen,path,"dokumencval.pdf");
//                final long downloadId = fetch.enqueue(request);
//
//                fetch.addFetchListener(new FetchListener() {
//
//                    @Override
//                    public void onUpdate(long id, int status, int progress, long downloadedBytes, long fileSize, int error) {
//                        Log.d("sss", "createUserWithEmail:onComplete:" + progress);
//                        if(progress==100){
//                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                                    +"dokumencval.pdf");//
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Intent newIntent = Intent.createChooser(intent, "Open File");
//                try {
//                    startActivity(newIntent);
//                } catch (ActivityNotFoundException e) {
//                    // Instruct the user to install a PDF reader here, or something
//                }
//                        }
//                        if(downloadId == id && status == Fetch.STATUS_DOWNLOADING) {
//                            Log.d("sss", "createUserWithEmail:onComplete:" + progress);
//                            if(progress==100){
//                                Toast.makeText(DetailBerkas.this, "Isikan semua field yang ada",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//
//                        }else if(error != Fetch.NO_ERROR) {
//                            //An error occurred
//                            Log.d("sss", "createUserWithEmail:onComplete:" + progress);
//                            if(error == Fetch.ERROR_HTTP_NOT_FOUND) {
//                                //handle error
//                                Log.d("sss", "createUserWithEmail:onComplete:" + progress);
//                            }
//
//                        }
//                    }
//                });
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(dokumen));
//                startActivity(i);

                down(dokumen,name);
            }
        });

        btnKirimEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String email = tvEmail.getText().toString();
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Pilih aplikasi kirim email"));
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(getIntent().getStringExtra("link_video")); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
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

    public void down(String dokumen, String namaFile) {
        Toast.makeText(this, "Mengunduh dimulai...", Toast.LENGTH_LONG).show();
        mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(dokumen);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri)
                .setTitle("Mengunduh")
                .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, namaFile)
                .setDescription("Unduhan dalam proses").setMimeType("pdf");
        long a = mManager.enqueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(broadcast, intentFilter);
    }
    public void showPdf() {

        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/Download/" + name );//name here is the name of any string you want to pass to the method
            if (!file.isDirectory())
                file.mkdir();
            Intent testIntent = new Intent("com.adobe.reader");
            testIntent.setType("application/pdf");
            testIntent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            testIntent.setDataAndType(uri, "application/pdf");
            startActivity(testIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showPdf();
        }
    };
}
