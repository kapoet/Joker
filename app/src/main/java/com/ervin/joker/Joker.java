package com.ervin.joker;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ervin on 6/4/2017.
 */

public class Joker extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
