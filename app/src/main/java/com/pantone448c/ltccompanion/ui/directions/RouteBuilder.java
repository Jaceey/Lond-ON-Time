package com.pantone448c.ltccompanion.ui.directions;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class RouteBuilder
{
    private static final String googleMapsDirectionAPI = "https://maps.googleapis.com/maps/api/directions/json?key=AIzaSyBem8E_gphjPMqF65s_s46c3gxxd-IpcjA&mode=transit";

    private static RequestQueue queue;

    public static void InitRouteBuilder(Application application)
    {
        if (queue == null)
        {
            queue = Volley.newRequestQueue(application);
        }
        else
        {
            Log.i("AlreadyInitialized", "Request Queue is already initialized");
        }
    }

    public static final TransitTrip buildTransitTrip(final String startAddress, final String destinationAddress)
    {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(googleMapsDirectionAPI);
        stringBuilder.append("&origin=");
        stringBuilder.append(startAddress);
        stringBuilder.append("&destination=");
        stringBuilder.append(destinationAddress);

        JsonObjectRequest request = new JsonObjectRequest(stringBuilder.toString(), new JSONObject(), future, future);
        queue.add(request);

        try {
            JSONObject response = future.get(); // this will block
            String arrivalTime = response.getJSONArray()
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
        }
    }

}
