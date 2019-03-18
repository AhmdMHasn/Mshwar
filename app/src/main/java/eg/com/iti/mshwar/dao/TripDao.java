package eg.com.iti.mshwar.dao;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import eg.com.iti.mshwar.adapter.RecyclerAdapter;
import eg.com.iti.mshwar.beans.TripBean;

public interface TripDao {

    // add trip

    public void addTrip(TripBean tripBean);

    public List<TripBean> getTripsFromFirebase(String tripStatus, RecyclerAdapter adapter);

    public boolean deleteTripFromFirebase(String key);
}
