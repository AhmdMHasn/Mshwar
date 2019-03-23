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
import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.util.Utils;

public class service extends IntentService {

    private NotificationManager alarmNotificationManager;

    public service() {
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

        TripBean tripBean = new TripBean();
        Intent dialogActivityIntent = new Intent(this, DialogActivity.class);

        tripBean.setKey(intent.getStringExtra("key"));
        tripBean.setName(intent.getStringExtra(Utils.COLUMN_TRIP_NAME));
        tripBean.setStartPoint(intent.getStringExtra(Utils.COLUMN_TRIP_START_POINT));
        tripBean.setEndPoint(intent.getStringExtra(Utils.COLUMN_TRIP_END_POINT));
        tripBean.setRepetition(intent.getStringExtra(Utils.COLUMN_TRIP_REPETITION));
        tripBean.setType(intent.getStringExtra(Utils.COLUMN_TRIP_TRIP_TYPE));
        tripBean.setStatus(intent.getStringExtra(Utils.COLUMN_TRIP_STATUS));

        tripBean.setStartPointLatitude(intent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, 0));
        tripBean.setStartPointLongitude(intent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, 0));
        tripBean.setEndPointLatitude(intent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, 0));
        tripBean.setEndPointLongitude(intent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, 0));

        tripBean.setNotes(intent.getStringArrayListExtra(Utils.COLUMN_TRIP_NOTES));
        tripBean.setAlarmIds(intent.getStringArrayListExtra(Utils.COLUMN_TRIP_ALARM_ID));
        tripBean.setTime(intent.getStringArrayListExtra(Utils.COLUMN_TRIP_Time));
        tripBean.setDate(intent.getStringArrayListExtra(Utils.COLUMN_TRIP_Date));

        dialogActivityIntent.putExtra("key", tripBean.getKey());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_NAME, tripBean.getName());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT, tripBean.getStartPoint());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT, tripBean.getEndPoint());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_REPETITION, tripBean.getRepetition());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_TRIP_TYPE, tripBean.getType());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_STATUS, tripBean.getStatus());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, tripBean.getStartPointLatitude());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, tripBean.getStartPointLongitude());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, tripBean.getEndPointLongitude());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, tripBean.getEndPointLatitude());

        dialogActivityIntent.putStringArrayListExtra(Utils.COLUMN_TRIP_NOTES, tripBean.getNotes());
        dialogActivityIntent.putStringArrayListExtra(Utils.COLUMN_TRIP_ALARM_ID, tripBean.getAlarmIds());
        dialogActivityIntent.putStringArrayListExtra(Utils.COLUMN_TRIP_Time, tripBean.getTime());
        dialogActivityIntent.putStringArrayListExtra(Utils.COLUMN_TRIP_Date, tripBean.getDate());
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_USER_ID, tripBean.getUserId());
//        dialogActivityIntent.putExtra(Utils.TRIP_TABLE, tripBean);

        dialogActivityIntent.putExtra("ringtone", "stop");
        dialogActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                dialogActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String msg = "from " + tripBean.getStartPoint() + " to " + tripBean.getEndPoint();

        NotificationCompat.Builder alarmNotificationBuilder =
                new NotificationCompat.Builder(this, "")
                        .setContentTitle(tripBean.getName())
                        .setSmallIcon(R.drawable.logo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg).setAutoCancel(true).setOngoing(true);

        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}
