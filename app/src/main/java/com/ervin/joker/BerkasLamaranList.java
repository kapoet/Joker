package com.ervin.joker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ervin on 6/6/2017.
 */

public class BerkasLamaranList extends AppCompatActivity {
    private static final String TAG = "BerkasLamaranList";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    DatabaseReference databaseUser;
    RecyclerView rv;
    FirebaseRecyclerAdapter<BerkasLamaran, BerkasLamaranAdapter> mFirebaseAdapter;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    String getNama, getGambar, link_video,link_dokumen,email_pelamar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_berkas_lamaran);

            mAuth = FirebaseAuth.getInstance();
            rv = (RecyclerView) findViewById(R.id.recycler_view_berkas);
        final String id_lowongan = getIntent().getStringExtra("lowongan_id");
            rv.setLayoutManager(new LinearLayoutManager(BerkasLamaranList.this));
            FirebaseUser user = mAuth.getCurrentUser();
            final String userID = user.getUid();
            myRef = database.getReference().child("berkas");
            databaseUser = database.getReference();
            mFirebaseAdapter = new FirebaseRecyclerAdapter<BerkasLamaran, BerkasLamaranAdapter>(BerkasLamaran.class, R.layout.layout_item_berkas_lamaran, BerkasLamaranAdapter.class,myRef.orderByChild("lowongan_id").equalTo(id_lowongan)) {
                @Override
                protected void populateViewHolder(final BerkasLamaranAdapter viewHolder, final BerkasLamaran model, final int position) {
                    // databaseUser.child(model.getPembuat_lowongan());
                    //String pembuatBerkas = model.getEmail_pelamar();
                    Log.d(TAG, "Value haha: " + model.getId_pelamar());
                    databaseUser.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                        // myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            // for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) { //buat dapat key node
                            //     String aa = childSnapshot.getKey(); // buat dapat key kode
                            // ProfilPengguna person = childSnapshot.getValue(ProfilPengguna.class);
                            // String value = dataSnapshot.getValue(String.class);
                            Log.d(TAG, "Value haha5234: " + model.getId_pelamar());
                            User pelamar = dataSnapshot.child(model.getId_pelamar()).getValue(User.class);

                            String sgetNama =pelamar.getNama();
                            String sgetGambar =pelamar.getPhoto_profil();
                            String sgetEmail = model.getEmail_pelamar();
                            viewHolder.setNama(sgetNama);
                            viewHolder.setLink_gambar(sgetGambar);
                            viewHolder.setEmail(sgetEmail);
                            //Log.d(TAG, "Value is: " + aa);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String abu = mFirebaseAdapter.getRef(position).getKey();
                            Log.d(TAG, "Value is: " + abu);
                            databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                // myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    // for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) { //buat dapat key node
                                    //     String aa = childSnapshot.getKey(); // buat dapat key kode
                                    // ProfilPengguna person = childSnapshot.getValue(ProfilPengguna.class);
                                    // String value = dataSnapshot.getValue(String.class);

                                    //LowonganPekerjaan  = dataSnapshot.getValue(LowonganPekerjaan.class);

                                    String pembuatBerkas =dataSnapshot.child("berkas").child(abu).getValue(BerkasLamaran.class).getId_pelamar();
                                    //Log.d(TAG, "Value is: " + aa);
                                    getNama =dataSnapshot.child("User").child(pembuatBerkas).getValue(User.class).getNama();
                                    getGambar =dataSnapshot.child("User").child(pembuatBerkas).getValue(User.class).getPhoto_profil();
                                    email_pelamar = model.getEmail_pelamar();
                                    link_video = model.getLink_video();
                                    link_dokumen = model.getLink_dokumen();
                                    boolean tanda = model.isTanda();
                                    Intent intent = new Intent(BerkasLamaranList.this, DetailBerkas.class);
                                    intent.putExtra("nama_perusahaan",getNama);
                                    intent.putExtra("gambar_perusahaan",getGambar);
                                    intent.putExtra("link_video",link_video);
                                    intent.putExtra("link_dokumen",link_dokumen);
                                    intent.putExtra("email_pelamar",email_pelamar);
                                    intent.putExtra("tanda", tanda);
                                    intent.putExtra("berkasID",abu);
                                    intent.putExtra("lowonganID",id_lowongan);
                                    startActivity(intent);
                                }
                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w(TAG, "Failed to read value.", error.toException());
                                }
                            });

//                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//                        String rawTanggalTerbit = formatter.format(new Date(tanggalTerbit));
//                        String rawBatasKiriman = formatter.format(new Date(batasPengiriman));
//                        Intent intent = new Intent(getActivity(), DetailLowongan.class);
//                        Log.d(TAG, "Value is: " + rawBatasKiriman);
//                        intent.putExtra("tanggal_terbit",rawTanggalTerbit);
//                        intent.putExtra("batas_kiriman", rawBatasKiriman);
//                        intent.putExtra("deskripsi_lowongan", deskripsi);
//                        intent.putExtra("posisi_lowong",posisiLowong);
//                        intent.putExtra("nama_perusahaan",getNama);
//                        intent.putExtra("gambar_perusahaan",getGambar);
//                        intent.putExtra("link_video",link_video);
//                        startActivity(intent);

                        }
                    });
//                viewHolder.setNamaPerusahaan(getNama);
//                viewHolder.setLink_gambar(getGambar);

                }
            };

            rv.setAdapter(mFirebaseAdapter);
    }

}
