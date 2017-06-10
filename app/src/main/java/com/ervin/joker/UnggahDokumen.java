package com.ervin.joker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;

/**
 * Created by ervin on 6/11/2017.
 */

public class UnggahDokumen extends AppCompatActivity {
    FirebaseListAdapter<Dokumen> mAdapter;
    private static final String TAG = "UnggahDokumen";
    private FirebaseAuth mAuth;
    String filePath;
    private static final int GET_DOKUMEN = 8;
    FirebaseUser user;
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kirim_dokumen);
        ListView lv = (ListView) findViewById(R.id.lv_kirim_dokumen);
        Button pickup = (Button) findViewById(R.id.pick_button_dokumen);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        myRef = database.getReference().child("Dokumen");
        Log.d(TAG, "populateView: Anjaaaayyyyyyyyyyyyyyy");
//        mAdapter = new FirebaseListAdapter<Dokumen>(this, Dokumen.class,android.R.layout.simple_list_item_1,myRef) {
//            @Override
//            protected void populateView(View v, Dokumen model, int position) {
//
//            }
//        };
        mAdapter = new FirebaseListAdapter<Dokumen>(UnggahDokumen.this, Dokumen.class, android.R.layout.simple_list_item_1, myRef.child(userID)) {

            @Override
            protected void populateView(View v, Dokumen model, int position) {
                ((TextView)v.findViewById(android.R.id.text1)).setText(model.getNama_dokumen());
                Log.d(TAG, "Value dari child ini: " + model.getNama_dokumen());
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Dokumen itemRef = mAdapter.getItem(position);
                String link_dokumen = itemRef.getLink_dokumen();
                Toast.makeText(getApplicationContext(), "List Item Value: "+link_dokumen, Toast.LENGTH_LONG).show();
            }
        });
        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(UnggahDokumen.this)
                        .withRequestCode(GET_DOKUMEN)
                        .start();
//                Intent intent = ne
            }
        });


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_DOKUMEN:
                if (resultCode == RESULT_OK && data != null) {
                    filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    String Path = filePath;
                    final String filename = Path.substring(Path.lastIndexOf("/") + 1);
                   // etDokumen.setText(filename);
                    Uri file = Uri.fromFile(new File(filePath));
                    StorageReference riversRef = mStorageRef.child("dokumen/"+user.getUid()+"/"+ filename);

                    riversRef.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    @SuppressWarnings("VisibleForTests")
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    String link_dokumen = String.valueOf(downloadUrl);
                                    Dokumen pdf = new Dokumen(link_dokumen,filename,user.getUid());
                                    myRef.child(user.getUid()).push().setValue(pdf);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                }
                            });
                }
                break;
        }
    }
}
