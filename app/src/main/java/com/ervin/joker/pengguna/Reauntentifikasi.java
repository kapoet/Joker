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

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email.getText().toString(), password.getText().toString());

// Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                String jenis = getIntent().getStringExtra("jenis");
                                if(jenis.equals("Pelamar_pekerjaan")){
                                    Intent intent = new Intent(Reauntentifikasi.this, EditProfilePelamar.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(Reauntentifikasi.this, EditProfilePersonalia.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });
            }
        });

    }
}
