package com.example.cocosda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private IntentIntegrator qrScan;

    private Button buttonLogout, certClaim, registerUser, btnSess;
    private TextView textViewName, statusName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog = new ProgressDialog(this);
        progressDialog.dismiss();

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textViewName = (TextView) findViewById(R.id.textViewName);
        certClaim = findViewById(R.id.btnCertClaim);
        statusName = findViewById(R.id.statusName);
        registerUser = findViewById(R.id.btnRegister);
        btnSess = findViewById(R.id.btnSess);

        qrScan = new IntentIntegrator(this);


        buttonLogout.setOnClickListener(this);
        certClaim.setOnClickListener(this);
        registerUser.setOnClickListener(this);
        btnSess.setOnClickListener(this);
//        progressDialog = new ProgressDialog(this);
//
//        progressDialog.setMessage("Fetching Data.....");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        Date currTime = Calendar.getInstance().getTime();
        String currDay = ""+currTime.getDate();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Sessions").child(currDay).child("Register_Station").child(firebaseAuth.getUid().toString());
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child("timeStart").exists() && !dataSnapshot.child("timeEnd").exists()){
//                    startActivity(new Intent(RegistrationActivity.this, SessionActivity.class));
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogout){
            firebaseAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else if(v == certClaim){
            startActivity(new Intent(this, CertificateActivity.class));
            finish();
        }else if(v == registerUser){
            startActivity(new Intent(this, RegistrationQRActivity.class));
            finish();
        }else if(v == btnSess){
            startActivity(new Intent(this, SessionActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

