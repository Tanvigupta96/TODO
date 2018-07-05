package com.example.tanvigupta.todolist3;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener,
    AdapterView.OnItemSelectedListener {

    ListView listView;
    Spinner spin;

    ArrayList<Note> notes = new ArrayList<>();
    TextView title, description, date, time;
    CustomAdaptor adaptor;
    public static final String KEY_VALUE1 = "title", KEY_VALUE2 = "description", KEY_VALUE3 = "date",
        KEY_VALUE4 = "time", KEY_VALUE5 = "category";
    public static final String ID = "id";
    private int position;
    //private long id;
    SQLiteDatabase database;

    String message;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        spin = findViewById(R.id.spin1);

        // We only need to initialize the db once
        NoteOpenHelper openHelper = NoteOpenHelper.getInstance(getApplicationContext());
        database = openHelper.getReadableDatabase();

        //spinner drop down elements
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Work");
        categories.add("Home");
        categories.add("Personal");
        categories.add("College");
        categories.add("Others");
        categories.add("All");

        //creating adaptor for spinner
        ArrayAdapter<String> dataAdapter =
            new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);

        //drop-down layout style -listview with menu buttons
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(dataAdapter);

        //spinner click listener
        spin.setOnItemSelectedListener(this);

        //attaching data adapter to spinner
        //spin.setAdapter(dataAdapter);

        adaptor = new CustomAdaptor(this, notes, getLayoutInflater());

        listView.setAdapter(adaptor);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
                Intent intent = new Intent(this, AddNoteActivity.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.remove:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                dialog2.setTitle("long click on the item to delete!!");
                dialog2.setCancelable(false);
                dialog2.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog1 = dialog2.create();
                dialog1.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "activity result called");

        //  NoteOpenHelper openHelper = NoteOpenHelper.getInstance(this);
        //database = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        switch (resultCode) {
            //add new item
            case 2:
                String title = data.getStringExtra(AddNoteActivity.TITLE_KEY);
                String description = data.getStringExtra(AddNoteActivity.DESCRIPTION_KEY);
                String date = data.getStringExtra(AddNoteActivity.DATE_KEY);
                String time = data.getStringExtra(AddNoteActivity.TIME_KEY);
                String category = data.getStringExtra(AddNoteActivity.CATEGORY_KEY);

                Note note = new Note(title, description, date, time, category);
                notes.add(note);
                adaptor.notifyDataSetChanged();
                break;

            case 5:
                //edit item
                title = data.getStringExtra(Description.TITLE_KEY);
                description = data.getStringExtra(Description.DESCRIPTION_KEY);
                date = data.getStringExtra(Description.DATE_KEY);
                time = data.getStringExtra(Description.TIME_KEY);
                category = data.getStringExtra(Description.CATEGORY_KEY);
                long id = data.getLongExtra(Description.ID_KEY, 0);

                note = new Note(title, description, date, time, category);

                notes.remove(position);
                notes.add(position, note);

                contentValues.put(Contract.NOTE.COLUMN_TITLE, title);
                contentValues.put(Contract.NOTE.COLUMN_DESCRIPTION, description);
                contentValues.put(Contract.NOTE.COLUMN_DATE, date);
                contentValues.put(Contract.NOTE.COLUMN_NOTE_TIME, time);

                String[] values = { id + "" };

                database.update(Contract.NOTE.TABLE_NAME, contentValues, Contract.NOTE.COLUMN_ID + "= ?",
                    values);

                adaptor.notifyDataSetChanged();
                break;
        }
    }

    @Override public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Note note = notes.get(i);
        position = i;

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("confirm delete!");
        dialog.setMessage("Are you sure you want to delete?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
                notes.remove(position);
                adaptor.notifyDataSetChanged();
            }
        });
        AlertDialog dialog1 = dialog.create();
        dialog1.show();
        return true;
    }

    @Override public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {

        Note note = notes.get(i);
        position = i;

        Bundle bundle = new Bundle();
        bundle.putString(KEY_VALUE1, note.title);
        bundle.putString(KEY_VALUE2, note.description);
        bundle.putString(KEY_VALUE3, note.date);
        bundle.putString(KEY_VALUE4, note.time);
        bundle.putLong(ID, note.id);

        Intent intent = new Intent(this, Description.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String selectedItem = adapterView.getItemAtPosition(i).toString();
        Log.d("MainActivity.class", selectedItem);

        String query;
        if (selectedItem.equals("All")) {
            query = String.format("SELECT * FROM %s ORDER BY %s", Contract.NOTE.TABLE_NAME,
                Contract.NOTE.COLUMN_TITLE);
        } else {
            query =
                String.format("SELECT * FROM %s WHERE %s = \'%s\' ORDER BY %s", Contract.NOTE.TABLE_NAME,
                    Contract.NOTE.COLUMN_CATEGORY, selectedItem, Contract.NOTE.COLUMN_TITLE);
        }

        Log.d("MainActivity.class", query);
        Cursor cursor = database.rawQuery(query, null);
        notes.clear();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_TITLE));
            String description =
                cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_NOTE_TIME));
            String category = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_CATEGORY));
            long id = cursor.getLong(cursor.getColumnIndex(Contract.NOTE.COLUMN_ID));

            Note note = new Note(title, description, date, time,
                category); //array list me b save krana h taki show ho when we run the application
            note.setId(id);
            notes.add(note);
        }
        cursor.close();

        Log.d("MainActivity.class", query);

        // Added by mayank you forgot to do this.
        adaptor.notifyDataSetChanged();

        Log.d("MainActivity.class", query);
    }

    @Override public void onNothingSelected(AdapterView<?> adapterView) {
    }
}


