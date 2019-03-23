package eg.com.iti.mshwar.dao;

import android.content.Context;
import java.util.List;
import eg.com.iti.mshwar.adapter.TripAdapter;
import eg.com.iti.mshwar.beans.Trip;

public interface TripDao {

    String addTrip(Trip trip);

    List<Trip> getTripsFromFirebase(String tripStatus, TripAdapter adapter);

    boolean deleteTripFromFirebase(Context context, Trip trip);

    void updateTripStatus(String tripKey, String updatedStatus);

    void startTrip(Context context, Trip trip);

    void updateTripInfo(Trip trip);
}
