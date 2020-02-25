package com.example.jssbs.Meeting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jssbs.Model.Meeting;
import com.example.jssbs.R;
import com.example.jssbs.RegisterLogin.LoginActivity;
import com.example.jssbs.ViewHolder.MeetingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewMeetingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mData;
    private DatabaseReference mMeetingRef;

    RecyclerView recyclerView;
    ArrayList<Meeting> meetingArrayList;

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
        setContentView(R.layout.activity_view_meeting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.meetingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        meetingArrayList = new ArrayList<Meeting>();
        final MeetingAdapter[] mAdapter = new MeetingAdapter[1];

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance();
        mMeetingRef = mData.getReference().child("Meeting");
        mMeetingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    final Meeting meeting = dataSnapshot1.getValue(Meeting.class);

                    if(meeting.getSellerID().equals(mAuth.getCurrentUser().getUid()) || meeting.getBuyerID().equals(mAuth.getCurrentUser().getUid()))
                        meetingArrayList.add(meeting);

                }

                mAdapter[0] = new MeetingAdapter(ViewMeetingActivity.this,meetingArrayList);
                recyclerView.setAdapter(mAdapter[0]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewMeetingActivity.this, "Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
