package com.example.jssbs.User;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jssbs.Model.User;
import com.example.jssbs.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminViewUserActivity extends AppCompatActivity {

    private ListView listView;

    private FirebaseDatabase mData;
    private DatabaseReference mUserRef;

    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;

    User userview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_user);

       userview = new User();

       listView = findViewById(R.id.listView);

       mData = FirebaseDatabase.getInstance();
       mUserRef = mData.getReference("User");

       list = new ArrayList<>();
       adapter = new ArrayAdapter<String>(this,R.layout.admin_view,R.id.name,list);

       mUserRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot ds: dataSnapshot.getChildren()){
                   userview = ds.getValue(User.class);
                   list.add(userview.getName().toString() + "\n" + userview.getStudentID().toString() + "\n" + userview.getEmail().toString() + "\n" + userview.getPhoneNumber() + "\n\n");
               }
               listView.setAdapter(adapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


    }
}
