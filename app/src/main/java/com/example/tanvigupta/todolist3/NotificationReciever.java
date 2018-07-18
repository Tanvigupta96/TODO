package com.example.tanvigupta.todolist3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;


public class NotificationReciever extends BroadcastReceiver {
    int i=1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        boolean isreminderenaled = sharedPreferences.getBoolean(Constants.IS_REMINDERS_ENABLE, false);
        if (isreminderenaled) {


            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            ++i;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channelid", "mychannel", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelid");
            builder.setContentTitle("New Notification");
            builder.setContentText("Time to check your TODO " + i);
            builder.setSmallIcon(R.drawable.notificationicon);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.pencil));
            builder.setColor(context.getResources().getColor(R.color.colorPrimary));
            Intent activityIntent = new Intent(context, Description.class);

            long id = intent.getLongExtra(AddNoteActivity.ID, 0);

            activityIntent.putExtra(Description.ID_KEY, id);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            manager.notify(1, notification);
        }
    }
}
