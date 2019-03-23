package eg.com.iti.mshwar.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import eg.com.iti.mshwar.service.NoteHeadService;

import eg.com.iti.mshwar.beans.Trip;
import eg.com.iti.mshwar.dao.TripDaoImpl;
import eg.com.iti.mshwar.service.NotificationService;
import eg.com.iti.mshwar.util.Utils;

public class DialogActivity extends Activity {
    Intent receivedIntent;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    Trip trip;
    Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dialog);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);

        receivedIntent = getIntent();
        if (receivedIntent.getStringExtra("ringtone") == null)
            ringtone.play();

        trip = new Trip();

        trip.setKey(receivedIntent.getStringExtra("key"));
        trip.setName(receivedIntent.getStringExtra(Utils.COLUMN_TRIP_NAME));
        trip.setStartPoint(receivedIntent.getStringExtra(Utils.COLUMN_TRIP_START_POINT));
        trip.setEndPoint(receivedIntent.getStringExtra(Utils.COLUMN_TRIP_END_POINT));
        trip.setRepetition(receivedIntent.getStringExtra(Utils.COLUMN_TRIP_REPETITION));
        trip.setType(receivedIntent.getStringExtra(Utils.COLUMN_TRIP_TRIP_TYPE));
        trip.setStatus(receivedIntent.getStringExtra(Utils.COLUMN_TRIP_STATUS));

        trip.setStartPointLatitude(receivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, 0));
        trip.setStartPointLongitude(receivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, 0));
        trip.setEndPointLatitude(receivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, 0));
        trip.setEndPointLongitude(receivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, 0));

        trip.setNotes(receivedIntent.getStringArrayListExtra(Utils.COLUMN_TRIP_NOTES));
        trip.setAlarmIds(receivedIntent.getStringArrayListExtra(Utils.COLUMN_TRIP_ALARM_ID));
        trip.setTime(receivedIntent.getStringArrayListExtra(Utils.COLUMN_TRIP_Time));
        trip.setDate(receivedIntent.getStringArrayListExtra(Utils.COLUMN_TRIP_Date));

        String title = trip.getName();
        String msg = "It's time for your trip from " + trip.getStartPoint()
                + " to " + trip.getEndPoint();

        final String key = trip.getKey();
        final TripDaoImpl dao = new TripDaoImpl();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setTitle(title)
                .setPositiveButton("start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                                !Settings.canDrawOverlays(getApplicationContext())) {

                            //If the draw over permission is not available open the settings screen
                            //to grant the permission.
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            intent.putExtra(Utils.COLUMN_TRIP_NOTES, trip.getNotes());
                            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                        } else {
                            showNoteHead();
                            dao.startTrip(DialogActivity.this, trip);
                        }

                    }
                })
                .setNeutralButton("later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(DialogActivity.this, NotificationService.class);
//                        intent.putExtra(Utils.TRIP_TABLE, trip);
                        intent.putExtra("key", trip.getKey());
                        intent.putExtra(Utils.COLUMN_TRIP_NAME, trip.getName());
                        intent.putExtra(Utils.COLUMN_TRIP_START_POINT, trip.getStartPoint());
                        intent.putExtra(Utils.COLUMN_TRIP_END_POINT, trip.getEndPoint());
                        intent.putExtra(Utils.COLUMN_TRIP_REPETITION, trip.getRepetition());
                        intent.putExtra(Utils.COLUMN_TRIP_TRIP_TYPE, trip.getType());
                        intent.putExtra(Utils.COLUMN_TRIP_STATUS, trip.getStatus());
                        intent.putExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, trip.getStartPointLatitude());
                        intent.putExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, trip.getStartPointLongitude());
                        intent.putExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, trip.getEndPointLongitude());
                        intent.putExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, trip.getEndPointLatitude());

                        intent.putExtra(Utils.COLUMN_TRIP_NOTES, trip.getNotes());
                        intent.putExtra(Utils.COLUMN_TRIP_ALARM_ID, trip.getAlarmIds());
                        intent.putExtra(Utils.COLUMN_TRIP_Time, trip.getTime());
                        intent.putExtra(Utils.COLUMN_TRIP_Date, trip.getDate());
                        intent.putExtra(Utils.COLUMN_TRIP_USER_ID, trip.getUserId());
                        startService(intent);
                        finish();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.updateTripStatus(key, Utils.CANCELLED);
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (ringtone.isPlaying())
                    ringtone.stop();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ringtone.isPlaying())
            ringtone.stop();
    }

    private void showNoteHead() {
        Intent serviceIntent = new Intent(DialogActivity.this, NoteHeadService.class);

        if (trip.getNotes() != null) {
            serviceIntent.putExtra(Utils.COLUMN_TRIP_NOTES, trip.getNotes());
            serviceIntent.putExtra("key", trip.getKey());
        }
//        Toast.makeText(DialogActivity.this, trip.getNotes().get(0), Toast.LENGTH_LONG).show();
        startService(serviceIntent);

    }

    public void openMap() {

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="
                        + receivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, 0) + ","
                        + receivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, 0)
                        + "(" + receivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT, 0) + ")"
                        + "&daddr="
                        + receivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, 0) + ","
                        + receivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, 0)
                        + "(" + receivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT, 0) + ")"
                ));

        startActivity(intent);
        DialogActivity.this.finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            // Settings activity never returns proper value so instead check with following method
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    showNoteHead();
                    openMap();
                } else { //Permission is not available
                    Toast.makeText(this,
                            "Draw over other app permission not available. Closing the application",
                            Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
