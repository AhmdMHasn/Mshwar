package eg.com.iti.mshwar.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import eg.com.iti.mshwar.Beans.TripBean;
import eg.com.iti.mshwar.util.Utils;

public class TripDao {
    TripBean tripBean;
    public TripDao(TripBean tripBean)
    {
       insertDetailsOfTrip(tripBean);
    }
    public  void insertDetailsOfTrip(TripBean tripBean) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child(Utils.TRIP_TABLE);
        String key = root.push().getKey();
        HashMap<String, Object> m = new HashMap<>();
        root.updateChildren(m);

        HashMap<String, Object> map = new HashMap<>();
        map.put(Utils.COLUMN_TRIP_NAME, tripBean.getNameOfTrip());
        map.put(Utils.COLUMN_TRIP_START_POINT, tripBean.getStartPoint());
        map.put(Utils.COLUMN_TRIP_START_POINT_LONGITUDE,tripBean.getLangitutdeOfStartPoint());
        map.put(Utils.COLUMN_TRIP_START_POINT_LATITUDE,tripBean.getLatitudeOfStartPoint());
        map.put(Utils.COLUMN_TRIP_END_POINT_LONGITUDE,tripBean.getLangitutdeOfEndPoint());
        map.put(Utils.COLUMN_TRIP_END_POINT_LATITUDE,tripBean.getLatitudeOfEndPoint());
        map.put(Utils.COLUMN_TRIP_END_POINT,tripBean.getEndPoint());
        map.put(Utils.COLUMN_TRIP_TRIP_TYPE,tripBean.getTypeOFTrip());
        map.put(Utils.COLUMN_TRIP_REPETITION,tripBean.getRepetition());
        map.put(Utils.COLUMN_TRIP_Date,tripBean.getDate());
        map.put(Utils.COLUMN_TRIP_Time,tripBean.getTime());



        DatabaseReference databaseReference = root.child(key);
        databaseReference.updateChildren(map);

    }
}
