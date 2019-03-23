package eg.com.iti.mshwar.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import eg.com.iti.mshwar.beans.Trip;
import eg.com.iti.mshwar.util.Utils;

public class TripDao {

    Trip trip;


    public TripDao(){

    }

    public TripDao(Trip trip)
    {
        // TODO: What is this?!!
        insertDetailsOfTrip(trip);
    }


    public  void insertDetailsOfTrip(Trip trip) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference().child(Utils.TRIP_TABLE);
        String key = root.push().getKey();
        HashMap<String, Object> m = new HashMap<>();
        root.updateChildren(m);

        HashMap<String, Object> map = new HashMap<>();
        map.put(Utils.COLUMN_TRIP_NAME, trip.getName());
        map.put(Utils.COLUMN_TRIP_START_POINT, trip.getStartPoint());
        map.put(Utils.COLUMN_TRIP_START_POINT_LONGITUDE, trip.getStartPointLongitude());
        map.put(Utils.COLUMN_TRIP_START_POINT_LATITUDE, trip.getStartPointLatitude());
        map.put(Utils.COLUMN_TRIP_END_POINT_LONGITUDE, trip.getEndPointLongitude());
        map.put(Utils.COLUMN_TRIP_END_POINT_LATITUDE, trip.getEndPointLatitude());
        map.put(Utils.COLUMN_TRIP_END_POINT, trip.getEndPoint());
        map.put(Utils.COLUMN_TRIP_TRIP_TYPE, trip.getType());
        map.put(Utils.COLUMN_TRIP_REPETITION, trip.getRepetition());
        map.put(Utils.COLUMN_TRIP_Date, trip.getDate());
        map.put(Utils.COLUMN_TRIP_Time, trip.getTime());

        DatabaseReference databaseReference = root.child(key);
        databaseReference.updateChildren(map);

    }

}
