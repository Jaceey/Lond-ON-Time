package com.pantone448c.ltccompanion;

import android.util.Log;

import java.util.TreeMap;

public final class Routes {

    public static void LoadRoutes(final TreeMap<Integer, Route> routes)
    {
        if (_routes == null)
        {
            _routes = new Routes(routes);
        }
        else
        {
            Log.i("AlreadyLoaded", "The static routes variable has already been initialized");
        }
    }

    public static Routes getRoutes()
    {
        return _routes;
    }

    private Routes(final TreeMap<Integer, Route> routes)
    {
        this.routes = routes;
    }
    public final TreeMap<Integer, Route> routes;

    private static Routes _routes;
}
