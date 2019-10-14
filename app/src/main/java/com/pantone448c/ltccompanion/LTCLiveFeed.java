package com.pantone448c.ltccompanion;

import android.util.Log;

import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtime.VehiclePosition;
import com.google.transit.realtime.GtfsRealtime.VehicleDescriptor;
import com.google.transit.realtime.GtfsRealtime.Alert;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripDescriptor;
import java.util.ArrayList;

import java.net.MalformedURLException;
import java.net.URL;

public class LTCLiveFeed {
    public static LTCLiveFeed Instance()
    {
        if (_instance == null)
        {
            _instance = new LTCLiveFeed();
        }
        return _instance;
    }
    private LTCLiveFeed()
    {
        try
        {
            vehiclePositions = new GTFSFeedReader(new URL("http://gtfs.ltconline.ca/Vehicle/VehiclePositions.pb"));
            tripUpdates = new GTFSFeedReader(new URL("http://gtfs.ltconline.ca/TripUpdate/TripUpdates.pb"));
            alerts = new GTFSFeedReader(new URL("http://gtfs.ltconline.ca/Alert/Alerts.pb"));
        }
        catch (MalformedURLException ex)
        {
            Log.e("MalformedURL", "You goofed...");
        }

    }

    public VehiclePosition[] getVehiclePositions() {
        try {
            if (feedThread != null) {
                feedThread.join(); //wait for the thread to finish executing before we create a new thread
            }
            feedThread = new Thread(vehiclePositions);
            feedThread.start();
            feedThread.join(); //this seems inneficient, I wonder if there is a way to reuse the same thread for all of this, or one thread for each feed that just constantly pulls data when it's not suspended
            VehiclePosition[] vehiclePositionsList = new VehiclePosition[vehiclePositions.getEntities().length];
            int count = 0;
            for (GtfsRealtime.FeedEntity entity : vehiclePositions.getEntities())
            {
               vehiclePositionsList[count] = entity.getVehicle();
               ++count;
            }
            return vehiclePositionsList;
        }
        catch(InterruptedException ex)
        {
            Log.e("InterruptedException", ex.getMessage());
        }
        return new VehiclePosition[]{}; //return an empty list as a default
    }

    private GTFSFeedReader vehiclePositions;
    private GTFSFeedReader tripUpdates;
    private GTFSFeedReader alerts;

    private Thread feedThread;
    private static LTCLiveFeed _instance;
}
