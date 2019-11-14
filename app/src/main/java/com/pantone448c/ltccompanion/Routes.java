package com.pantone448c.ltccompanion;

import android.util.Log;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Contains a singleton to store all the Routes
 */
public final class Routes {

    public static ArrayList<Route> getRoutes()
    {
        if (_instance == null)
        {
            _instance = new Routes();

        }

        return _instance.routes;
    }

    public static Route getRoute(int routeID)
    {
        for (int i = 0; i<_instance.routes.size(); ++i)
        {
            if (_instance.routes.get(i).ROUTE_ID == routeID)
            {
                return _instance.routes.get(i);
            }
        }
        throw new NullPointerException();
    }

    private Routes() {this.routes = GTFSStaticData.getRoutes();}
    public final ArrayList<Route> routes;
    private static Routes _instance;
}
