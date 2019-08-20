package com.example.cocosda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogout;
    private TextView textViewName;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Logging in....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textViewName = (TextView) findViewById(R.id.textViewName);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temp;
                temp = dataSnapshot.child("title").getValue().toString()+" "+
                        dataSnapshot.child("fName").getValue().toString()+" "+
                        dataSnapshot.child("mName").getValue().toString()+" "+
                        dataSnapshot.child("lName").getValue().toString();

                if(dataSnapshot.child("suffix").exists()){
                    temp = temp+" "+dataSnapshot.child("suffix").getValue().toString();
                }
                if(dataSnapshot.child("extension").exists()){
                    temp = temp+", "+dataSnapshot.child("extension").getValue().toString();
                }
                textViewName.setText(temp);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogout){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
