package com.pantone448c.ltccompanion;

import android.app.Application;
import android.util.Log;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.javatuples.Triplet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Contains helper functions for parsing GTFS Static Data
 */
public class GTFSStaticData {

    private static Application context;

    //call in main activity onCreate
    public static void initContext(Application appContext)
    {
        if (context == null)
        {
            context = appContext;
        }
        else
        {
            Log.d("AlreadyInitialized", "App context already initialized!");
        }

    }

    /**
     *
     * @return an ArrayList of Route objects with all the routes in routes.txt
     */
    public static ArrayList<Route> getRoutes()
    {
        ArrayList<Route> routes = new ArrayList<>();
        InputStream in = context.getResources().openRawResource(R.raw.routes);
        Reader read = new InputStreamReader(in);

        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record : records)
            {
                if (count > 0)//skip headers
                {
                    int route_id = Integer.parseInt(record.get(2)); //will always have a value as it's required by GTFS standard
                    int route_gid = Integer.parseInt(record.get(0));
                    String route_name = record.get(3);
                    String route_color = "#" + record.get(7);

                    Route route = new Route(route_id, route_gid, route_name, route_color);
                    routes.add(route);
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }

        Collections.sort(routes, new Comparator<Route>() {
            @Override public int compare(Route r1, Route r2) {
                return r1.ROUTE_ID - r2.ROUTE_ID;
            }
        });

        return routes;
    }

    /**
     *
     * @param route_id The bus route id for the specific trip
     * @param direction The direction of the trip you're looking for
     * @param numTrips The number of trips you want to look up
     * @return an Array of Trip Objects
     */
    public static final Trip[] getTrips(int route_id, int direction, int numTrips)
    {
        ArrayList<Trip> trips = new ArrayList<>();
        InputStream in = context.getResources().openRawResource(R.raw.trips);
        Reader read = new InputStreamReader(in);
        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            int tripCount = 0;
            for (CSVRecord record :records)
            {
                if (count > 0) {
                    if (Integer.parseInt(record.get(0)) == route_id && Integer.parseInt(record.get(5)) == direction) {
                        if (tripCount < numTrips) {
                            int service_id = Integer.parseInt(record.get(1));
                            int trip_id = Integer.parseInt(record.get(2));
                            String trip_headsign = record.get(3);
                            Direction _direction;
                            Wheelchair_Accessible wheelchair_accessible;
                            Bikes_Allowed bikes_allowed;
                            if (record.get(5) == "1") {
                                _direction = Direction.OTHERWAY;
                            } else //if it's not otherway we'll just assume it's ONEWAY (it should only be a 0 or 1 but better to just default to ONEWAY
                            {
                                _direction = Direction.ONEWAY;
                            }

                            if (record.get(8) == "1") {
                                wheelchair_accessible = Wheelchair_Accessible.WHEELCHAIRS;
                            } else if (record.get(8) == "2") {
                                wheelchair_accessible = Wheelchair_Accessible.NOWHEELCHAIRS;
                            } else {
                                wheelchair_accessible = Wheelchair_Accessible.NOINFO;
                            }

                            if (record.get(9) == "1") {
                                bikes_allowed = Bikes_Allowed.BIKES;
                            } else if (record.get(9) == "2") {
                                bikes_allowed = Bikes_Allowed.NOBIKES;
                            } else {
                                bikes_allowed = Bikes_Allowed.NOINFO;
                            }
                            trips.add(new Trip(route_id, service_id, trip_id, trip_headsign, _direction, wheelchair_accessible, bikes_allowed));
                            ++tripCount;
                        } else {
                            break;
                        }
                    }
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.e("IndexOutofRange", ex.getMessage());
        }
        Trip[] tripsArray = new Trip[trips.size()];
        tripsArray = trips.toArray(tripsArray); //convert ArrayList to straight array, don't need to return an entire arraylist since this shouldn't be edited.
        return tripsArray;
    }

    /**
     *
     * @param route_id The bus route id for the specific trip
     * @param direction The direction of the trip you're looking for
     * @return an Array of Trip Objects
     */
    public static final Trip[] getTrips(int route_id, int direction)
    {
        ArrayList<Trip> trips = new ArrayList<>();
        InputStream in = context.getResources().openRawResource(R.raw.trips);
        Reader read = new InputStreamReader(in);
        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record :records)
            {
                if (count > 0)
                {
                    if (Integer.parseInt(record.get(0)) == route_id && Integer.parseInt(record.get(5)) == direction)
                    {
                        int service_id = Integer.parseInt(record.get(1));
                        int trip_id = Integer.parseInt(record.get(2));
                        String trip_headsign = record.get(3);
                        Direction _direction;
                        Wheelchair_Accessible wheelchair_accessible;
                        Bikes_Allowed bikes_allowed;
                        if (record.get(5) == "1")
                        {
                            _direction = Direction.OTHERWAY;
                        }
                        else //if it's not otherway we'll just assume it's ONEWAY (it should only be a 0 or 1 but better to just default to ONEWAY
                        {
                            _direction = Direction.ONEWAY;
                        }

                        if (record.get(8) == "1")
                        {
                            wheelchair_accessible = Wheelchair_Accessible.WHEELCHAIRS;
                        }
                        else if (record.get(8) == "2")
                        {
                            wheelchair_accessible = Wheelchair_Accessible.NOWHEELCHAIRS;
                        }
                        else
                        {
                            wheelchair_accessible = Wheelchair_Accessible.NOINFO;
                        }

                        if (record.get(9) == "1")
                        {
                            bikes_allowed = Bikes_Allowed.BIKES;
                        }
                        else if (record.get(9) == "2")
                        {
                            bikes_allowed = Bikes_Allowed.NOBIKES;
                        }
                        else
                        {
                            bikes_allowed = Bikes_Allowed.NOINFO;
                        }
                        trips.add(new Trip(route_id, service_id, trip_id, trip_headsign, _direction, wheelchair_accessible, bikes_allowed));
                    }
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.e("IndexOutofRange", ex.getMessage());
        }
        Trip[] tripsArray = new Trip[trips.size()];
        tripsArray = trips.toArray(tripsArray); //convert ArrayList to straight array, don't need to return an entire arraylist since this shouldn't be edited.
        return tripsArray;
    }

    /**
     *
     * @param tripid the id of the trip that we'll be looking up stops for
     * @return list of stoptimes
     */
    public static final StopTime[] getStopTimes(int tripid)
    {
        ArrayList<StopTime> stopTimes = new ArrayList<>();
        InputStream in = context.getResources().openRawResource(R.raw.stop_times);
        Reader read = new InputStreamReader(in);
        boolean found = false;
        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record: records)
            {
                if (count > 0)
                {
                    int test = Integer.parseInt(record.get(0));
                    if (Integer.parseInt(record.get(0)) == tripid)
                    {
                        found = true;
                        int trip_id = Integer.parseInt(record.get(0));
                        String arrival_time = record.get(1);
                        String departure_time = record.get(2);
                        int stop_id = Integer.parseInt(record.get(3));
                        int stop_sequence = Integer.parseInt(record.get(4));
                        stopTimes.add(new StopTime(trip_id, arrival_time, departure_time, stop_id, stop_sequence));
                    }
                    if (found && Integer.parseInt(record.get(0)) != tripid) //data is already ordered so if we've found the tripid and it's no longer matching up, then we've found all the stops for that trip
                    {
                        break;
                    }
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.e("IndexOutofRange", ex.getMessage());
        }

        StopTime[] stopTimesArray = new StopTime[stopTimes.size()];
        stopTimesArray = stopTimes.toArray(stopTimesArray);
        return stopTimesArray;
    }

    /**
     *
     * @param routeid the route that we want to look up stops for
     * @param direction the direction of the route
     * @return list of stops
     */
    public static final Stop[] getStops(int routeid, int direction)
    {
        ArrayList<Stop> stops = new ArrayList<>();
        InputStream in = context.getResources().openRawResource(R.raw.stops);
        Reader read = new InputStreamReader(in);

        Trip trip = getTrips(routeid, direction, 1)[0]; //only need the first trip to find all the stops associated with it, need the trip info because thats what allows us to associate the routeid with stops
        StopTime[] stopTimes = getStopTimes(trip.TRIP_ID); //need stopTimes as they allow us to associate a tripid with specific stop locations
        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record: records)
            {
                if (count > 0)
                {
                    for (int i=0; i<stopTimes.length; ++i) //will probably sort this list to make search times faster if it's too slow
                    {
                        if (Integer.parseInt(record.get(0)) == stopTimes[i].STOP_ID)
                        {
                            int stop_id = Integer.parseInt((record.get(0)));
                            int stop_code = Integer.parseInt((record.get(1)));
                            String stop_name = record.get(2);
                            float stop_lat = Float.parseFloat(record.get(4));
                            float stop_lon = Float.parseFloat(record.get(5));
                            int wheelchair_boarding = Integer.parseInt(record.get(11));
                            stops.add(new Stop(stop_id, stop_code, stop_name, stop_lat, stop_lon, wheelchair_boarding));
                        }
                    }

                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.e("IndexOutofRange", ex.getMessage());
        }
        Stop[] stopsArray = new Stop[stops.size()];
        stopsArray = stops.toArray(stopsArray);
        return  stopsArray;
    }



    /**
     *
     * @param routeid the route that we want to look up stops for
     * @param direction the direction of the route
     * @return list features with the lat/lon for the stops
     */
    public static final Feature[] getStopsAsFeatures(int routeid, int direction)
    {
        ArrayList<Feature> features = new ArrayList<>();
        InputStream in = context.getResources().openRawResource(R.raw.stops);
        Reader read = new InputStreamReader(in);

        Trip trip = getTrips(routeid, direction, 1)[0]; //only need the first trip to find all the stops associated with it, need the trip info because thats what allows us to associate the routeid with stops
        StopTime[] stopTimes = getStopTimes(trip.TRIP_ID); //need stopTimes as they allow us to associate a tripid with specific stop locations
        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record: records)
            {
                if (count > 0)
                {
                    for (int i=0; i<stopTimes.length; ++i)
                    {
                        if (Integer.parseInt(record.get(0)) == stopTimes[i].STOP_ID)
                        {
                            int stop_id = Integer.parseInt((record.get(0)));
                            int stop_code = Integer.parseInt((record.get(1)));
                            String stop_name = record.get(2);
                            float stop_lat = Float.parseFloat(record.get(4));
                            float stop_lon = Float.parseFloat(record.get(5));
                            int wheelchair_boarding = Integer.parseInt(record.get(11));

                            Feature newFeat = Feature.fromGeometry(Point.fromLngLat(stop_lon, stop_lat));
                            newFeat.addNumberProperty("stop_id", stop_id);
                            newFeat.addNumberProperty("stop_code", stop_code);
                            newFeat.addStringProperty("stop_name", stop_name);
                            newFeat.addNumberProperty("stop_lat", stop_lat);
                            newFeat.addNumberProperty("stop_lon", stop_lon);
                            newFeat.addNumberProperty("wheelchair_boarding", wheelchair_boarding);

                            features.add(newFeat);
                        }
                    }
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.e("IndexOutofRange", ex.getMessage());
        }
        Feature[] featuresArray = new Feature[features.size()];
        featuresArray = features.toArray(featuresArray);
        return  featuresArray;
    }

    /**
     *
     * @return list of stops
     */
    public static final Stop[] getStops()
    {
        ArrayList<Stop> stops = new ArrayList<>();
        InputStream in = context.getResources().openRawResource(R.raw.stops);
        Reader read = new InputStreamReader(in);

        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record: records)
            {
                if (count > 0)
                {

                    int stop_id = Integer.parseInt((record.get(0)));
                    int stop_code;
                    if (record.get(1) != "")
                    {
                        stop_code = Integer.parseInt((record.get(1)));
                    }
                    else
                    {
                        stop_code = 0;
                    }
                    String stop_name = record.get(2);
                    float stop_lat = Float.parseFloat(record.get(4));
                    float stop_lon = Float.parseFloat(record.get(5));
                    int wheelchair_boarding = Integer.parseInt(record.get(11));
                    stops.add(new Stop(stop_id, stop_code, stop_name, stop_lat, stop_lon, wheelchair_boarding));
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.e("IndexOutofRange", ex.getMessage());
        }
        Stop[] stopsArray = new Stop[stops.size()];
        stopsArray = stops.toArray(stopsArray);
        return  stopsArray;
    }

    /**
     *
     * @return list of stops
     */
    public static final Stop getStop(int stopID)
    {
        InputStream in = context.getResources().openRawResource(R.raw.stops);
        Reader read = new InputStreamReader(in);

        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record: records)
            {
                if (count > 0)
                {

                    if (Integer.parseInt(record.get(0)) == stopID)
                    {
                        int stop_id = Integer.parseInt((record.get(0)));
                        int stop_code;
                        if (record.get(1) != "")
                        {
                            stop_code = Integer.parseInt((record.get(1)));
                        }
                        else
                        {
                            stop_code = 0;
                        }
                        String stop_name = record.get(2);
                        float stop_lat = Float.parseFloat(record.get(4));
                        float stop_lon = Float.parseFloat(record.get(5));
                        int wheelchair_boarding = Integer.parseInt(record.get(11));
                        return new Stop(stop_id, stop_code, stop_name, stop_lat, stop_lon, wheelchair_boarding);
                    }
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.e("IndexOutofRange", ex.getMessage());
        }
        throw new NullPointerException();
    }


    /**
     *
     * @return list features with the lat/lon for the stops
     */
    public static final Feature[] getStopsAsFeatures()
    {
        ArrayList<Feature> features = new ArrayList<>();
        InputStream in = context.getResources().openRawResource(R.raw.stops);
        Reader read = new InputStreamReader(in);

        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record: records)
            {
                if (count > 0)
                {
                    int stop_id = Integer.parseInt((record.get(0)));
                    int stop_code;
                    if (record.get(1) != "")
                    {
                        stop_code = Integer.parseInt((record.get(1)));
                    }
                    else
                    {
                        stop_code = 0;
                    }
                    String stop_name = record.get(2);
                    float stop_lat = Float.parseFloat(record.get(4));
                    float stop_lon = Float.parseFloat(record.get(5));
                    int wheelchair_boarding = Integer.parseInt(record.get(11));

                    Feature newFeat = Feature.fromGeometry(Point.fromLngLat(stop_lon, stop_lat));
                    newFeat.addNumberProperty("stop_id", stop_id);
                    newFeat.addNumberProperty("stop_code", stop_code);
                    newFeat.addStringProperty("stop_name", stop_name);
                    newFeat.addNumberProperty("stop_lat", stop_lat);
                    newFeat.addNumberProperty("stop_lon", stop_lon);
                    newFeat.addNumberProperty("wheelchair_boarding", wheelchair_boarding);

                    features.add(newFeat);
                    /*double lat = Double.parseDouble(record.get(4));
                    double lon = Double.parseDouble(record.get(5));
                    Feature feature = Feature.fromGeometry(Point.fromLngLat(lon, lat));
                    feature.addStringProperty("StopName", record.get(2));
                    feature.addNumberProperty("StopID", Integer.parseInt(record.get(0)));
                    feature.addNumberProperty("WheelChairBoarding", Integer.parseInt(record.get(11)));
                    features.add(feature);*/
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.e("IndexOutofRange", ex.getMessage());
        }
        Feature[] featuresArray = new Feature[features.size()];
        featuresArray = features.toArray(featuresArray);
        return  featuresArray;
    }

    public static final Trip getTrip(int tripID)
    {
        InputStream in = context.getResources().openRawResource(R.raw.trips);
        Reader read = new InputStreamReader(in);

        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record: records)
            {
                if (count > 0)
                {
                    if (Integer.parseInt(record.get(2)) == tripID)
                    {
                        int routeID = Integer.parseInt(record.get(0));
                        int serviceID = Integer.parseInt(record.get(1));
                        String tripHeadsign = record.get(3);
                        Direction direction;
                        Wheelchair_Accessible wheelchair_accessible;
                        Bikes_Allowed bikes_allowed;
                        if (record.get(5) == "1")
                        {
                            direction = Direction.ONEWAY;
                        }
                        else
                        {
                            direction = Direction.OTHERWAY;
                        }

                        if (record.get(8) == "1")
                        {
                            wheelchair_accessible = Wheelchair_Accessible.WHEELCHAIRS;
                        }
                        else if (record.get(8) == "2")
                        {
                            wheelchair_accessible = Wheelchair_Accessible.NOWHEELCHAIRS;
                        }
                        else
                        {
                            wheelchair_accessible = Wheelchair_Accessible.NOINFO;
                        }

                        if (record.get(9) == "1")
                        {
                            bikes_allowed = Bikes_Allowed.BIKES;
                        }
                        else if (record.get(9) == "2")
                        {
                            bikes_allowed = Bikes_Allowed.NOBIKES;
                        }
                        else
                        {
                            bikes_allowed = Bikes_Allowed.NOINFO;
                        }
                        return new Trip(routeID, serviceID, tripID, tripHeadsign, direction, wheelchair_accessible, bikes_allowed);
                    }
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.e("IndexOutofRange", ex.getMessage());
        }
        throw new NullPointerException();
    }

    public static final Triplet<Route, StopTime, Trip>[] getBusesForStop(int stopID, int numTrips)
    {
        ArrayList<Triplet<Route, StopTime, Trip>> output = new ArrayList<>();
        InputStream in = context.getResources().openRawResource(R.raw.stop_times);
        Reader read = new InputStreamReader(in);
        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            int countTrips = 0;
            for (CSVRecord record: records)
            {
                if (count > 0) {
                    if (countTrips < numTrips) {
                        if (Integer.parseInt(record.get(3)) == stopID) {
                            String[] arrivalTime = record.get(1).split(":");
                            int hour = Integer.parseInt(arrivalTime[0].trim());
                            int minute = Integer.parseInt(arrivalTime[1].trim());
                            int second = Integer.parseInt((arrivalTime[2].trim()));
                            int seconds = (hour * 60 * 60) + (minute * 60) + second;

                            int curHour = LocalDateTime.now().getHour();
                            int curMinute = LocalDateTime.now().getMinute();
                            int curSecond = LocalDateTime.now().getSecond();
                            int curSeconds = (curHour * 60 * 60) + (curMinute * 60) + curSecond;

                            if (seconds > curSeconds) {
                                int tripID = Integer.parseInt(record.get(0));
                                int stopSequence = Integer.parseInt(record.get(4));
                                StopTime stopTime = new StopTime(tripID, record.get(1), record.get(2), stopID, stopSequence);
                                Trip trip = getTrip(tripID);
                                Route route = Routes.getRoute(trip.ROUTE_ID);
                                output.add(new Triplet<>(route, stopTime, trip));
                                ++countTrips;
                            }
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.e("IndexOutofRange", ex.getMessage());
        }

        Triplet<Route, StopTime, Trip>[] outputArray = new Triplet[output.size()];
        outputArray = output.toArray(outputArray);
        return outputArray;
    }
}
