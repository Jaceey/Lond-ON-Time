package com.pantone448c.ltccompanion;

import android.util.Log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.TreeMap;

public class GTFSStaticData {
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
                    routes.put(route_name, route);
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


}
