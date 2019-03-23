package eg.com.iti.mshwar.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.adapter.NoteMapAdapter;
import eg.com.iti.mshwar.util.Utils;

public class NoteHeadService extends Service {

    private WindowManager mWindowManager;
    private View mChatHeadView;
    private RecyclerView mRecyclerView;
    private ArrayList<String> notesArrayList = new ArrayList<>();

    public NoteHeadService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String key = intent.getStringExtra("key");
        notesArrayList = intent.getStringArrayListExtra(Utils.COLUMN_TRIP_NOTES);
        if (notesArrayList != null) {
            NoteMapAdapter noteAdapter = new NoteMapAdapter(notesArrayList);
//        NoteMapAdapter noteAdapter = new NoteMapAdapter(notes);
            mRecyclerView = mChatHeadView.findViewById(R.id.note_recycle_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(noteAdapter);
        }
        Log.e("gggggggggggggg", intent.getStringArrayListExtra(Utils.COLUMN_TRIP_NOTES).get(0));
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the chat head layout we created
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.layout_note_head, null);

        //Add the view to the window.
//        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

//        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                LAYOUT_FLAG,
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//                PixelFormat.TRANSLUCENT);

        //Specify the chat head position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatHeadView, params);

        //Set the close button.
        ImageView closeButton = (ImageView) mChatHeadView.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the service and remove the chat head from the window
                stopSelf();
            }
        });

//        ArrayList<String> notes = new ArrayList<String>() {{
//            add("hello from the other side");
//            add("i wish i not call you thouthent time");
//            add("i wish i not call you thouthent time");
//            add("i wish i not call you thouthent time");
//            add("i wish i not call you thouthent time");
//            add("i wish i not call you thouthent time");
//            add("i wish i not call you thouthent time");
//            add("i wish i not call you thouthent time");
//            add("i wish i not call you thouthent time");
//        }};




        //Drag and move chat head using user's touch action.
        final ImageView chatHeadImage = mChatHeadView.findViewById(R.id.chat_head_profile_iv);
        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            //Open the chat conversation click.
                            /*
                            Intent intent = new Intent(NoteHeadService.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            */
                            //TODO get note array and show it
                            mRecyclerView.setVisibility(View.GONE);


                        }
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mChatHeadView, params);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
    }
}