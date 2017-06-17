package com.ervin.joker.pengguna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ervin.joker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by ervin on 6/11/2017.
 */

public class Reauntentifikasi extends AppCompatActivity {
    EditText email,password;
    Button autentifikasi;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reauntentifikasi);
        email = (EditText) findViewById(R.id.et_reauntentifikasi_email);
        password = (EditText) findViewById(R.id.et_reauntentifikasi_password);
        autentifikasi = (Button) findViewById(R.id.btn_reauntentifikasi_autentifikasi);
        progressDialog = new ProgressDialog(Reauntentifikasi.this);
        autentifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                if(!mail.isEmpty()&&!pass.isEmpty()){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email.getText().toString(), password.getText().toString());
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        String jenis = getIntent().getStringExtra("jenis");
                                        if (jenis.equals("Pelamar_pekerjaan")) {
                                            Intent intent = new Intent(Reauntentifikasi.this, EditProfilePelamar.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(Reauntentifikasi.this, EditProfilePersonalia.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Reauntentifikasi.this,"Email/Sandi yang dimasukkan salah", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Reauntentifikasi.this,"Silahkan isikan email/sandi anda", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }
}
