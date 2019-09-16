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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity
        implements View.OnClickListener{

    private Button buttonLogout, buttonScan;
    private TextView textViewName;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    private IntentIntegrator qrScan;
    public String temporaryStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Logging in....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);

        qrScan = new IntentIntegrator(this);



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
        buttonScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogout){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
<<<<<<< HEAD
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    temporaryStr = result.getContents();
//                    Toast.makeText(this, temporaryStr, Toast.LENGTH_LONG).show();
                    temporaryStr.substring(temporaryStr.lastIndexOf("=")+1);
                    Toast.makeText(this, temporaryStr, Toast.LENGTH_LONG).show();


=======
                    String ID = result.getContents().substring(result.getContents().lastIndexOf("=") + 1);
                    Toast.makeText(this, ID, Toast.LENGTH_LONG).show();
>>>>>>> a90674315f642019d7fad533c4d1f9256b40d06e
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
