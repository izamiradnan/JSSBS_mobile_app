package com.example.jssbs.Meeting;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jssbs.Item.DetailActivity;
import com.example.jssbs.Main.MainActivity;
import com.example.jssbs.Model.Item;
import com.example.jssbs.Model.User;
import com.example.jssbs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class SetMeetingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button meetingBtn,selectedDate;
    private TextView txtDate,txtTime;
    private Spinner txtPlace;
    private EditText txtQuan;
    private long mid;
    private String itemID,date,time,meetingID,buyerID,itemQuan,tempID,meetingPlace,sellerID;


    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef,mItemRef,mMeetingRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_meeting);

        mAuth = FirebaseAuth.getInstance();
        mItemRef = FirebaseDatabase.getInstance().getReference("Item");
        mUserRef = FirebaseDatabase.getInstance().getReference("User");
        mMeetingRef = FirebaseDatabase.getInstance().getReference("Meeting");

        final DialogFragment dialogFragment = new DatePickerDialogTheme4();
        final DialogFragment dialogFragment1 = new TimePickerFragment();


        final TextView txtDate = (TextView) findViewById(R.id.meeting_date);
        final TextView txtTime = (TextView) findViewById(R.id.meeting_time);
        Spinner spinner = (Spinner) findViewById(R.id.meeting_place);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Meeting_Place,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        EditText txtQuan = (EditText) findViewById(R.id.item_quantity);

        meetingBtn = findViewById(R.id.meetingBtn);
        selectedDate = findViewById(R.id.date);

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), "Theme 4");
            }
        });



        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment1.show(getSupportFragmentManager(), "time picker");
            }
        });

        meetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMeeting();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String txtPlace = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            TextView textView = (TextView) getActivity().findViewById(R.id.meeting_date);
            textView.setText(year + "-" + (month + 1) + "-" + day);


        }


    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
            return timePickerDialog;

        }

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            TextView textView1 = (TextView) getActivity().findViewById(R.id.meeting_time);
            textView1.setText(hour + ":" + minute);
        }
    }



    private void setMeeting() {

        txtDate=(TextView) findViewById(R.id.meeting_date);
        txtTime=(TextView) findViewById(R.id.meeting_time);
        txtPlace=(Spinner) findViewById(R.id.meeting_place);
        txtQuan=(EditText) findViewById(R.id.item_quantity);

        date = txtDate.getText().toString().trim();
        time = txtTime.getText().toString().trim();
        meetingPlace = txtPlace.getSelectedItem().toString().trim();
        itemQuan = txtQuan.getText().toString().trim();


        Intent intent = getIntent();
        itemID = intent.getStringExtra("Item_ID");
        sellerID = intent.getStringExtra("Seller ID");

        if(TextUtils.isEmpty(date)){
            Toast.makeText(this, "Please choose meeting date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(time)){
            Toast.makeText(this, "Please choose meeting time", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(meetingPlace)){
            Toast.makeText(this, "Please choose meeting place", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(itemQuan)){
            Toast.makeText(this, "Please enter item quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        mUserRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    buyerID = user.getUid();

                    meetingID = date +" "+ time;

                    meetingDatabase(buyerID,meetingID,date,time,meetingPlace,itemQuan,itemID,sellerID);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void meetingDatabase(String buyerID, String meetingID, String date, String time, String meetingPlace, final String itemQuan, final String itemID, String sellerID) {
        final HashMap<String,Object> productMap = new HashMap<>();

        productMap.put("meetingID",meetingID);
        productMap.put("itemID",itemID);
        productMap.put("date",date);
        productMap.put("time",time);
        productMap.put("meetingPlace",meetingPlace);
        productMap.put("quantity",itemQuan);
        productMap.put("buyerID",buyerID);
        productMap.put("sellerID",sellerID);

        mMeetingRef.child(meetingID).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SetMeetingActivity.this,"Meeting set",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SetMeetingActivity.this, MainActivity.class));
                    }
        });

        mItemRef.child(itemID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Item item = dataSnapshot.getValue(Item.class);
                    if(itemID.equals(item.getItemID())){

                        int newStock = Integer.parseInt(item.getItemStock()) - Integer.parseInt(itemQuan);
                        String itemStock = Integer.toString(newStock);

                        tempItem(itemStock);
                    }

                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tempItem(String itemStock) {

        final HashMap<String,Object> productMap2 = new HashMap<>();

        productMap2.put("itemStock",itemStock);


        mItemRef.child(itemID).updateChildren(productMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SetMeetingActivity.this, "Item Booked", Toast.LENGTH_SHORT).show();

            }
        });
    }


}
