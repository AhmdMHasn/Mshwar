package eg.com.iti.mshwar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.fragment.MainTripsFragment;
import eg.com.iti.mshwar.util.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Fragments
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ConstraintLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFirebaseAuth();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_home));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTripActivity();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            TextView userEmail = headerView.findViewById(R.id.navUserEmail);
            TextView userName = headerView.findViewById(R.id.navUserName);

            String name = user.getDisplayName();
            String email = user.getEmail();

            userName.setText("Welcome, " + name);
            userEmail.setText(email);
        }

        // Add Main Fragment (trips recycler view)
        fragmentContainer = findViewById(R.id.layout_content_main);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null){
            Fragment fragment = new MainTripsFragment();
            ((MainTripsFragment) fragment).setStatus(Utils.UPCOMING);
            myFragmentTransaction(fragment, Utils.REPLACE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState(){
        Log.d(TAG, "checkAuthenticationState: checking authentication state.");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Log.d(TAG, "checkAuthenticationState: user is null, navigating back to login screen.");

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.optionSync:
                //Toast.makeText(this, "Process synchronization..", Toast.LENGTH_SHORT).show();
                // Handle sync here
                Toast.makeText(this, "Synchronization completed successfully.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.optionAddTrip:
                addTripActivity();
                return true;
            case R.id.optionSignOut:
                signOut();
                return true;
            case R.id.optionAccountSettings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Sign out the current user
     */
    private void signOut(){
        Log.d(TAG, "signOut: signing out");
        FirebaseAuth.getInstance().signOut();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.nav_home:
                // Handle all trips action
                break;

            case R.id.nav_trips_all:
                intent = new Intent(this, TripsHistoryActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_help:

                break;

            case R.id.nav_about_us:
                intent = new Intent(this, TeamActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_logout:
                signOut();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*----------------------------- Firebase setup ---------------------------------*/
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    /*----------------------------- Recycler Views Replacement ---------------------------------*/
    public void myFragmentTransaction(Fragment fragment, int type){
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (type){
            case Utils.ADD:
                fragmentTransaction.add(R.id.layout_content_main, fragment, "fragment");
                break;
            case Utils.REPLACE:
                fragmentTransaction.replace(R.id.layout_content_main, fragment, "fragment");
                break;
        }
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void addTripActivity() {
        Intent intent = new Intent(MainActivity.this,TripActivity.class);
        startActivity(intent);
    }

}
