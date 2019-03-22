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

        TripBean tripBean = (TripBean) intent.getSerializableExtra(Utils.TRIP_TABLE);

        Intent dialogActivityIntent = new Intent(this, DialogActivity.class);
        dialogActivityIntent.putExtra(Utils.TRIP_TABLE, tripBean);
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
