package com.ervin.joker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ervin on 5/30/2017.
 */

public class SignIn extends AppCompatActivity {
    private static final String TAG = "SignIn";
    EditText edtEmail, edtPassword;
    Button btnSignIn, btnSignUp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sign_in);
        edtEmail = (EditText) findViewById(R.id.edt_sign_in_email);
        edtPassword = (EditText) findViewById(R.id.edt_sign_in_password);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in_masuk);
        btnSignUp = (Button)findViewById(R.id.btn_sign_in_mendaftar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(SignIn.this,"onAuthStateChanged:signed_in:" + user.getUid(), Toast.LENGTH_SHORT).show();
                    myRef.child("User").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            User pengguna = dataSnapshot.getValue(User.class);
                            String jenis_Pengguna = pengguna.getJenis_pengguna();
                            if(jenis_Pengguna.equals("Personalia")){
                                Intent intent = new Intent(SignIn.this,MainActivityPersonalia.class);
                                startActivity(intent);
                            } else if (jenis_Pengguna.equals("Pelamar_pekerjaan")){
                                Intent intent = new Intent(SignIn.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                } else {
                    // User is signed out
                    Toast.makeText(SignIn.this,"Silahkan isi semua field yang ada", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password);

                if(email.isEmpty()||password.isEmpty()){
                    Toast.makeText(SignIn.this,"Silahkan isi semua field yang ada", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out "+email+" "+password);
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
