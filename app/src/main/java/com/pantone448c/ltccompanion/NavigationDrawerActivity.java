package com.pantone448c.ltccompanion;

import android.os.Bundle;

import android.view.MenuItem;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.transit.realtime.GtfsRealtime;
import com.pantone448c.ltccompanion.GTFSData.GTFSStaticData;
import com.pantone448c.ltccompanion.GTFSData.LTCLiveFeed;
import com.pantone448c.ltccompanion.ui.directions.RouteBuilder;
import com.pantone448c.ltccompanion.ui.mapbox.MapBoxFragment;
import com.pantone448c.ltccompanion.ui.savedstops.SavedStopsFragment;
import com.pantone448c.ltccompanion.viewmodels.StopViewModel;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private StopViewModel stopViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GTFSStaticData.initContext(this.getApplication());
        RouteBuilder.initRetroFit();
        setContentView(R.layout.activity_navigation_drawer);

        //StopViewModel
        SavedStopsFragment.stopViewModel = new ViewModelProvider(this).get(StopViewModel.class);

        //Navigation Bar Configuration
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_map_box, R.id.nav_routes, R.id.nav_directions, R.id.nav_savedstops)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager manager;
        MapBoxFragment frag;
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(this, "Bazinga", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_resetCameraPos:
                // User chose the "Reset camera position" action,
                // reset the camera...
                manager = getSupportFragmentManager();
                frag = (MapBoxFragment) manager.findFragmentById(R.id.nav_map_box);
                frag.ResetCameraPosition(MapBoxFragment.lastDeviceLocation, true);
                return true;

            case R.id.action_centerCameraLondon:
                manager = getSupportFragmentManager();
                frag = (MapBoxFragment) manager.findFragmentById(R.id.nav_map_box);
                frag.ResetCameraPosition(MapBoxFragment.LONDON_COORDS, true);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
