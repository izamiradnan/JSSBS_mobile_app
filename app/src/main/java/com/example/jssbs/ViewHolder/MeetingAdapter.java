package com.example.jssbs.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jssbs.Meeting.MeetingAct;
import com.example.jssbs.Meeting.UpdateMeetingActivity;
import com.example.jssbs.Meeting.MeetingActBuyer;
import com.example.jssbs.Model.Meeting;
import com.example.jssbs.Model.User;
import com.example.jssbs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder>{
    Context context;
    ArrayList<Meeting> meeting;
    private DatabaseReference mMeetingRef,mItemRef,mUserRef;
    private FirebaseAuth mAuth;
    private String phoneNumber;



    public MeetingAdapter(Context c, ArrayList<Meeting> m){
        context = c;
        meeting = m;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.meeting_card_view,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        mAuth = FirebaseAuth.getInstance();
        mMeetingRef = FirebaseDatabase.getInstance().getReference().child("Meeting");
        mItemRef = FirebaseDatabase.getInstance().getReference().child("Item");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("User");


        if(meeting.get(i).getSellerID().equals(mAuth.getCurrentUser().getUid()))
            myViewHolder.type.setText("Seller");
        if(meeting.get(i).getBuyerID().equals(mAuth.getCurrentUser().getUid()))
            myViewHolder.type.setText("Buyer");

        //myViewHolder.date.setText(meeting.get(i).getDate());
        //myViewHolder.place.setText(meeting.get(i).getMeetingPlace());
        //myViewHolder.time.setText(meeting.get(i).getTime());
        //myViewHolder.itemQuan.setText(meeting.get(i).getQuantity());
        myViewHolder.itemID.setText(meeting.get(i).getItemID());
        myViewHolder.buyerID.setText(meeting.get(i).getBuyerID());
        //myViewHolder.sellerID.setText(meeting.get(i).getSellerID());

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                 User user = dataSnapshot2.getValue(User.class);

                 if(meeting.get(i).getSellerID().equals(user.getUid())){
                     phoneNumber = user.getPhoneNumber();
                 }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        myViewHolder.meetingCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(meeting.get(i).getSellerID().equals(mAuth.getCurrentUser().getUid())) {
                    Intent intent = new Intent(context, MeetingAct.class);
                    intent.putExtra("Type", myViewHolder.type.toString());
                    intent.putExtra("Item ID", meeting.get(i).getItemID());
                    intent.putExtra("Meeting Date", meeting.get(i).getDate());
                    intent.putExtra("Meeting Place", meeting.get(i).getMeetingPlace());
                    intent.putExtra("Meeting Time", meeting.get(i).getTime());
                    intent.putExtra("Item Quantity", meeting.get(i).getQuantity());
                    intent.putExtra("Buyer ID", meeting.get(i).getBuyerID());
                    intent.putExtra("Seller ID", meeting.get(i).getSellerID());
                    intent.putExtra("Meeting ID", meeting.get(i).getMeetingID());
                    intent.putExtra("Phone Number", phoneNumber);

                    context.startActivity(intent);

                    Intent intent1 = new Intent(context, UpdateMeetingActivity.class);
                    intent1.putExtra("Meeting ID2", meeting.get(i).getMeetingID());

                    context.startActivity(intent1);

                }
                else if(meeting.get(i).getBuyerID().equals(mAuth.getCurrentUser().getUid())){
                    Intent intent = new Intent(context, MeetingActBuyer.class);
                    intent.putExtra("Type",myViewHolder.type.toString());
                    intent.putExtra("Item ID",meeting.get(i).getItemID());
                    intent.putExtra("Meeting Date",meeting.get(i).getDate());
                    intent.putExtra("Meeting Place",meeting.get(i).getMeetingPlace());
                    intent.putExtra("Meeting Time",meeting.get(i).getTime());
                    intent.putExtra("Item Quantity",meeting.get(i).getQuantity());
                    intent.putExtra("Buyer ID",meeting.get(i).getBuyerID());
                    intent.putExtra("Seller ID",meeting.get(i).getSellerID());
                    intent.putExtra("Meeting ID",meeting.get(i).getMeetingID());
                    intent.putExtra("Phone Number",phoneNumber);

                    context.startActivity(intent);

                    Intent intent1 = new Intent(context, UpdateMeetingActivity.class);
                    intent1.putExtra("Meeting ID2",meeting.get(i).getMeetingID());

                    context.startActivity(intent1);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return meeting.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView type,itemID,date,place,time,itemQuan,sellerID,buyerID;
        CardView meetingCV;
        public MyViewHolder(View itemView){
            super(itemView);

            type = (TextView) itemView.findViewById(R.id.meeting_type);
            itemID = (TextView) itemView.findViewById(R.id.item_id);
            //date = (TextView) itemView.findViewById(R.id.meeting_date);
            //place = (TextView) itemView.findViewById(R.id.meeting_place);
            //time = (TextView) itemView.findViewById(R.id.meeting_time);
            //itemQuan = (TextView) itemView.findViewById(R.id.item_quantity);
            //sellerID = (TextView) itemView.findViewById(R.id.seller_id);
            buyerID = (TextView) itemView.findViewById(R.id.buyer_id);
            meetingCV = (CardView) itemView.findViewById(R.id.meetingCV);

        }
    }



}
