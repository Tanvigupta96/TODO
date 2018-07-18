package com.example.tanvigupta.todolist3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class MyReceiver extends BroadcastReceiver {
    String message;
    String senderNum;
    String msgTime;
    String msgDate;
    int i;
    long id;

    public static final String ID_KEY2 = "id";


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i("my receiver", "Started");
        final Bundle bundle = intent.getExtras();

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        boolean isSmsReadEnabled = sharedPreferences.getBoolean(Constants.IS_SMS_READ_ENABLE, false);
        if (isSmsReadEnabled) {
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

        // This code is only here to show notification when you receive an sms
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        ++i;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channelid1", "mychannel1", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelid1");
        builder.setContentTitle("New Notification");
        builder.setContentText("Sms Recieved"+i);
        builder.setSmallIcon(R.drawable.notificationicon);

        Intent activityIntent = new Intent(context, Description.class);



        activityIntent.putExtra(ID_KEY2, id);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        manager.notify(1, notification);
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

         id = database.insert(Contract.NOTE.TABLE_NAME, null, contentValues);


         }


}


