package com.example.jssbs.Item;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jssbs.Main.MainActivity;
import com.example.jssbs.Model.Item;
import com.example.jssbs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateItemActivity extends AppCompatActivity {

    private EditText itemPrice,itemQuantity;
    private Button updateItemBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mItemRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        mAuth = FirebaseAuth.getInstance();

        itemPrice = findViewById(R.id.editItemPrice);
        itemQuantity = findViewById(R.id.editItemQuantity);
        updateItemBtn = findViewById(R.id.updateItemBtn);

        updateItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemProcess();
            }
        });
    }

    private void updateItemProcess() {



        }
    }