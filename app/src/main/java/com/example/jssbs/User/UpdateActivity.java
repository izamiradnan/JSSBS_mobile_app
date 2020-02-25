package com.example.jssbs.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jssbs.Main.MainActivity;
import com.example.jssbs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private Button confirmUpdateBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference();

        phoneNumber = findViewById(R.id.editPhoneNumber);
        confirmUpdateBtn = findViewById(R.id.confirmUpdateBtn);

        confirmUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProcess();
            }
        });


    }

    private void updateProcess() {
        String uUserID = mAuth.getUid();
        String uPhoneNumber = phoneNumber.getText().toString().trim();

        HashMap<String,Object> result = new HashMap<>();

        result.put("phoneNumber",uPhoneNumber);

        mUserRef.child("User").child(uUserID).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(UpdateActivity.this, "Update fail", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
