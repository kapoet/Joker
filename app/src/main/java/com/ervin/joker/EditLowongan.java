package com.ervin.joker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class EditLowongan extends AppCompatActivity {
    Button btnPilihTanggal, btnTambahLowongan;
    EditText etPosisiLowong, etTanggal, etDeskripsi;
    private int mYear, mMonth, mDay;
    Date date ;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_lowongan);
        mAuth = FirebaseAuth.getInstance();
        btnPilihTanggal = (Button) findViewById(R.id.btn_edit_lowongan_select_date);
        btnTambahLowongan = (Button) findViewById(R.id.btn_edit_lowongan_tambah);
        etDeskripsi = (EditText) findViewById(R.id.et_edit_lowongan_deskripsi);
        etPosisiLowong = (EditText) findViewById(R.id.et_edit_lowongan_posisi_lowong);
        etTanggal = (EditText) findViewById(R.id.et_edit_lowongan_date_picker);
        etPosisiLowong.setText(getIntent().getStringExtra("posisi_lowong"));
        etTanggal.setText(getIntent().getStringExtra("batas_kiriman"));
        etDeskripsi.setText(getIntent().getStringExtra("deskripsi_lowongan"));
        final String lowongan_ID = getIntent().getStringExtra("lowongan_id");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        btnPilihTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditLowongan.this,
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
                Log.d("bere", "Value is: " + jambaru);
                SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

                try {
                    date = formatter2.parse(jambaru);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar now = Calendar.getInstance();
                long timeMilisSekarang = now.getTimeInMillis();
                Long timeInMillis = date.getTime();
                Log.d("hahah", "Value is: " + timeInMillis);
                String deskripsi = etDeskripsi.getText().toString();
                String posisi = etPosisiLowong.getText().toString();
                FirebaseUser user = mAuth.getCurrentUser();
                LowonganPekerjaan lowongan = new LowonganPekerjaan(posisi,user.getUid(),deskripsi,timeInMillis,timeMilisSekarang);
                myRef.child("Lowongan_pekerjaan").child(lowongan_ID).setValue(lowongan);
            }
        });
    }


}
