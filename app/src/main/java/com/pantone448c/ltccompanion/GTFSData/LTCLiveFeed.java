package com.pantone448c.ltccompanion.GTFSData;

import android.util.Log;

import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.VehiclePosition;
import com.google.transit.realtime.GtfsRealtime.Alert;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;

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
            for (FeedEntity entity : vehiclePositions.getEntities())
            {
                if (entity.hasVehicle())
                {
                    vehiclePositionsList[count] = entity.getVehicle();
                    ++count;
                }
            }
            return vehiclePositionsList;
        }
        catch(InterruptedException ex)
        {
            Log.e("InterruptedException", ex.getMessage());
        }
        return new VehiclePosition[]{}; //return an empty list as a default
    }

    //returns all vehicle positions for a specific routeid
    public VehiclePosition[] getVehiclePositions(int routeid) {
        try {
            if (feedThread != null) {
                feedThread.join(); //wait for the thread to finish executing before we create a new thread
            }
            feedThread = new Thread(vehiclePositions);
            feedThread.start();
            feedThread.join(); //this seems inneficient, I wonder if there is a way to reuse the same thread for all of this, or one thread for each feed that just constantly pulls data when it's not suspended
            ArrayList<VehiclePosition> vehiclePositionsList = new ArrayList<>();
            for (FeedEntity entity : vehiclePositions.getEntities())
            {
                if (entity.hasVehicle())
                {
                    if (entity.getVehicle().hasTrip())
                    {
                        if (Integer.parseInt(entity.getVehicle().getTrip().getRouteId()) == routeid)
                        {
                            vehiclePositionsList.add(entity.getVehicle());
                        }
                    }

                }
            }
            VehiclePosition[] vehiclePositionsArray = new VehiclePosition[vehiclePositionsList.size()];
            vehiclePositionsArray = vehiclePositionsList.toArray(vehiclePositionsArray);
            return vehiclePositionsArray;
        }
        catch(InterruptedException ex)
        {
            Log.e("InterruptedException", ex.getMessage());
        }
        return new VehiclePosition[]{}; //return an empty list as a default
    }

    public TripUpdate[] getTripUpdates() {
        try {
            if (feedThread != null) {
                feedThread.join(); //wait for the thread to finish executing before we create a new thread
            }
            feedThread = new Thread(tripUpdates);
            feedThread.start();
            feedThread.join(); //this seems inneficient, I wonder if there is a way to reuse the same thread for all of this, or one thread for each feed that just constantly pulls data when it's not suspended
            TripUpdate[] tripUpdateList = new TripUpdate[tripUpdates.getEntities().length];
            int count = 0;
            for (FeedEntity entity : tripUpdates.getEntities())
            {
                if (entity.hasTripUpdate())
                {
                    tripUpdateList[count] = entity.getTripUpdate();
                    ++count;
                }
            }
            return tripUpdateList;
        }
        catch(InterruptedException ex)
        {
            Log.e("InterruptedException", ex.getMessage());
        }
        return new TripUpdate[]{}; //return an empty list as a default
    }

    public TripUpdate[] getTripUpdates(int routeid) {
        try {
            if (feedThread != null) {
                feedThread.join(); //wait for the thread to finish executing before we create a new thread
            }
            feedThread = new Thread(tripUpdates);
            feedThread.start();
            feedThread.join(); //this seems inneficient, I wonder if there is a way to reuse the same thread for all of this, or one thread for each feed that just constantly pulls data when it's not suspended
            ArrayList<TripUpdate> tripUpdateList = new ArrayList<>();
            for (FeedEntity entity : tripUpdates.getEntities())
            {
                if (entity.hasTripUpdate())
                {
                    if (Integer.parseInt(entity.getTripUpdate().getTrip().getRouteId()) == routeid)
                    {
                        tripUpdateList.add(entity.getTripUpdate());
                    }

                }
            }
            TripUpdate[] tripUpdateArray = new TripUpdate[tripUpdateList.size()];
            tripUpdateArray = tripUpdateList.toArray(tripUpdateArray);
            return tripUpdateArray;
        }
        catch(InterruptedException ex)
        {
            Log.e("InterruptedException", ex.getMessage());
        }
        return new TripUpdate[]{}; //return an empty list as a default
    }

    public Alert[] getAlerts() {
        try {
            if (feedThread != null) {
                feedThread.join(); //wait for the thread to finish executing before we create a new thread
            }
            feedThread = new Thread(alerts);
            feedThread.start();
            feedThread.join(); //this seems inneficient, I wonder if there is a way to reuse the same thread for all of this, or one thread for each feed that just constantly pulls data when it's not suspended
            Alert[] alertList = new Alert[alerts.getEntities().length];
            int count = 0;
            for (FeedEntity entity : alerts.getEntities())
            {
                if (entity.hasAlert())
                {
                    alertList[count] = entity.getAlert();
                    ++count;
                }
            }
            return alertList;
        }
        catch(InterruptedException ex)
        {
            Log.e("InterruptedException", ex.getMessage());
        }
        return new Alert[]{}; //return an empty list as a default
    }

    private GTFSFeedReader vehiclePositions;
    private GTFSFeedReader tripUpdates;
    private GTFSFeedReader alerts;

    private Thread feedThread;
    private static LTCLiveFeed _instance;
}
