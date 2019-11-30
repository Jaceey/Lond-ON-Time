package com.pantone448c.ltccompanion;

import com.pantone448c.ltccompanion.GTFSData.GTFSStaticData;

import java.util.HashMap;

public class StopTimesByTrip {

    private HashMap<Integer, StopTime[]> stopIDsByTripID;
    private StopTimesByTrip()
    {
        stopIDsByTripID = GTFSStaticData.getStopTimes();
    }

    public static StopTime[] getStopsByID(int tripID)
    {
        return _instance.stopIDsByTripID.get(tripID);
    }

    public static void loadStopTimes()
    {
        if (_instance == null)
        {
            _instance = new StopTimesByTrip();
        }
    }

    private static StopTimesByTrip _instance;
}
