package eg.com.iti.mshwar.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.adapter.RecyclerAdapter;
import eg.com.iti.mshwar.dao.TripDaoImpl;
import eg.com.iti.mshwar.model.TripDao;
import eg.com.iti.mshwar.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private String status;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private LinearLayout empty;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setUpRecyclerView(view);
        empty = view.findViewById(R.id.layout_empty);
        recyclerView = view.findViewById(R.id.mainRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new RecyclerAdapter(getContext(), empty);

    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.setUpdatedData(new TripDaoImpl().getTripsFromFirebase(status, adapter));
        recyclerView.setAdapter(adapter);

    }

    private void setUpRecyclerView(View view) {

        LinearLayout empty = view.findViewById(R.id.layout_empty);
        RecyclerView recyclerView = view.findViewById(R.id.mainRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerAdapter adapter = new RecyclerAdapter(getContext(), empty);
        adapter.setUpdatedData(new TripDaoImpl().getTripsFromFirebase(status, adapter));
        recyclerView.setAdapter(adapter);
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

