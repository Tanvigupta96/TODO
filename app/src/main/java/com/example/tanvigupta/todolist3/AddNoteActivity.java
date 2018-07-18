package com.example.tanvigupta.todolist3;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String DATE_KEY = "date";
    public static final String TIME_KEY = "time";
    public static final String CATEGORY_KEY = "category";
    public static final String ID = "id";
    public static final String IS_STARRED="isstarred";

    EditText txtDate, txtTime;
    ImageView datebtn, timebtn;
    private int mYEAR, mMONTH, mDAY, mHOUR, mMINUTE;
    Spinner spin;


    String spinner_item;
    EditText descriptioneditText;

    SQLiteDatabase database;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        datebtn = findViewById(R.id.datebtn);
        timebtn = findViewById(R.id.timebtn);
        txtDate = findViewById(R.id.edittext3);
        txtTime = findViewById(R.id.edittext4);
        descriptioneditText = findViewById(R.id.edittext2);





        NoteOpenHelper openHelper = NoteOpenHelper.getInstance(this);
        database = openHelper.getWritableDatabase();

        datebtn.setOnClickListener(this);
        timebtn.setOnClickListener(this);

        spin = findViewById(R.id.spinner);

        //spinner click listener
        spin.setOnItemSelectedListener(this);

        //spinner drop down elements
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Work");
        categories.add("Home");
        categories.add("Personal");
        categories.add("College");
        categories.add("Others");

        //creating adaptor for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories);

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



        long id = database.insert(Contract.NOTE.TABLE_NAME, null, contentValues);



        //for deletion
        Intent intent1=new Intent(AddNoteActivity.this,MainActivity.class);
        intent1.putExtra(ID,id);
        Log.d("AddNoteActivity",id+"");
        startActivity(intent1);



        Intent data = new Intent();
        data.putExtra(TITLE_KEY, title);
        data.putExtra(DESCRIPTION_KEY, description);
        data.putExtra(DATE_KEY, date);
        data.putExtra(TIME_KEY, time);
        data.putExtra(CATEGORY_KEY, spinner_item);
        data.putExtra(ID, id);



        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mYEAR);
        cal.set(Calendar.MONTH, mMONTH);
        cal.set(Calendar.DAY_OF_MONTH, mDAY);
        cal.set(Calendar.HOUR_OF_DAY, mHOUR);
        cal.set(Calendar.MINUTE, mMINUTE);


        Intent i = new Intent(this, NotificationReciever.class);
        i.putExtra(ID, id);
        i.putExtra(TIME_KEY, time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, i, 0);
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        setResult(2, data);
        finish();




    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Calendar calendar = Calendar.getInstance();
        if (id == R.id.datebtn) {
            mYEAR = calendar.get(Calendar.YEAR);
            mMONTH = calendar.get(Calendar.MONTH);
            mDAY = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(AddNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            txtDate.setText(i + "-" + (i1 + 1) + "-" + i2);
                            mYEAR = i;
                            mMONTH = i1;
                            mDAY = i2;

                        }
                    }, mYEAR, mMONTH, mDAY);
            datePickerDialog.show();
        } else if (id == R.id.timebtn) {
            //GET CURRENT TIME
            mHOUR = calendar.get(Calendar.HOUR_OF_DAY);
            mMINUTE = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog =
                    new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            txtTime.setText(i + ":" + i1);
                            mHOUR = i;
                            mMINUTE = i1;
                        }
                    }, mHOUR, mMINUTE, true);
            timePickerDialog.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinner_item = adapterView.getItemAtPosition(i).toString();
        Log.d("AddNoteActivity.class", spinner_item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }




}

