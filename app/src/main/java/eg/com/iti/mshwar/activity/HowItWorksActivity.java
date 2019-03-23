package eg.com.iti.mshwar.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.adapter.HowItWorksAdapter;
import eg.com.iti.mshwar.adapter.TeamSliderAdapter;
import eg.com.iti.mshwar.beans.TeamMember;

public class HowItWorksActivity extends AppCompatActivity {

    List<Integer> mList = new ArrayList<>();
    ViewPager viewPager;
    TabLayout indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_works);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_how_it_works));

        viewPager = findViewById(R.id.teamViewPager);
        indicator = (TabLayout) findViewById(R.id.indicator);

        addImagesToList();

        viewPager.setAdapter(new HowItWorksAdapter(this, mList));
        indicator.setupWithViewPager(viewPager, true);

    }

    private void addImagesToList() {

        mList.add(R.drawable.srcshot00);
        mList.add(R.drawable.srcshot01);
        mList.add(R.drawable.srcshot02);
        mList.add(R.drawable.srcshot03);
        mList.add(R.drawable.srcshot04);
        mList.add(R.drawable.srcshot05);

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
