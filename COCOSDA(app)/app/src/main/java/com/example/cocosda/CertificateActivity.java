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

public class CertificateActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private IntentIntegrator qrScan;

    private Button buttonLogout, buttonScan;
    private TextView textViewName, statusName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.dismiss();
        qrScan = new IntentIntegrator(this);


        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        statusName = findViewById(R.id.statusName);

        buttonLogout.setOnClickListener(this);
        buttonScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogout){
            startActivity(new Intent(this, RegistrationActivity.class));
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
                    final String ID = result.getContents().substring(result.getContents().lastIndexOf("=") + 1);
                    databaseReference = FirebaseDatabase.getInstance().getReference("Certificate");
                    databaseReference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Date currTime = Calendar.getInstance().getTime();
                            if(!dataSnapshot.exists()){
                                currTime = Calendar.getInstance().getTime();
//                                Toast.makeText(RegistrationActivity.this,  "Time In", Toast.LENGTH_LONG).show();
                                databaseReference.child(ID).setValue(currTime.toString());
                                statusName.setText("Claiming Certificate");
                            }
                            statusName.setText("Certificate Already Claimed");
//                            else if(dataSnapshot.exists()){
//                                Toast.makeText(RegistrationActivity.this, "Time  Out", Toast.LENGTH_LONG).show();
//                                databaseReference = FirebaseDatabase.getInstance().getReference("TimeIn_TimeOut").child("Time_Out");
//                                databaseReference.child(ID).setValue(currTime.toString());
//                            }
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
