package com.example.jssbs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class TestDate extends AppCompatActivity{
    android.widget.DatePicker dateP;
    TextView showDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_date);

        dateP = (DatePicker) findViewById(R.id.datePicker);
        showDate = (TextView)findViewById(R.id.showDate);
    }

    public void click_select(View view){
        showDate.setText(record_date());
    }

    public String record_date(){
        String txtDate = dateP.getDayOfMonth()+"/"+dateP.getMonth()+"/"+dateP.getYear();
        return txtDate;
    }
}
