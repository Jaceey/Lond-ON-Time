package com.pantone448c.ltccompanion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.transit.realtime.GtfsRealtime;
import com.pantone448c.ltccompanion.viewmodels.StopViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StopViewModel stopVM;
    private List<Stop> tempstops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GTFSStaticData.initContext(this.getApplication());
        Routes.LoadRoutes(GTFSStaticData.getRoutes());
        stopVM = new ViewModelProvider(this).get(StopViewModel.class);
        stopVM.getStops().observe( this, new Observer<List<Stop>>(){
                    @Override
                    public void onChanged(@Nullable final List<Stop> stops)
                    {
                        tempstops = stops;
                    }
        }
        );
    }


    public void download(View view) {
        //GtfsRealtime.TripUpdate[] test = LTCLiveFeed.Instance().getTripUpdates(3963);
        //GtfsRealtime.VehiclePosition[] test2 = LTCLiveFeed.Instance().getVehiclePositions(3963);
        //GtfsRealtime.Alert[] test3 = LTCLiveFeed.Instance().getAlerts();

        Stop[] stops = GTFSStaticData.getStops(3980, 0);
        Toast myToast = Toast.makeText(this, "Well it didn't crash so that's a start!", Toast.LENGTH_LONG);
        myToast.show();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    URL url = new URL("http://gtfs.ltconline.ca/Vehicle/VehiclePositions.pb");
                    URL url2 = new URL("http://gtfs.ltconline.ca/Alert/Alerts.pb");
                    URL url3 = new URL("http://gtfs.ltconline.ca/TripUpdate/TripUpdates.pb");
                    GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream());
                    GtfsRealtime.FeedMessage feed2 = GtfsRealtime.FeedMessage.parseFrom(url2.openStream());
                    GtfsRealtime.FeedMessage feed3 = GtfsRealtime.FeedMessage.parseFrom(url3.openStream());
                    GtfsRealtime.FeedEntity test;

                }
                catch (Exception ex)
                {

                }
            }
        }).start();*/
    }

    public void addStops(View view) {
        /*
        Stop stop = new Stop(598, 22, "Adelaide at Central  NB - #22", 42.994567f, -81.234344f, 0);
        Stop stop2 = new Stop(599, 35, "blah not a real stop", 53.213311f, 24.312331f, 0 );
        Stop stop3 = new Stop(13, 2, "Shas", 53.13345f, 12.512f, 0);
        stopVM.insertStop(stop);
        stopVM.insertStop(stop2);
        stopVM.insertStop(stop3);
         */
        System.out.println("blah");
    }

    public void onViewMapClick(View view) {
        Intent intent = new Intent(this, MapBoxActivity.class);
        startActivity(intent);
    }
}
