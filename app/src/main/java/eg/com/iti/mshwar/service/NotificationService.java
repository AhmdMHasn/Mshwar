package eg.com.iti.mshwar.service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.activity.DialogActivity;
import eg.com.iti.mshwar.beans.Trip;
import eg.com.iti.mshwar.util.Utils;

public class NotificationService extends IntentService {

    private NotificationManager alarmNotificationManager;

    public NotificationService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification(intent);
    }

    private void sendNotification(Intent intent) {
        Log.d("AlarmService", "Preparing to send notification...: ");
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Trip trip = new Trip();
        Intent dialogActivityIntent = new Intent(this, DialogActivity.class);

        trip.setKey(intent.getStringExtra("key"));
        trip.setName(intent.getStringExtra(Utils.COLUMN_TRIP_NAME));
        trip.setStartPoint(intent.getStringExtra(Utils.COLUMN_TRIP_START_POINT));
        trip.setEndPoint(intent.getStringExtra(Utils.COLUMN_TRIP_END_POINT));
        trip.setRepetition(intent.getStringExtra(Utils.COLUMN_TRIP_REPETITION));
        trip.setType(intent.getStringExtra(Utils.COLUMN_TRIP_TRIP_TYPE));
        trip.setStatus(intent.getStringExtra(Utils.COLUMN_TRIP_STATUS));

        trip.setStartPointLatitude(intent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, 0));
        trip.setStartPointLongitude(intent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, 0));
        trip.setEndPointLatitude(intent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, 0));
        trip.setEndPointLongitude(intent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, 0));

        trip.setNotes(intent.getStringArrayListExtra(Utils.COLUMN_TRIP_NOTES));
        trip.setAlarmIds(intent.getStringArrayListExtra(Utils.COLUMN_TRIP_ALARM_ID));
        trip.setTime(intent.getStringArrayListExtra(Utils.COLUMN_TRIP_Time));
        trip.setDate(intent.getStringArrayListExtra(Utils.COLUMN_TRIP_Date));

        dialogActivityIntent.putExtra("key", trip.getKey());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_NAME, trip.getName());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT, trip.getStartPoint());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT, trip.getEndPoint());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_REPETITION, trip.getRepetition());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_TRIP_TYPE, trip.getType());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_STATUS, trip.getStatus());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, trip.getStartPointLatitude());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, trip.getStartPointLongitude());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, trip.getEndPointLongitude());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, trip.getEndPointLatitude());

        dialogActivityIntent.putStringArrayListExtra(Utils.COLUMN_TRIP_NOTES, trip.getNotes());
        dialogActivityIntent.putStringArrayListExtra(Utils.COLUMN_TRIP_ALARM_ID, trip.getAlarmIds());
        dialogActivityIntent.putStringArrayListExtra(Utils.COLUMN_TRIP_Time, trip.getTime());
        dialogActivityIntent.putStringArrayListExtra(Utils.COLUMN_TRIP_Date, trip.getDate());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_USER_ID, trip.getUserId());
//        dialogActivityIntent.putExtra(Utils.TRIP_TABLE, trip);

        dialogActivityIntent.putExtra("ringtone", "stop");
        dialogActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                dialogActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String msg = "from " + trip.getStartPoint() + " to " + trip.getEndPoint();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("id",
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            alarmNotificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder alarmNotificationBuilder =
                    new NotificationCompat.Builder(this, "id")
                            .setContentTitle(trip.getName())
                            .setSmallIcon(R.drawable.logo)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                            .setContentText(msg).setAutoCancel(true).setOngoing(true);

            alarmNotificationBuilder.setContentIntent(contentIntent);
            alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
        }
        Log.d("AlarmService", "Notification sent.");
    }
}
