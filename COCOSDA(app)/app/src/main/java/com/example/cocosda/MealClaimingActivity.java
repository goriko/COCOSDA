package com.example.cocosda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class MealClaimingActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonLogout, buttonScan, snak1Btn, lunchBtn, snak2Btn;
    private TextView textViewName;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    private IntentIntegrator qrScan;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_claiming);

        progressDialog = new ProgressDialog(this);

        progressDialog.dismiss();
//        progressDialog.setMessage("Logging in....");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        lunchBtn = findViewById(R.id.btnLunch);
        snak2Btn = findViewById(R.id.btnSnak2);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        snak1Btn= (Button) findViewById(R.id.btnSnak1);
        textViewName = (TextView) findViewById(R.id.textViewName);

        qrScan = new IntentIntegrator(this);



        firebaseAuth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String temp;
//                temp = dataSnapshot.child("title").getValue().toString()+" "+
//                        dataSnapshot.child("fName").getValue().toString()+" "+
//                        dataSnapshot.child("mName").getValue().toString()+" "+
//                        dataSnapshot.child("lName").getValue().toString();
//
//                if(dataSnapshot.child("suffix").exists()){
//                    temp = temp+" "+dataSnapshot.child("suffix").getValue().toString();
//                }
//                if(dataSnapshot.child("extension").exists()){
//                    temp = temp+", "+dataSnapshot.child("extension").getValue().toString();
//                }
//                textViewName.setText(temp);
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(MealClaimingActivity.this, MainActivity.class));
            }
        });




        buttonLogout.setOnClickListener(this);
        snak1Btn.setOnClickListener(this);
        lunchBtn.setOnClickListener(this);
        snak2Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogout){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else if(v == snak1Btn){
            temp = "morning_snack";
//            Toast.makeText(this, temp, Toast.LENGTH_LONG).show();
            qrScan.initiateScan();
        }else if(v == lunchBtn){
            temp = "lunch_meal";
            qrScan.initiateScan();
        }else if(v == snak2Btn){
            temp = "afternoon_snack";
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
                    //setting values to textviewc1
                    Log.d("EYY", obj.toString());
                    textViewName.setText(obj.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast

                    //filter where to insert
//                    if(temp.equals("morning_snack")){
//                        Toast.makeText(MealClaimingActivity.this,temp, Toast.LENGTH_LONG).show();
//                    }else if(temp.equals("lunch_meal")){
//                        Toast.makeText(MealClaimingActivity.this,temp, Toast.LENGTH_LONG).show();
//                    }else if(temp.equals("afternoon_snack")){
//                        Toast.makeText(MealClaimingActivity.this,temp, Toast.LENGTH_LONG).show();
//                    }

                    //extracting id number only

                    final String ID = result.getContents().substring(result.getContents().lastIndexOf("=") + 1);
                    databaseReference = FirebaseDatabase.getInstance().getReference("Meal").child(temp);
                    databaseReference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                String cmp;
                                Date currTime = Calendar.getInstance().getTime();
//                                cmp = "10/" + currTime.getDate() + "/2019";
                                databaseReference.child(ID).setValue(currTime.toString());
                            }else{
                                Toast.makeText(MealClaimingActivity.this, "already exist", Toast.LENGTH_LONG).show();
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
