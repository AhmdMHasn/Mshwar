package eg.com.iti.mshwar.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import eg.com.iti.mshwar.activity.DialogActivity;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //start the dialog activity
        Intent dialogActivityIntent = new Intent(context, DialogActivity.class);
        dialogActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialogActivityIntent);
        Log.i("Alarm", "alarm is ringing");
    }
}
