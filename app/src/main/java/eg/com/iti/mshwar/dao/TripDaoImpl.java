package eg.com.iti.mshwar.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.util.Utils;

public class TripDaoImpl implements TripDao {

    @Override
    public void addTrip(TripBean tripBean) {

        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child(Utils.TRIP_TABLE);
        String key = root.push().getKey();
        HashMap<String, Object> m = new HashMap<>();
        root.updateChildren(m);

        HashMap<String, Object> map = new HashMap<>();
        map.put(Utils.COLUMN_TRIP_NAME, tripBean.getName());
        map.put(Utils.COLUMN_TRIP_START_POINT, tripBean.getStartPoint());
        map.put(Utils.COLUMN_TRIP_START_POINT_LONGITUDE,tripBean.getStartPointLongitude());
        map.put(Utils.COLUMN_TRIP_START_POINT_LATITUDE,tripBean.getStartPointLatitude());
        map.put(Utils.COLUMN_TRIP_END_POINT_LONGITUDE,tripBean.getEndPointLongitude());
        map.put(Utils.COLUMN_TRIP_END_POINT_LATITUDE,tripBean.getEndPointLatitude());
        map.put(Utils.COLUMN_TRIP_END_POINT,tripBean.getEndPoint());
        map.put(Utils.COLUMN_TRIP_TRIP_TYPE,tripBean.getType());
        map.put(Utils.COLUMN_TRIP_REPETITION,tripBean.getRepetition());
        map.put(Utils.COLUMN_TRIP_Date,tripBean.getDate());
        map.put(Utils.COLUMN_TRIP_Time,tripBean.getTime());
        map.put(Utils.COLUMN_TRIP_NOTES, tripBean.getNotes());
        map.put(Utils.COLUMN_TRIP_STATUS, tripBean.getStatus());
        map.put(Utils.COLUMN_TRIP_ALARM_ID, tripBean.getAlarmIds());
        map.put(Utils.COLUMN_TRIP_USER_ID, tripBean.getUserId());


        DatabaseReference databaseReference = root.child(key);
        databaseReference.updateChildren(map);

    }
}
