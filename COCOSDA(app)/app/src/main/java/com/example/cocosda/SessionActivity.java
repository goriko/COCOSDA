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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Queue;

public class SessionActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private IntentIntegrator qrScan;

    private Button btnEndSess, buttonScan, btnRoom1, btnRoom2;
    private TextView textViewName, room;
    public String tempStr;
    public ClassSession classSession;
    public  StatusClass statusClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        firebaseAuth =  FirebaseAuth.getInstance();
        qrScan = new IntentIntegrator(this);

        btnEndSess = findViewById(R.id.btnEnd);
        buttonScan = findViewById(R.id.buttonScan);
        btnRoom1 = findViewById(R.id.btnRoom1);
        btnRoom2 = findViewById(R.id.btnRoom2);
        room = findViewById(R.id.roomPicker);

        btnEndSess.setOnClickListener(this);
        buttonScan.setOnClickListener(this);
        btnRoom1.setOnClickListener(this);
        btnRoom2.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);



//        Date currTime = Calendar.getInstance().getTime();
//        String currDay = ""+currTime.getDate();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Sessions").child(currDay).child("Register_Station").child(firebaseAuth.getUid().toString());
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child("room").exists()){
//                    String temp = dataSnapshot.child("room").getValue().toString();
//                    if(temp.equals("RoomA")){
//                        room.setText("RoomA");
//                    }else if(temp.equals("RoomB")){
//                        room.setText("RoomB");
//                    }
//                    btnRoom1.setEnabled(false);
//                    btnRoom2.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
//        final Date currTime = Calendar.getInstance().getTime();
//        String currDay = ""+currTime.getDate();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Sessions").child(currDay).child("Register_Station").child(firebaseAuth.getUid().toString());
        if(v == btnEndSess){

            startActivity(new Intent(this, RegistrationActivity.class));
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    classSession = new ClassSession(dataSnapshot.child("room").getValue().toString(), dataSnapshot.child("timeStart").getValue().toString(), currTime.toString());
//                    databaseReference.setValue(classSession);
//                    startActivity(new Intent(SessionActivity.this, RegistrationActivity.class));
//                    finish();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

        }else if(v == buttonScan){
            qrScan.initiateScan();

        }else if(v == btnRoom1){
            tempStr = "RoomA";
//            classSession = new ClassSession(tempStr, currTime.toString(),null);
//            databaseReference.setValue(classSession);
            btnRoom1.setEnabled(false);
            btnRoom2.setEnabled(false);
            room.setText(tempStr);
        }else if(v == btnRoom2){
            tempStr = "RoomB";
//            classSession = new ClassSession(tempStr, currTime.toString(), null);
//            databaseReference.setValue(classSession);
            btnRoom1.setEnabled(false);
            btnRoom2.setEnabled(false);
            room.setText(tempStr);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
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
                    final Date currTime = Calendar.getInstance().getTime();
                    String currDay = ""+currTime.getDate();
                    final String ID = result.getContents().substring(result.getContents().lastIndexOf("=") + 1);
                    FirebaseDatabase database =  FirebaseDatabase.getInstance();
                    String tempID = database.getReference("Sessions").child(tempStr).child(currDay).push().getKey();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Sessions").child(tempStr).child(currDay).child(firebaseAuth.getUid().toString());

                    statusClass = new StatusClass(ID, currTime.toString(), "0");
                    databaseReference.child(tempID).setValue(statusClass);
                    qrScan.initiateScan();
//                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
