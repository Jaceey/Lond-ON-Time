package com.pantone448c.ltccompanion;

import android.util.Log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Contains helper functions for parsing GTFS Static Data
 */
public class GTFSStaticData {

    /**
     *
     * @param in An input stream pointing to the routes.txt file
     * @return a TreeMap of Route objects with all the routes in routes.txt
     */
    public static TreeMap<Integer, Route> getRoutes(InputStream in)
    {
        TreeMap<Integer, Route> routes = new TreeMap<>();

        Reader read = new InputStreamReader(in);

        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record : records)
            {
                if (count > 0)//skip headers
                {
                    int route_id = Integer.parseInt(record.get(0)); //will always have a value as it's required by GTFS standard
                    int route_name = Integer.parseInt(record.get(2));
                    int route_colour = 0;
                    int route_text_colour = 0;
                    if (!record.get(7).isEmpty())
                    {
                        route_colour = Integer.parseInt(record.get(7), 16);
                    }
                    if (!record.get(8).isEmpty())
                    {
                        route_text_colour = Integer.parseInt(record.get(8), 16);
                    }
                    Route route = new Route(route_id, route_name, route_colour, route_text_colour);
                    routes.put(route_id, route);
                }
                ++count;
            }
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }


        return routes;
    }

    /**
     *
     * @param in An input stream to the trips.txt file
     * @param route_id The bus route id for the specific trip
     * @param direction The direction of the trip you're looking for
     * @param numTrips The number of trips you want to look up, 0 for all trips
     * @return an Array of Trip Objects
     */
    public static final Trip[] getTripsByRouteDirection(InputStream in, int route_id, Direction direction, int numTrips)
    {
        ArrayList<Trip> trips = new ArrayList<>();

        Reader read = new InputStreamReader(in);
        try
        {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            int tripCount = 0;
            for (CSVRecord record :records)
            {
                if (count > 1)
                {
                    if (Integer.parseInt(record.get(0)) == route_id)
                    {
                        if (numTrips > 0) //if numTrips is equal to 0 pull all the trips
                        {
                            if (tripCount < numTrips)
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
                                ++tripCount;
                            }
                            else
                            {
                                break;
                            }

                        }
                        else
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


}
