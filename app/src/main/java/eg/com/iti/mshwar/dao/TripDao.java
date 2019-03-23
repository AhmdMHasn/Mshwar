package eg.com.iti.mshwar.dao;

import android.content.Context;
import java.util.List;
import eg.com.iti.mshwar.adapter.TripAdapter;
import eg.com.iti.mshwar.beans.TripBean;

public interface TripDao {

    String addTrip(TripBean tripBean);

    List<TripBean> getTripsFromFirebase(String tripStatus, TripAdapter adapter);

    boolean deleteTripFromFirebase(Context context, TripBean tripBean);

    void updateTripStatus(String tripKey, String updatedStatus);

    void startTrip(Context context, TripBean trip);

    void updateTripInfo(TripBean tripBean);
}
