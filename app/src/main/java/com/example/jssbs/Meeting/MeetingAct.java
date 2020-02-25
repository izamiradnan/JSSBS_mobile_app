package com.example.jssbs.Meeting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jssbs.Item.DetailActivity;
import com.example.jssbs.Item.ItemActivity;
import com.example.jssbs.Model.Item;
import com.example.jssbs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MeetingAct extends AppCompatActivity {

    TextView type,time,date,place,buyerID,itemID,itemQuan,sellerID,phoneNumber;
    TextView uMeetingTime,uMeetingDate;
    Spinner uMeetingPlace;
    String meetingID,meetingType,meetingTime,meetingDate,meetingPlace,BuyerID,ItemID,itemQuantity,SellerID,PhoneNumber;
    Button updateMeetingBtn,deleteMeetingBtn,cancelBtn;
    private DatabaseReference mMeetingRef,mItemRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meeting2);

        mMeetingRef = FirebaseDatabase.getInstance().getReference("Meeting");
        mItemRef = FirebaseDatabase.getInstance().getReference("Item");
        mAuth = FirebaseAuth.getInstance();
        //final DialogFragment dialogFragment = new SetMeetingActivity.DatePickerDialogTheme4();
        //final DialogFragment dialogFragment1 = new SetMeetingActivity.TimePickerFragment();

        type = findViewById(R.id.meeting_type);
        time = findViewById(R.id.meeting_time);
        date = findViewById(R.id.meeting_date);
        place = findViewById(R.id.meeting_place);
        buyerID = findViewById(R.id.buyer_id);
        itemID = findViewById(R.id.item_id);
        itemQuan = findViewById(R.id.item_quantity);
        sellerID = findViewById(R.id.seller_id);
        updateMeetingBtn = findViewById(R.id.updateMeetingBtn);
        deleteMeetingBtn = findViewById(R.id.deleteMeetingBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        phoneNumber = findViewById(R.id.phoneNumber);

        meetingID = getIntent().getStringExtra("Meeting ID");
        meetingType = getIntent().getStringExtra("Meeting Type");
        meetingTime = getIntent().getStringExtra("Meeting Time");
        meetingDate = getIntent().getStringExtra("Meeting Date");
        meetingPlace = getIntent().getStringExtra("Meeting Place");
        BuyerID = getIntent().getStringExtra("Buyer ID");
        ItemID = getIntent().getStringExtra("Item ID");
        itemQuantity = getIntent().getStringExtra("Item Quantity");
        SellerID = getIntent().getStringExtra("Seller ID");
        PhoneNumber = getIntent().getStringExtra("Phone Number");

        type.setText(meetingType);
        time.setText(meetingTime);
        date.setText(meetingDate);
        place.setText(meetingPlace);
        buyerID.setText(BuyerID);
        itemID.setText(ItemID);
        itemQuan.setText(itemQuantity);
        sellerID.setText(SellerID);
        phoneNumber.setText(PhoneNumber);

        //uMeetingDate.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View v) {
        //dialogFragment.show(getSupportFragmentManager(), "Theme 4");
        //}
        //});



        // uMeetingTime.setOnClickListener(new View.OnClickListener() {
        // @Override
        //public void onClick(View v) {
        // dialogFragment1.show(getSupportFragmentManager(), "time picker");
        // }
        //  });

        updateMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // mMeetingRef.addValueEventListener(new ValueEventListener() {
                    //@Override
                   // public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            //Meeting meeting = dataSnapshot1.getValue(Meeting.class);
                            //if(buyerID.getText().toString().equals(meeting.getBuyerID()) || sellerID.getText().toString().equals(meeting.getSellerID()))
                               // meetingID = meeting.getMeetingID();
                           // Toast.makeText(MeetingAct.this, "MASUK", Toast.LENGTH_SHORT).show();
                       // }
                  //  }

                    //@Override
                   // public void onCancelled(@NonNull DatabaseError databaseError) {

                    //}
               // });

                startActivity(new Intent(MeetingAct.this, UpdateMeetingActivity.class) );


            }
        });

       deleteMeetingBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatabaseReference drMeeting = FirebaseDatabase.getInstance().getReference("Meeting").child(meetingID);
               if(mAuth.getCurrentUser().getUid().equals(SellerID)){
                   drMeeting.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()) {
                           Toast.makeText(MeetingAct.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(MeetingAct.this, ViewMeetingActivity.class));
                       } else {
                           Toast.makeText(MeetingAct.this, "Delete fail", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
               }
           }
       });

       cancelBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mItemRef.child(ItemID).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.hasChildren()){
                           Item item = dataSnapshot.getValue(Item.class);

                           int newStock = Integer.parseInt(item.getItemStock()) + Integer.parseInt(itemQuantity);
                           String itemStock = Integer.toString(newStock);
                           final HashMap<String,Object> productMap2 = new HashMap<>();
                           productMap2.put("itemStock",itemStock);
                           mItemRef.child(ItemID).updateChildren(productMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   Toast.makeText(MeetingAct.this, "Item Cancelled", Toast.LENGTH_SHORT).show();

                               }
                           });
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

               DatabaseReference drMeeting = FirebaseDatabase.getInstance().getReference("Meeting").child(meetingID);
               if(mAuth.getCurrentUser().getUid().equals(SellerID)){
                   drMeeting.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               Toast.makeText(MeetingAct.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(MeetingAct.this, ViewMeetingActivity.class));
                           } else {
                               Toast.makeText(MeetingAct.this, "Delete fail", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
           }
       });

    }

}
