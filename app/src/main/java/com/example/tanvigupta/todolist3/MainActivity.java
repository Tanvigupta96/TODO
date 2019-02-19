package com.example.tanvigupta.todolist3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

import static com.example.tanvigupta.todolist3.R.drawable.*;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    ListView listView;
    Spinner spin;
    FloatingActionButton fab;
    android.support.v7.widget.Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;


    ArrayList<Note> notes = new ArrayList<>();
    TextView title, description, date, time;
    CustomAdaptor adaptor;
    public static final String KEY_VALUE1 = "title", KEY_VALUE2 = "description", KEY_VALUE3 = "date",
            KEY_VALUE4 = "time", KEY_VALUE5 = "category";
    public static final String ID = "id";
    private int position;
    //public long id;
    SQLiteDatabase database;

    String message;

    String selectedItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        spin = findViewById(R.id.spin1);
        fab = findViewById(R.id.add);
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout =  findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Notes");


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
        final ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);

        //drop-down layout style -listview with menu buttons
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(dataAdapter);

        //spinner click listener
        spin.setOnItemSelectedListener(this);

        //attaching data adapter to spinner
        //spin.setAdapter(dataAdapter);

        adaptor = new CustomAdaptor(this, notes, getLayoutInflater(), new TODOitemClickListenener() {
            @Override
            public void rowImageClicked(View view, final int position, final long id) {
                Note note = notes.get(position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("confirm delete!");
                dialog.setMessage("Are you sure you want to delete?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int i) {

                        notes.remove(position);
                        adaptor.notifyDataSetChanged();

                        Log.d("MainActivity",id+"");
                        String[] SelectionArgs={id+ ""};
                        Cursor cursor=database.query(Contract.NOTE.TABLE_NAME,null,Contract.NOTE.COLUMN_ID+ " = ? ",SelectionArgs,null,null,null);
                        database.delete(Contract.NOTE.TABLE_NAME,Contract.NOTE.COLUMN_ID+" = ? ",SelectionArgs);


                        cursor.close();




                    }
                });
                AlertDialog dialog1 = dialog.create();
                dialog1.show();

                }

            @Override
            public void onStarClicked(View view, int position, long id) {
                view.setBackgroundColor(Color.YELLOW);
            }


        });

        listView.setAdapter(adaptor);
        listView.setOnItemClickListener(this);
        fab.setOnClickListener(this);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                Intent intent1 = new Intent(this, PermissionSettings.class);
                startActivity(intent1);
                break;

                case R.id.title:
                sort(Contract.NOTE.COLUMN_TITLE);
                break;
            case R.id.desc:
                sort(Contract.NOTE.COLUMN_DESCRIPTION);
                break;
            case R.id.date:
                sort(Contract.NOTE.COLUMN_DATE);
                break;
            case R.id.time:
                sort(Contract.NOTE.COLUMN_NOTE_TIME);
                break;
            case R.id.feedback:
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri=Uri.parse("https://www.google.co.in/");
                intent.setData(uri);
                Toast.makeText(this,"You can send your feedback via mail",Toast.LENGTH_LONG).show();
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", resultCode + "");

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
                long id = data.getLongExtra(AddNoteActivity.ID, 0);

                Note note = new Note(title, description, date, time, category);
                note.setId(id);
                notes.add(note);
                adaptor.notifyDataSetChanged();


                break;

            case 5:
                //edit item
                Log.d("MainActivity", "edit result called");
                title = data.getStringExtra(Description.TITLE_KEY);
                description = data.getStringExtra(Description.DESCRIPTION_KEY);
                date = data.getStringExtra(Description.DATE_KEY);
                time = data.getStringExtra(Description.TIME_KEY);
                category = data.getStringExtra(Description.CATEGORY_KEY);
                id = data.getLongExtra(Description.ID_KEY, 0);
                Log.d("MainActivity.class", id + "");

                note = new Note(title, description, date, time, category);

                notes.remove(position);
                notes.add(position, note);

                contentValues.put(Contract.NOTE.COLUMN_TITLE, title);
                contentValues.put(Contract.NOTE.COLUMN_DESCRIPTION, description);
                contentValues.put(Contract.NOTE.COLUMN_DATE, date);
                contentValues.put(Contract.NOTE.COLUMN_NOTE_TIME, time);

                String[] values = {id + ""};

                database.update(Contract.NOTE.TABLE_NAME, contentValues, Contract.NOTE.COLUMN_ID + "= ?",
                        values);

                adaptor.notifyDataSetChanged();
                break;
        }
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, final View view, final int i, long l) {
        Snackbar snackbar = Snackbar.make(view, "View Description", Snackbar.LENGTH_LONG).setAction("VIEW", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = notes.get(i).getId();
                Intent intent = new Intent(MainActivity.this, Description.class);
                intent.putExtra(Description.ID_KEY, id);
                startActivityForResult(intent, 1);

            }
        });
        snackbar.show();


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        selectedItem = adapterView.getItemAtPosition(i).toString();
        Log.d("MainActivity.class", selectedItem);

        String query;
        if (selectedItem.equals("All")) {
            query = String.format("SELECT * FROM %s", Contract.NOTE.TABLE_NAME);
        } else {
            query =
                    String.format("SELECT * FROM %s WHERE %s = \'%s\'" , Contract.NOTE.TABLE_NAME,
                            Contract.NOTE.COLUMN_CATEGORY, selectedItem);
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
        // i forgot to do this
        adaptor.notifyDataSetChanged();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void sort(String orderbyarguement) {
        String query;
        if(selectedItem=="All"){
            query=String.format("SELECT * FROM %s order by %s",Contract.NOTE.TABLE_NAME,orderbyarguement);
        }else {
            query = String.format("SELECT * FROM %s where %s = \'%s\'order by %s", Contract.NOTE.TABLE_NAME, Contract.NOTE.COLUMN_CATEGORY, selectedItem, orderbyarguement);
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

            Note note = new Note(title, description, date, time, category); //array list me b save krana h taki show ho when we run the application
            note.setId(id);
            notes.add(note);
        }
        cursor.close();


        // i forgot to do this
        adaptor.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.add) {

            Intent intent = new Intent(this, AddNoteActivity.class);
            startActivityForResult(intent, 1);
        }


    }



}





