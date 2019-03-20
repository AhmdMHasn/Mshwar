package eg.com.iti.mshwar.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import eg.com.iti.mshwar.service.service;
import eg.com.iti.mshwar.util.Utils;

public class DialogActivity extends Activity {
    Intent ReceivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dialog);
        ReceivedIntent = getIntent();
        String title = ReceivedIntent.getStringExtra(Utils.COLUMN_TRIP_NAME);
        String msg = "It's time for your trip from " + ReceivedIntent.getStringExtra(Utils.COLUMN_TRIP_START_POINT)
                + " to " + ReceivedIntent.getStringExtra(Utils.COLUMN_TRIP_END_POINT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg)
                .setTitle(title)
                .setPositiveButton("start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
                })
                .setNeutralButton("later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(DialogActivity.this, service.class);
                        intent.putExtra(Utils.COLUMN_TRIP_NAME,
                                ReceivedIntent.getStringExtra(Utils.COLUMN_TRIP_NAME));

                        intent.putExtra(Utils.COLUMN_TRIP_END_POINT,
                                ReceivedIntent.getStringExtra(Utils.COLUMN_TRIP_END_POINT));

                        intent.putExtra(Utils.COLUMN_TRIP_START_POINT,
                                ReceivedIntent.getStringExtra(Utils.COLUMN_TRIP_START_POINT));

                        intent.putExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE,
                                ReceivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LATITUDE, 0));

                        intent.putExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE,
                                ReceivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, 0));

                        intent.putExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE,
                                ReceivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LATITUDE, 0));

                        intent.putExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE,
                                ReceivedIntent.getDoubleExtra(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, 0));

                        startService(intent);
                        finish();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO: cancel the trip
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
