package eg.com.iti.mshwar.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.adapter.TeamSliderAdapter;
import eg.com.iti.mshwar.beans.TeamMember;

public class TeamActivity extends AppCompatActivity {

    List<TeamMember> mList = new ArrayList<>();
    ViewPager viewPager;
    TabLayout indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_team));

        viewPager = findViewById(R.id.teamViewPager);
        indicator = (TabLayout) findViewById(R.id.indicator);

        addTeamToList();

        viewPager.setAdapter(new TeamSliderAdapter(this, mList));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
    }

    private void addTeamToList() {
        TeamMember member1 = new TeamMember();
        member1.setName("Ahmed M. Hassan");
        member1.setImage(R.drawable.ahmdmhasn);
        member1.setUrlLinkedIn("https://www.linkedin.com/in/ahmdmhasn");
        member1.setUrlGitHub("https://github.com/AhmdMHasn");
        mList.add(member1);

        TeamMember member2 = new TeamMember();
        member2.setName("Ahmed Sallam");
        member2.setImage(R.drawable.nav_user);
        mList.add(member2);

        TeamMember member3 = new TeamMember();
        member3.setName("Ismail El-Mogy");
        member3.setImage(R.drawable.nav_user);
        mList.add(member3);

        TeamMember member4 = new TeamMember();
        member4.setName("Asmaa Fathy");
        member4.setImage(R.drawable.nav_user);
//        member4.setUrlLinkedIn("");
//        member4.setUrlGitHub("");
        mList.add(member4);

        Collections.shuffle(mList);
    }

    @Override // For action bar
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class SliderTimer extends TimerTask{

        @Override
        public void run() {
            TeamActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager.getCurrentItem() < mList.size() - 1){
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });
        }
    }
}
