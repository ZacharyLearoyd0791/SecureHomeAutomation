package ca.future.home.it.secure.home.automation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    DatabaseActivity databaseActivity = new DatabaseActivity();
    private static final int NOTIFICATION_ID = 123;
    private NotificationManager notificationManager;
    private Notification notification;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseActivity.Activity();

        createNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createNotification() {

        databaseActivity.AlertMode();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = getString(R.string.channelId);
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId, getString(R.string.channelName), NotificationManager.IMPORTANCE_HIGH);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }

        notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.AppName))
                .setContentText(getString(R.string.foreground_text))
                .setSmallIcon(R.drawable.app_icon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

}