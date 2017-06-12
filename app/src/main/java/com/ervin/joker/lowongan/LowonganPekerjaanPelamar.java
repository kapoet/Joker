package com.ervin.joker.lowongan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ervin.joker.R;
import com.ervin.joker.pengguna.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ervin on 6/2/2017.
 */

public class LowonganPekerjaanPelamar extends Fragment {
    private static final String TAG = "LowonganPekerjaanPelama";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    DatabaseReference databaseUser;
    RecyclerView rv;
    FirebaseRecyclerAdapter<LowonganPekerjaan, LowonganPekerjaanAdapter> mFirebaseAdapter;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    String getNama, getGambar, link_video,posisiLowong,deskripsi;
    long batasPengiriman , tanggalTerbit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View vw = inflater.inflate(R.layout.layout_list_pekerjaan,container,false);
        mAuth = FirebaseAuth.getInstance();
        rv = (RecyclerView) vw.findViewById(R.id.recycleViewGamabar);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        myRef = database.getReference().child("Lowongan_pekerjaan");
        databaseUser = database.getReference();
        Calendar date = Calendar.getInstance();
        long hariIni = date.getTimeInMillis();
        myRef.keepSynced(true);
        databaseUser.keepSynced(true);
        Log.d(TAG, "tanggal hari ini: " + hariIni);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<LowonganPekerjaan, LowonganPekerjaanAdapter>(LowonganPekerjaan.class, R.layout.layout_item_pekerjaan, LowonganPekerjaanAdapter.class,myRef.orderByChild("batas_pengiriman").startAt(hariIni)) {
            @Override
            protected void populateViewHolder(final LowonganPekerjaanAdapter viewHolder, final LowonganPekerjaan model, final int position) {
                long available = model.getTanggal_terbit();
                long expired = model.getBatas_pengiriman();
                Calendar date = Calendar.getInstance();
                long millisecondsDate = date.getTimeInMillis();
                int day= (int) TimeUnit.MILLISECONDS.toDays(millisecondsDate-available);
                viewHolder.setBatasPengiriman(day);
                viewHolder.setLowongan(model.getPosisi_lowong());
                viewHolder.setDeskripsiPerusahaan(model.getDeskripsi());
                // databaseUser.child(model.getPembuat_lowongan());
                databaseUser.child("User").child(model.getPembuat_lowongan()).addListenerForSingleValueEvent(new ValueEventListener() {
                    // myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        // for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) { //buat dapat key node
                        //     String aa = childSnapshot.getKey(); // buat dapat key kode
                        // ProfilPengguna person = childSnapshot.getValue(ProfilPengguna.class);
                        // String value = dataSnapshot.getValue(String.class);
                        User personalia = dataSnapshot.getValue(User.class);
                        String sgetNama =personalia.getNama();
                        String sgetGambar =personalia.getPhoto_profil();
                        viewHolder.setNamaPerusahaan(sgetNama);
                        viewHolder.setLink_gambar(sgetGambar);
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
                                posisiLowong =dataSnapshot.child("Lowongan_pekerjaan").child(abu).getValue(LowonganPekerjaan.class).getPosisi_lowong();
                                deskripsi =dataSnapshot.child("Lowongan_pekerjaan").child(abu).getValue(LowonganPekerjaan.class).getDeskripsi();
                                tanggalTerbit =dataSnapshot.child("Lowongan_pekerjaan").child(abu).getValue(LowonganPekerjaan.class).getTanggal_terbit();
                                batasPengiriman =dataSnapshot.child("Lowongan_pekerjaan").child(abu).getValue(LowonganPekerjaan.class).getBatas_pengiriman();
                                String pembuatLowongan =dataSnapshot.child("Lowongan_pekerjaan").child(abu).getValue(LowonganPekerjaan.class).getPembuat_lowongan();
                                //Log.d(TAG, "Value is: " + aa);
                                getNama =dataSnapshot.child("User").child(pembuatLowongan).getValue(User.class).getNama();
                                getGambar =dataSnapshot.child("User").child(pembuatLowongan).getValue(User.class).getDeskripsi();
                                link_video = dataSnapshot.child("User").child(pembuatLowongan).getValue(User.class).getVideo_id();
                                String jenis_pengguna = dataSnapshot.child("User").child(userID).getValue(User.class).getJenis_pengguna();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String rawTanggalTerbit = formatter.format(new Date(tanggalTerbit));
                                String rawBatasKiriman = formatter.format(new Date(batasPengiriman));
                                Intent intent = new Intent(getActivity(), DetailLowongan.class);
                                Log.d(TAG, "Value is: " + rawBatasKiriman);
                                intent.putExtra("tanggal_terbit",rawTanggalTerbit);
                                intent.putExtra("batas_kiriman", rawBatasKiriman);
                                intent.putExtra("deskripsi_lowongan", deskripsi);
                                intent.putExtra("posisi_lowong",posisiLowong);
                                intent.putExtra("nama_perusahaan",getNama);
                                intent.putExtra("gambar_perusahaan",getGambar);
                                intent.putExtra("link_video",link_video);
                                intent.putExtra("lowongan_id",abu);
                                intent.putExtra("jenis_pengguna",jenis_pengguna);
                                Log.d("aaaa", "setLink_gambar: "+jenis_pengguna);
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

        return vw;
    }

    public void onStart() {
        super.onStart();

    }



}
