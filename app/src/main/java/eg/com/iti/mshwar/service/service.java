package eg.com.iti.mshwar.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.activity.DialogActivity;
import eg.com.iti.mshwar.util.Utils;

public class service extends IntentService {

    private NotificationManager alarmNotificationManager;

    public service() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification(intent.getStringExtra(Utils.COLUMN_TRIP_NAME), intent);
    }

    private void sendNotification(String msg, Intent intent) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

//        int id = intent.getExtras().getInt("id");
        Intent dialogActivityIntent = new Intent(this, DialogActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                dialogActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_NAME,
                intent.getStringExtra(Utils.COLUMN_TRIP_NAME));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT,
                intent.getStringExtra(Utils.COLUMN_TRIP_END_POINT));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT,
                intent.getStringExtra(Utils.COLUMN_TRIP_START_POINT));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE,
                intent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, 0));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE,
                intent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, 0));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE,
                intent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, 0));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE,
                intent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, 0));

        NotificationCompat.Builder alarmNotificationBuilder =
                new NotificationCompat.Builder(this, "Mshwar")
                        .setContentTitle(intent.getStringExtra(Utils.COLUMN_TRIP_NAME))
                        .setSmallIcon(R.drawable.logo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg).setAutoCancel(true).setOngoing(true);

        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}
