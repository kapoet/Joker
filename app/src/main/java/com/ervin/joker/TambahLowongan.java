package com.ervin.joker;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ervin on 6/8/2017.
 */

public class TambahLowongan extends Fragment {
    Button btnPilihTanggal, btnTambahLowongan;
    EditText etPosisiLowong, etTanggal, etDeskripsi;
    private int mYear, mMonth, mDay;
    Date date ;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.layout_tambah_lowongan,container,false);
        mAuth = FirebaseAuth.getInstance();
        btnPilihTanggal = (Button) vw.findViewById(R.id.btn_select_date);
        btnTambahLowongan = (Button) vw.findViewById(R.id.btn_tambah_lowongan_tambah);
        etDeskripsi = (EditText) vw.findViewById(R.id.et_tambah_lowongan_deskripsi);
        etPosisiLowong = (EditText) vw.findViewById(R.id.et_tambah_lowongan_posisi_lowong);
        etTanggal = (EditText) vw.findViewById(R.id.et_tambah_lowongan_date_picker);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        btnPilihTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                etTanggal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTambahLowongan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tanggal = etTanggal.getText().toString();
                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String gmtTime = formatter.format(todayDate);
                String jambaru = tanggal+" "+gmtTime;
                SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

                try {
                    date = formatter2.parse(jambaru);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar now = Calendar.getInstance();
                long timeMilisSekarang = now.getTimeInMillis();
                Long timeInMillis = date.getTime();
                String deskripsi = etDeskripsi.getText().toString();
                String posisi = etPosisiLowong.getText().toString();
                FirebaseUser user = mAuth.getCurrentUser();
                LowonganPekerjaan lowongan = new LowonganPekerjaan(posisi,user.getUid(),deskripsi,timeInMillis,timeMilisSekarang);
                myRef.child("Lowongan_pekerjaan").push().setValue(lowongan);

            }
        });

        return vw;
    }
}
