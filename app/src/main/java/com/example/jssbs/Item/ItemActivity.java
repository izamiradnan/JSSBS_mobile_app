package com.example.jssbs.Item;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jssbs.RegisterLogin.LoginActivity;
import com.example.jssbs.ViewHolder.ItemAdapter;
import com.example.jssbs.Model.Item;
import com.example.jssbs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mData;
    private DatabaseReference mUserRef,mUser;

    RecyclerView recyclerView;
    ArrayList<Item> itemArrayList;

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
        setContentView(R.layout.activity_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.itemRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemArrayList = new ArrayList<Item>();
        final ItemAdapter[] adapter = new ItemAdapter[1];

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance();
        mUserRef = mData.getReference().child("Item");
        mUser = mData.getReference();
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    final Item item = dataSnapshot1.getValue(Item.class);

                    if(item.getSellerID().equals(mAuth.getCurrentUser().getUid()))
                        itemArrayList.add(item);

                }

                adapter[0] = new ItemAdapter(ItemActivity.this,itemArrayList);
                recyclerView.setAdapter(adapter[0]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ItemActivity.this, "Error",Toast.LENGTH_SHORT).show();
            }
        });

    }



}
