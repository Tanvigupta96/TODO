package com.example.tanvigupta.todolist3;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import java.util.ArrayList;
import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity
    implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String DATE_KEY = "date";
    public static final String TIME_KEY = "time";
    public static final String CATEGORY_KEY = "category";

    EditText txtDate, txtTime;
    Button datebtn, timebtn;
    private int mYEAR, mMONTH, mDAY, mHOUR, mMINUTE;
    Spinner spin;
    String spinner_item;
    EditText descriptioneditText;
    SQLiteDatabase database;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        datebtn = findViewById(R.id.datebtn);
        timebtn = findViewById(R.id.timebtn);
        txtDate = findViewById(R.id.edittext3);
        txtTime = findViewById(R.id.edittext4);
        descriptioneditText = findViewById(R.id.edittext2);

        NoteOpenHelper openHelper = NoteOpenHelper.getInstance(this);
        database = openHelper.getReadableDatabase();

        datebtn.setOnClickListener(this);
        timebtn.setOnClickListener(this);

        spin = findViewById(R.id.spinner);

        //spinner click listener
        spin.setOnItemSelectedListener(this);

        //spinner drop down elements
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Work");
        categories.add("Home");
        categories.add("Personal");
        categories.add("College");
        categories.add("Others");

        //creating adaptor for spinner
        ArrayAdapter<String> dataAdapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);

        //drop-down layout style -listview with menu buttons
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spin.setAdapter(dataAdapter);

        Intent intent = getIntent();
        String description = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (description != null) {
            descriptioneditText.setText(description);
            descriptioneditText.setSelection(descriptioneditText.getText().length());
        }
    }

    public void saveNote(View view) {

        ContentValues contentValues = new ContentValues();

        EditText titleeditText = findViewById(R.id.edittext1);

        String title = titleeditText.getText().toString();
        String description = descriptioneditText.getText().toString();
        String date = txtDate.getText().toString();
        String time = txtTime.getText().toString();

        //DATABASE THING

        //yaha se we are saving data into the database
        contentValues.put(Contract.NOTE.COLUMN_TITLE, title);
        contentValues.put(Contract.NOTE.COLUMN_DESCRIPTION, description);
        contentValues.put(Contract.NOTE.COLUMN_DATE, date);
        contentValues.put(Contract.NOTE.COLUMN_NOTE_TIME, time);
        contentValues.put(Contract.NOTE.COLUMN_CATEGORY, spinner_item);

        database.insert(Contract.NOTE.TABLE_NAME, null, contentValues);

        Intent data = new Intent();
        data.putExtra(TITLE_KEY, title);
        data.putExtra(DESCRIPTION_KEY, description);
        data.putExtra(DATE_KEY, date);
        data.putExtra(TIME_KEY, time);
        data.putExtra(CATEGORY_KEY, spinner_item);

        setResult(2, data);
        finish();
    }

    @Override public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.datebtn) {

            //GET CURRENT DATE
            final Calendar c = Calendar.getInstance();
            mDAY = c.get(Calendar.DAY_OF_MONTH);
            mMONTH = c.get(Calendar.DATE);
            mYEAR = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog =
                new DatePickerDialog(AddNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txtDate.setText(i + "-" + (i1 + 1) + "-" + i2);
                    }
                }, mDAY, mMONTH, mYEAR);
            datePickerDialog.show();
        } else if (id == R.id.timebtn) {
            //GET CURRENT TIME
            final Calendar c = Calendar.getInstance();
            mHOUR = c.get(Calendar.HOUR_OF_DAY);
            mMINUTE = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog =
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        txtTime.setText(i + ":" + i1);
                    }
                }, mHOUR, mMINUTE, true);
            timePickerDialog.show();
        }
    }

    @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinner_item = adapterView.getItemAtPosition(i).toString();
        Log.d("AddNoteActivity.class", spinner_item);
    }

    @Override public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

