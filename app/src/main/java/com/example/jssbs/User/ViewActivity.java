package com.example.jssbs.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.jssbs.Model.User;
import com.example.jssbs.R;
import com.example.jssbs.RegisterLogin.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewActivity extends AppCompatActivity {

    private TextView userID,name,studentID,phoneNumber,email;
    private Button updateBtn;



    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logOut:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference();

        userID = findViewById(R.id.userID);
        name = findViewById(R.id.name);
        studentID = findViewById(R.id.studentID);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        updateBtn = findViewById(R.id.updateBtn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ViewActivity.this, UpdateActivity.class));
            }
        });

        mUserRef.child("User").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    String Vuserid = user.getUid();
                    String Vname = user.getName();
                    String Vstudentid = user.getStudentID();
                    String VphoneNumber = user.getPhoneNumber();
                    String Vemail = user.getEmail();

                    userID.setText(Vuserid);
                    name.setText(Vname);
                    studentID.setText(Vstudentid);
                    phoneNumber.setText(VphoneNumber);
                    email.setText(Vemail);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });}}

