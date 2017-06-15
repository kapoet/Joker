package com.ervin.joker.berkas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ervin on 6/8/2017.
 */

public class KirimEmail extends AppCompatActivity {
    Button btnKirim;
    EditText etAlamat, etSubjek, etIsi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_kirim_email);
//        etAlamat = (EditText) findViewById(R.id.et_kirim_email_alamat);
//        etSubjek = (EditText) findViewById(R.id.et_kirim_email_subjek);
//        etIsi = (EditText) findViewById(R.id.et_kirim_email_body);
//        btnKirim = (Button) findViewById(R.id.btn_kirim_email_kirim);
//        String email = getIntent().getStringExtra("email");
//        etAlamat.setText(email);
//
//        btnKirim.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String subjek = etSubjek.getText().toString();
//                String isi = etIsi.getText().toString();
////                Intent intent = new Intent(Intent.ACTION_SEND);
////                intent.setType("text/plain");
////                intent.putExtra(Intent.EXTRA_EMAIL, etAlamat.getText().toString());
////                intent.putExtra(Intent.EXTRA_SUBJECT, subjek);
////                intent.putExtra(Intent.EXTRA_TEXT, isi);
////
////                startActivity(Intent.createChooser(intent, "Send Email"));
//                Intent email = new Intent(Intent.ACTION_SEND);
//                email.putExtra(Intent.EXTRA_EMAIL, new String[]{etAlamat.getText().toString()});
//                email.putExtra(Intent.EXTRA_SUBJECT, subjek);
//                email.putExtra(Intent.EXTRA_TEXT, isi);
//                email.setType("message/rfc822");
//                startActivity(Intent.createChooser(email, "Choose an Email client :"));
//                finish();
//            }
//        });
    }
}
