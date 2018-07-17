package com.example.tanvigupta.todolist3;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyReceiver extends BroadcastReceiver {
    String message;
    String senderNum;
    String msgTime;
    String msgDate;

    @Override public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i("my receiver", "Started");
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                /*for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();
                    String format = "HH:mm";
                    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                    msgTime = sdf.format(Calendar.getInstance().getTime());
                    msgDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                }*/

                // No need of the for loop bcoz we only need the last sms received
                SmsMessage currentMessage =
                    SmsMessage.createFromPdu((byte[]) pdusObj[pdusObj.length - 1]);
                String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                senderNum = phoneNumber;
                message = currentMessage.getDisplayMessageBody();
                String format = "HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                msgTime = sdf.format(Calendar.getInstance().getTime());
                msgDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }

        if (senderNum != null && message != null && msgDate != null && msgDate != null && msgTime != null) {

            String title = senderNum;
            String description = message;
            String date = msgDate;
            String time = msgTime;
            String category = "Others";
            add(title, description, date, time, category, context);
        }
    }

    public void add(String title, String description, String date, String time, String category,
        Context context) {
        Log.d("MyReceiver", "add: ");
        // No need for this note thing
        Note note = new Note(title, description, date, time, category);

        NoteOpenHelper openHelper = NoteOpenHelper.getInstance(context.getApplicationContext());
        SQLiteDatabase database = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.NOTE.COLUMN_TITLE, title);
        contentValues.put(Contract.NOTE.COLUMN_DESCRIPTION, description);
        contentValues.put(Contract.NOTE.COLUMN_DATE, date);
        contentValues.put(Contract.NOTE.COLUMN_NOTE_TIME, time);
        contentValues.put(Contract.NOTE.COLUMN_CATEGORY, category);

        database.insert(Contract.NOTE.TABLE_NAME, null, contentValues);
    }
}


