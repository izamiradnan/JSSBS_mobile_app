package com.example.jssbs.Meeting;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jssbs.Main.MainActivity;
import com.example.jssbs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class UpdateMeetingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView type,time,date,place,buyerID,itemID,itemQuan,sellerID;
    TextView uMeetingTime,uMeetingDate;
    Spinner uMeetingPlace;
    String MeetingID,meetingID,meetingType,meetingTime,meetingDate,meetingPlace,BuyerID,ItemID,itemQuantity,SellerID;
    Button update;
    private DatabaseReference mMeetingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meeting);

        mMeetingRef = FirebaseDatabase.getInstance().getReference("Meeting");

        Spinner spinner = (Spinner) findViewById(R.id.editMeetingPlace);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Meeting_Place,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        final DialogFragment dialogFragment = new UpdateMeetingActivity.DatePickerDialogTheme4();
        final DialogFragment dialogFragment1 = new UpdateMeetingActivity.TimePickerFragment();


        //time = findViewById(R.id.meeting_time);
        //date = findViewById(R.id.meeting_date);
       // place = findViewById(R.id.meeting_place);


        uMeetingTime = findViewById(R.id.editMeetingTime);
        uMeetingPlace = findViewById(R.id.editMeetingPlace);
        uMeetingDate = findViewById(R.id.editMeetingDate);

        update = findViewById(R.id.updateMeetingButton);

        Intent intent1 = getIntent();
        MeetingID = intent1.getStringExtra("Meeting ID2");

        //Toast.makeText(UpdateMeetingActivity.this, "Masuk", Toast.LENGTH_SHORT).show();

       // meetingTime = time.getText().toString();
                //getIntent().getStringExtra("Meeting Time");
        //meetingDate = date.getText().toString();
                //getIntent().getStringExtra("Meeting Date");
        //meetingPlace = place.
                //getIntent().getStringExtra("Meeting Place");



        //time.setText(meetingTime);
        //date.setText(meetingDate);
        //place.setText(meetingPlace);


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


        uMeetingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), "Theme 4");
            }
        });

        uMeetingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment1.show(getSupportFragmentManager(), "time picker");
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMeeting(MeetingID);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String uMeetingPlace = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            return datePickerDialog;
        }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView textView = (TextView) getActivity().findViewById(R.id.editMeetingDate);
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
            TextView textView1 = (TextView) getActivity().findViewById(R.id.editMeetingTime);
            textView1.setText(hour + ":" + minute);
        }
    }

    private void updateMeeting(String meetingID) {

                HashMap<String, Object> result = new HashMap<>();

                String MEetingID = meetingID;

                uMeetingDate =(TextView) findViewById(R.id.editMeetingDate);
                uMeetingTime =(TextView) findViewById(R.id.editMeetingTime);
                uMeetingPlace =(Spinner) findViewById(R.id.editMeetingPlace);

                String newDate = uMeetingDate.getText().toString().trim();
                String newTime = uMeetingTime.getText().toString().trim();
                String newPlace = uMeetingPlace.getSelectedItem().toString().trim();


                result.put("meetingPlace", newPlace);
                result.put("time", newTime);
                result.put("date", newDate);

                mMeetingRef.child(MEetingID).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateMeetingActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateMeetingActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(UpdateMeetingActivity.this, "Update fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
    }





