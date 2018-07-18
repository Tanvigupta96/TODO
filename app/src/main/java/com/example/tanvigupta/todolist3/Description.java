package com.example.tanvigupta.todolist3;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


public class Description extends AppCompatActivity {
    TextView txt1, txt2, txt3, txt4;
    public static final String TITLE_KEY = "title", DESCRIPTION_KEY = "description", DATE_KEY = "date", TIME_KEY = "time", CATEGORY_KEY = "category", ID_KEY = "id";

    long id,id1;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        txt1 = findViewById(R.id.title);
        txt2 = findViewById(R.id.description);
        txt3 = findViewById(R.id.date);
        txt4 = findViewById(R.id.time);

        NoteOpenHelper helper = NoteOpenHelper.getInstance(this);
        database = helper.getReadableDatabase();

        Intent intent1 = getIntent();
        id = intent1.getLongExtra(ID_KEY, 0);
        loadAndSetNote(id);

        Intent intent2=getIntent();
        id1=intent2.getLongExtra(MyReceiver.ID_KEY2,0);
        loadandsetNoteformessage(id1);



    }

    private void loadAndSetNote(long id) {

        Cursor cursor = database.rawQuery(String.format("select * from %s where %s = %s", Contract.NOTE.TABLE_NAME, Contract.NOTE.COLUMN_ID, id), null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_NOTE_TIME));
            txt1.setText(title);
            txt2.setText(description);
            txt3.setText(date);
            txt4.setText(time);
        }
        cursor.close();
    }


    private void loadandsetNoteformessage(long id) {

        Cursor cursor = database.rawQuery(String.format("select * from %s where %s = %s", Contract.NOTE.TABLE_NAME, Contract.NOTE.COLUMN_ID, id), null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndex(Contract.NOTE.COLUMN_NOTE_TIME));
            txt1.setText(title);
            txt2.setText(description);
            txt3.setText(date);
            txt4.setText(time);
        }
        cursor.close();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        id = intent.getLongExtra(ID_KEY, 0);
        loadAndSetNote(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit:
                txt1 = findViewById(R.id.title);
                txt2 = findViewById(R.id.description);
                txt3 = findViewById(R.id.date);
                txt4 = findViewById(R.id.time);

                String title = txt1.getText().toString();
                String description = txt2.getText().toString();
                String date = txt3.getText().toString();
                String time = txt4.getText().toString();


                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("description", description);
                bundle.putString("date", date);
                bundle.putString("time", time);
                bundle.putLong("id", this.id);

                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtras(bundle);
                Log.d("Description", "start activity for result");
                startActivityForResult(intent, 2);
                break;


        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("Description.class", "getting called");


        String title = data.getStringExtra(EditActivity.TITLE_KEY);
        String description = data.getStringExtra(EditActivity.DESCRIPTION_KEY);
        String date = data.getStringExtra(EditActivity.DATE_KEY);
        String time = data.getStringExtra(EditActivity.TIME_KEY);

        txt1.setText(title);
        txt2.setText(description);
        txt3.setText(date);
        txt4.setText(time);
        data.putExtra(ID_KEY, id);
        Log.d("Description.class", id + "");

        setResult(5, data);
    }

}



