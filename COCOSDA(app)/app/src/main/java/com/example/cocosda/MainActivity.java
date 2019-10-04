package com.example.cocosda;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In....");
        progressDialog.setCancelable(false);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            progressDialog.show();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("user_station").child(firebaseAuth.getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String temp;
                    temp = dataSnapshot.child("group_station").getValue().toString();
                    if(temp.equals("meal_station")){
                        Toast.makeText(MainActivity.this, temp, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, MealClaimingActivity.class));
                        finish();
                    }else if(temp.equals("kit_station")){
                        Toast.makeText(MainActivity.this, temp, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                        finish();
                    }else if(temp.equals("register_station")){
                        Toast.makeText(MainActivity.this, temp,  Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//            startActivity(new Intent(getApplicationContext(), UserActivity.class));
        }


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(TextUtils.isEmpty(editTextEmail.getText().toString())){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(editTextPassword.getText().toString())){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }else{
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("user_station").child(firebaseAuth.getUid());
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String temp;
                                        temp = dataSnapshot.child("group_station").getValue().toString();
                                        if(temp.equals("meal_station")){
                                            Toast.makeText(MainActivity.this, temp, Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(MainActivity.this, MealClaimingActivity.class));
                                            finish();
                                        }else if(temp.equals("kit_station")){
                                            Toast.makeText(MainActivity.this, temp,  Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(MainActivity.this, UserActivity.class));
                                            finish();
                                        }else if(temp.equals("register_station")){
                                            Toast.makeText(MainActivity.this, temp,  Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                //Filtering of users happens here
//                                startActivity(new Intent(getApplicationContext(), UserActivity.class));
                            }else{
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                editTextEmail.setText("");
                                editTextPassword.setText("");
                            }
                        }
                    });
        }
    }
}
