package com.example.tanvigupta.todolist3;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String DATE_KEY = "date";
    public static final String TIME_KEY = "time";
    EditText txtDate, txtTime;
    ImageView datebtn, timebtn;
    private int mYEAR, mMONTH, mDAY, mHOUR, mMINUTE;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String title,description,date,time;


    EditText ed1, ed2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ed1 = findViewById(R.id.edittext1);
        ed2 = findViewById(R.id.edittext2);
        txtDate = findViewById(R.id.edittext3);
        txtTime = findViewById(R.id.edittext4);

        datebtn = findViewById(R.id.datebtn);
        timebtn = findViewById(R.id.timebtn);
        toolbar=findViewById(R.id.toolbar);
        collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Note");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);



        Intent intent1 = getIntent();
        title = intent1.getStringExtra(Description.TITLE_KEY);
        description = intent1.getStringExtra(Description.DESCRIPTION_KEY);
        date = intent1.getStringExtra(Description.DATE_KEY);
        time = intent1.getStringExtra(Description.TIME_KEY);


        ed1.setText(title);
        ed2.setText(description);
        txtDate.setText(date);
        txtTime.setText(time);

        datebtn.setOnClickListener(EditActivity.this);
        timebtn.setOnClickListener(EditActivity.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });





    }


    public void saveNote(View view) {
        title = ed1.getText().toString();
      description = ed2.getText().toString();
      date =txtDate.getText().toString();
      time = txtTime.getText().toString();

        Intent data = new Intent();
        data.putExtra(TITLE_KEY, title);
        data.putExtra(DESCRIPTION_KEY, description);
        data.putExtra(DATE_KEY, date);
        data.putExtra(TIME_KEY, time);

        setResult(3, data);
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.datebtn) {

            //GET CURRENT DATE
            final Calendar c = Calendar.getInstance();
            mDAY = c.get(Calendar.DAY_OF_MONTH);
            mMONTH = c.get(Calendar.DATE);
            mYEAR = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    txtDate.setText(i + "-" + (i1 + 1) + "-" + i2);
                }
            }, mDAY, mMONTH, mYEAR);
            datePickerDialog.show();


        } else if (id == R.id.timebtn) {
            //GET CURRENT TIME
            final Calendar c = Calendar.getInstance();
            mHOUR = c.get(Calendar.HOUR_OF_DAY);
            mMINUTE = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    txtTime.setText(i + ":" + i1);
                }
            }, mHOUR, mMINUTE, true);
            timePickerDialog.show();

        }
    }


}




