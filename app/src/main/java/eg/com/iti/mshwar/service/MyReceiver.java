package eg.com.iti.mshwar.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import eg.com.iti.mshwar.activity.DialogActivity;
import eg.com.iti.mshwar.util.Utils;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //start the dialog activity
        Intent dialogActivityIntent = new Intent(context, DialogActivity.class);

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_NAME, intent.getStringExtra(Utils.COLUMN_TRIP_NAME));
        dialogActivityIntent.putExtra("key", intent.getStringExtra("key"));
        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_USER_ID,
                intent.getStringExtra(Utils.COLUMN_TRIP_USER_ID));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT,
                intent.getStringExtra(Utils.COLUMN_TRIP_START_POINT));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT,
                intent.getStringExtra(Utils.COLUMN_TRIP_END_POINT));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_REPETITION,
                intent.getStringExtra(Utils.COLUMN_TRIP_REPETITION));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_TRIP_TYPE,
                intent.getStringExtra(Utils.COLUMN_TRIP_TRIP_TYPE));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_STATUS,
                intent.getStringExtra(Utils.COLUMN_TRIP_STATUS));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE,
                intent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, 0));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE,
                intent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, 0));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE,
                intent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, 0));

        dialogActivityIntent.putExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE,
                intent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, 0));

        dialogActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialogActivityIntent);
        Log.i("Alarm", "alarm is ringing");
    }
}
