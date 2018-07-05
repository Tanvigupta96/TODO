package com.example.tanvigupta.todolist3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteOpenHelper extends SQLiteOpenHelper {
    public final static int VERSION=1;
    public final static String DATABASE_NAME="Note_db";

    private static NoteOpenHelper instance;

    public static NoteOpenHelper getInstance(Context context){
        if(instance == null){
            instance = new NoteOpenHelper(context.getApplicationContext());
        }
        return instance;
    }


    public NoteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String NoteSql = "CREATE TABLE "+ Contract.NOTE.TABLE_NAME + "( " +
                Contract.NOTE.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                Contract.NOTE.COLUMN_TITLE +" TEXT , " +
                Contract.NOTE.COLUMN_DESCRIPTION + " TEXT , " +
                Contract.NOTE.COLUMN_DATE + " TEXT , " +
                Contract.NOTE.COLUMN_CATEGORY + " TEXT , " +
                Contract.NOTE.COLUMN_NOTE_TIME + " TEXT )";
        sqLiteDatabase.execSQL(NoteSql);
        Log.d("NoteOpenHelper.class",NoteSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
