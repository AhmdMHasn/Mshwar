package eg.com.iti.mshwar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import eg.com.iti.mshwar.service.service;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("It's time for your trip")
                .setTitle("trip name")
                .setPositiveButton("start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO: go to google maps app.
                        Uri gmmIntentUri = Uri.parse("geo:30.7749, 31.4194");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        DialogActivity.this.startActivity(mapIntent);
                        DialogActivity.this.finish();
                    }
                })
                .setNeutralButton("later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO: start a service that creates a notification
                        Intent intent = new Intent(DialogActivity.this, service.class);
                        startService(intent);
                        finish();
                    }
                })
                .setNegativeButton("cancel trip", new DialogInterface.OnClickListener() {
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
