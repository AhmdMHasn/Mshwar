package eg.com.iti.mshwar.dao;

import android.content.Context;
import java.util.List;
import eg.com.iti.mshwar.adapter.RecyclerAdapter;
import eg.com.iti.mshwar.beans.TripBean;

public interface TripDao {

    String addTrip(TripBean tripBean);

    List<TripBean> getTripsFromFirebase(String tripStatus, RecyclerAdapter adapter);

    boolean deleteTripFromFirebase(String key);

    void updateTripStatus(String tripKey, String updatedStatus);

    void startTrip(Context context, TripBean trip);
}
