package eg.com.iti.mshwar.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.adapter.ViewPagerAdapter;
import eg.com.iti.mshwar.fragment.MainFragment;
import eg.com.iti.mshwar.util.Utils;

public class TripsHistoryActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_history);

        tabLayout = findViewById(R.id.tab_layout_history);
        viewPager = findViewById(R.id.viewpager_trips_history);

        // Setup upper toolbar with title and back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Trips");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add fragment to adapter
        Fragment allTripsFrag = new MainFragment();
        ((MainFragment) allTripsFrag).setStatus(Utils.ALL);

        Fragment pastTripsFrag = new MainFragment();
        ((MainFragment) pastTripsFrag).setStatus(Utils.DONE);

        adapter.addFragment(pastTripsFrag, "Past");
        adapter.addFragment(allTripsFrag, "All Trips");

        // Adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
