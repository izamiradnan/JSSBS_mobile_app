package com.example.jssbs.RegisterLogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jssbs.Main.MainActivity;
import com.example.jssbs.Model.User;
import com.example.jssbs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {

    private EditText mName, mStudentID, mPhoneNo, mEmail, mPassword;
    private long uid;
    private Button mRegisterButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef,reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //create instances
        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference();
        reff = FirebaseDatabase.getInstance().getReference("User");

        //link with xml
        mName = findViewById(R.id.Rname);
        mStudentID = findViewById(R.id.Rid);
        mPhoneNo = findViewById(R.id.Rphone);
        mEmail = findViewById(R.id.Remail);
        mPassword = findViewById(R.id.Rpassword);

        mRegisterButton = findViewById(R.id.regButton);

        //attach listener to the button

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
              }
        });

    }

    private void register(){
        String name = mName.getText().toString().trim();
        String studentID = mStudentID.getText().toString().trim();
        String phoneNo = mPhoneNo.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(studentID)){
            Toast.makeText(this, "Please enter your student ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phoneNo)){
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    createUser();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createUser(){
        String mUid = mAuth.getCurrentUser().getUid();
        String name = mName.getText().toString().trim();
        String studentID = mStudentID.getText().toString().trim();
        String phoneNo = mPhoneNo.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();


        User user = new User(mUid,name,studentID,phoneNo,email,password);
        mUserRef.child("User").child(mUid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }else{
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

