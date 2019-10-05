package com.pantone448c.ltccompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.transit.realtime.GtfsRealtime;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void download(View view) {
        new Thread(new Runnable() {
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
                }
                catch (Exception ex)
                {

                }
            }
        }).start();
    }
}
