package com.ervin.joker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ervin.joker.pengguna.SignUpAsPelamar;
import com.ervin.joker.pengguna.SignUpAsPersonalia;

/**
 * Created by ervin on 5/30/2017.
 */

public class SignUp extends AppCompatActivity {
    Button signUpAsPelamar, signUpAsPersonalia;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sign_up);
        signUpAsPelamar = (Button) findViewById(R.id.btn_sign_up_pelamar_pekerjaan);
        signUpAsPersonalia = (Button) findViewById(R.id.btn_sign_up_personalia);

        signUpAsPelamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,SignUpAsPelamar.class);
                startActivity(intent);
            }
        });

        signUpAsPersonalia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignUpAsPersonalia.class);
                startActivity(intent);
            }
        });
    }
}
