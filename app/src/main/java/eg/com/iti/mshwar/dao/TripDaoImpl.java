package eg.com.iti.mshwar.dao;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.com.iti.mshwar.adapter.TripAdapter;
import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.service.MyReceiver;
import eg.com.iti.mshwar.util.Utils;

public class TripDaoImpl implements TripDao {

    public static final String TAG = "TripDao";
    public static DatabaseReference reference;
    public static FirebaseDatabase mDatabase;
    public static String currentUserId;
    List<TripBean> tripList;

    public TripDaoImpl() {
        getDatabase();
    }

    // To allow firebase to cache data
    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        reference = FirebaseDatabase.getInstance().getReference();

        return mDatabase;
    }

    @Override
    public String addTrip(TripBean tripBean) {

        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child(Utils.TRIP_TABLE);
        String key = root.push().getKey();
        HashMap<String, Object> m = new HashMap<>();
        root.updateChildren(m);

        HashMap<String, Object> map = new HashMap<>();

        map.put(Utils.COLUMN_TRIP_NAME, tripBean.getName());
        map.put(Utils.COLUMN_TRIP_START_POINT, tripBean.getStartPoint());
        map.put(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, tripBean.getStartPointLongitude());
        map.put(Utils.COLUMN_TRIP_START_POINT_LATITUDE, tripBean.getStartPointLatitude());
        map.put(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, tripBean.getEndPointLongitude());
        map.put(Utils.COLUMN_TRIP_END_POINT_LATITUDE, tripBean.getEndPointLatitude());
        map.put(Utils.COLUMN_TRIP_END_POINT, tripBean.getEndPoint());
        map.put(Utils.COLUMN_TRIP_TRIP_TYPE, tripBean.getType());
        map.put(Utils.COLUMN_TRIP_REPETITION, tripBean.getRepetition());
        map.put(Utils.COLUMN_TRIP_Date, tripBean.getDate());
        map.put(Utils.COLUMN_TRIP_Time, tripBean.getTime());
        map.put(Utils.COLUMN_TRIP_NOTES, tripBean.getNotes());
        map.put(Utils.COLUMN_TRIP_STATUS, tripBean.getStatus());
        map.put(Utils.COLUMN_TRIP_ALARM_ID, tripBean.getAlarmIds());
        map.put(Utils.COLUMN_TRIP_USER_ID, tripBean.getUserId());

        DatabaseReference databaseReference = root.child(key);
        databaseReference.updateChildren(map);

        return key;
    }

    //for updating the trip status to upcoming, done or cancelled
    public void updateTripStatus(String tripKey, String updatedStatus) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child(Utils.TRIP_TABLE);
        root.child(tripKey).child(Utils.COLUMN_TRIP_STATUS).setValue(updatedStatus);
    }

    /**
     * This great method :) is called to get data from firebase depending on the trip status
     * The data is added to the recycle view adapter to update the list
     */
    public List<TripBean> getTripsFromFirebase(final String tripStatus, final TripAdapter adapter) {
        tripList = new ArrayList<>();

        Query query = reference.child(Utils.TRIP_TABLE)
                .orderByChild(Utils.COLUMN_TRIP_USER_ID)
                .equalTo(currentUserId);

        query.keepSynced(true);

        Log.d(TAG, "Current User ID: " + currentUserId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            private String _tripStatus = tripStatus;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + "Test2");
                tripList.clear();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    TripBean trip = singleSnapshot.getValue(TripBean.class);
                    trip.setKey(singleSnapshot.getKey());
                    Log.d(TAG, "onDataChange: Key: " + trip.toString());
                    tripList.add(trip);
                }

                List<TripBean> tripListFiltered = new ArrayList<>();

                if (_tripStatus != null) {
                    switch (_tripStatus) {
                        case (Utils.UPCOMING):
                            for (int i = 0; i < tripList.size(); i++) {
                                if (tripList.get(i).getStatus().equalsIgnoreCase(Utils.UPCOMING)) {
                                    tripListFiltered.add(tripList.get(i));
                                }
                            }
                            break;

                        case (Utils.DONE):
                        case (Utils.CANCELLED):
                            for (int i = 0; i < tripList.size(); i++) {
                                if (!tripList.get(i).getStatus().equalsIgnoreCase(Utils.UPCOMING)) {
                                    tripListFiltered.add(tripList.get(i));
                                }
                            }
                            break;

                        default:
                            tripListFiltered = tripList;
                            break;
                    }
                    adapter.setUpdatedData(tripListFiltered);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

        return tripList;
    }

    public boolean deleteTripFromFirebase(Context context, TripBean tripBean) {
        // handle removing the alarm here .. if succeed return true ya Sallam
        for (String alarmId : tripBean.getAlarmIds()) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.valueOf(alarmId),
                    new Intent(context, MyReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);

            alarmManager.cancel(pendingIntent);
        }
        reference.child(Utils.TRIP_TABLE).child(tripBean.getKey()).removeValue();
        return true;
    }

    public void startTrip(Context context, TripBean trip) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="
                        + trip.getStartPointLatitude() + ","
                        + trip.getStartPointLongitude()
                        + "(" + trip.getStartPoint() + ")"
                        + "&daddr="
                        + trip.getEndPointLatitude() + ","
                        + trip.getStartPointLongitude()
                        + "(" + trip.getEndPoint() + ")"
                ));
        intent.putExtra(Utils.COLUMN_TRIP_NOTES, trip.getNotes());
        context.startActivity(intent);
        updateTripStatus(trip.getKey(), Utils.DONE);
    }

    @Override
    public void updateTripInfo(TripBean tripBean) {
        DatabaseReference ref = reference.child(Utils.TRIP_TABLE).child(tripBean.getKey());
        HashMap<String, Object> map = new HashMap<>();

        map.put(Utils.COLUMN_TRIP_NAME, tripBean.getName());
        map.put(Utils.COLUMN_TRIP_START_POINT, tripBean.getStartPoint());
        map.put(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, tripBean.getStartPointLongitude());
        map.put(Utils.COLUMN_TRIP_START_POINT_LATITUDE, tripBean.getStartPointLatitude());
        map.put(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, tripBean.getEndPointLongitude());
        map.put(Utils.COLUMN_TRIP_END_POINT_LATITUDE, tripBean.getEndPointLatitude());
        map.put(Utils.COLUMN_TRIP_END_POINT, tripBean.getEndPoint());
        map.put(Utils.COLUMN_TRIP_TRIP_TYPE, tripBean.getType());
        map.put(Utils.COLUMN_TRIP_REPETITION, tripBean.getRepetition());
        map.put(Utils.COLUMN_TRIP_Date, tripBean.getDate());
        map.put(Utils.COLUMN_TRIP_Time, tripBean.getTime());
        map.put(Utils.COLUMN_TRIP_NOTES, tripBean.getNotes());
        map.put(Utils.COLUMN_TRIP_STATUS, tripBean.getStatus());
        map.put(Utils.COLUMN_TRIP_ALARM_ID, tripBean.getAlarmIds());
        map.put(Utils.COLUMN_TRIP_USER_ID, tripBean.getUserId());

        ref.updateChildren(map);
    }


}