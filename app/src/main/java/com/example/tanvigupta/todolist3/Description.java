package com.example.tanvigupta.todolist3;

import android.Manifest;
import android.content.Intent;
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
    public static final String TITLE_KEY = "title", DESCRIPTION_KEY = "description", DATE_KEY = "date", TIME_KEY = "time",CATEGORY_KEY= "category",ID_KEY="id";

    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        txt1 = findViewById(R.id.title);
        txt2 = findViewById(R.id.description);
        txt3 = findViewById(R.id.date);
        txt4 = findViewById(R.id.time);

        Intent intent1 = getIntent();
        String title = intent1.getStringExtra(MainActivity.KEY_VALUE1);
        String description = intent1.getStringExtra(MainActivity.KEY_VALUE2);
        String date = intent1.getStringExtra(MainActivity.KEY_VALUE3);
        String time = intent1.getStringExtra(MainActivity.KEY_VALUE4);
        String category= intent1.getStringExtra(MainActivity.KEY_VALUE5);
        id=intent1.getLongExtra(MainActivity.ID,0);


        txt1.setText(title);
        txt2.setText(description);
        txt3.setText(date);
        txt4.setText(time);
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
                bundle.putLong("id",this.id);

                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
                break;



        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        String title = data.getStringExtra(EditActivity.TITLE_KEY);
        String description = data.getStringExtra(EditActivity.DESCRIPTION_KEY);
        String date = data.getStringExtra(EditActivity.DATE_KEY);
        String time = data.getStringExtra(EditActivity.TIME_KEY);

        txt1.setText(title);
        txt2.setText(description);
        txt3.setText(date);
        txt4.setText(time);
        data.putExtra(ID_KEY,id);

        setResult(5, data);
    }

}



