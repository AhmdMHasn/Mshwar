package eg.com.iti.mshwar.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.beans.User;
import eg.com.iti.mshwar.util.Utils;

import static android.support.constraint.Constraints.TAG;

public class TripDao {

    TripBean tripBean;


    public TripDao(){

    }

    public TripDao(TripBean tripBean)
    {
        // TODO: What is this?!!
        insertDetailsOfTrip(tripBean);
    }


    public  void insertDetailsOfTrip(TripBean tripBean) {
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


        DatabaseReference databaseReference = root.child(key);
        databaseReference.updateChildren(map);

    }

}
