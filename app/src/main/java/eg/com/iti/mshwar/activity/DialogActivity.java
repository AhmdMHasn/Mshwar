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

import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.dao.TripDaoImpl;
import eg.com.iti.mshwar.service.service;
import eg.com.iti.mshwar.util.Utils;

public class DialogActivity extends Activity {
    Intent ReceivedIntent;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    TripBean tripBean;
    Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dialog);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);

        Intent receivedIntent = getIntent();
        if (receivedIntent.getStringExtra("ringtone") == null)
            ringtone.play();

        tripBean = (TripBean) receivedIntent.getSerializableExtra(Utils.TRIP_TABLE);
        String title = tripBean.getName();
        String msg = "It's time for your trip from " + tripBean.getStartPoint()
                + " to " + tripBean.getEndPoint();

        final String key = tripBean.getKey();
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
                            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                        } else {
                            showNoteHead();
                            openMap();

                        }




                        dao.startTrip(DialogActivity.this, tripBean);
                        DialogActivity.this.finish();
                    }
                })
                .setNeutralButton("later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(DialogActivity.this, service.class);
                        intent.putExtra(Utils.TRIP_TABLE, tripBean);

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

        startService(new Intent(DialogActivity.this, NoteHeadService.class));
        finish();

    }

    public void openMap(){

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="
                        +ReceivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, 0)+ ","
                        +ReceivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, 0)
                        +"(" + ReceivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT, 0) +")"
                        +"&daddr="
                        +ReceivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, 0)+ ","
                        +ReceivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, 0)
                        +"(" + ReceivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT, 0) +")"
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
