package eg.com.iti.mshwar.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.dao.TripDaoImpl;
import eg.com.iti.mshwar.service.service;
import eg.com.iti.mshwar.util.Utils;

public class DialogActivity extends Activity {
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
}
