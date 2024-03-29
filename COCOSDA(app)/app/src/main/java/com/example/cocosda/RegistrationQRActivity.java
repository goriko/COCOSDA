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

public class RegistrationQRActivity extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private IntentIntegrator qrScan;

    private Button buttonLogout, buttonScan;
    private TextView textViewName, statusName;
    private StatusClass statusClass;
    String tsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_qr);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.dismiss();

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        statusName = findViewById(R.id.statusName);

        qrScan = new IntentIntegrator(this);


        buttonLogout.setOnClickListener(this);
        buttonScan.setOnClickListener(this);

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
            startActivity(new Intent(this, RegistrationActivity.class));
            finish();
        }else if(v == buttonScan){
            qrScan.initiateScan();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textview
                    Log.d("EYY", obj.toString());
                    textViewName.setText(obj.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast

                    //extracting id number only
                    Date currTime = Calendar.getInstance().getTime();
                    String xtr = ""+currTime.getDate();
                    final String ID = result.getContents().substring(result.getContents().lastIndexOf("=") + 1);
                    databaseReference = FirebaseDatabase.getInstance().getReference("Registered_User").child(xtr).child(firebaseAuth.getUid().toString());
                    databaseReference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Date currTime = Calendar.getInstance().getTime();
                            if(!dataSnapshot.exists()){
                                currTime = Calendar.getInstance().getTime();
                                statusClass = new StatusClass(ID, currTime.toString(), "0");
                                databaseReference.child(ID).setValue(statusClass);
                                statusName.setText("Registered!");
                                qrScan.initiateScan();
                            }else{
                                statusClass = new StatusClass(dataSnapshot.child("user_ID").getValue().toString(), dataSnapshot.child("time").getValue().toString(), dataSnapshot.child("status").getValue().toString() + "1");
                                databaseReference.child(ID).setValue(statusClass);
                                statusName.setText("User Already Registered!");
                                qrScan.initiateScan();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

