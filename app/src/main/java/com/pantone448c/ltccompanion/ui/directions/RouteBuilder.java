package com.pantone448c.ltccompanion.ui.directions;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

        String arrivalTime;
        String departureTime;
        String distance;
        String duration;

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
            JSONObject route = response.getJSONArray("routes").getJSONObject(0);
            JSONObject leg = route.getJSONArray("legs").getJSONObject(0);
            JSONObject arrivalTimeObject = leg.getJSONObject("arrival_time");
            arrivalTime = arrivalTimeObject.getString("text");
            JSONObject departureTimeObject = leg.getJSONObject("departure_time");
            departureTime = departureTimeObject.getString("text");
            JSONObject distanceObject = leg.getJSONObject("distance");
            distance = distanceObject.getString("text");
            JSONObject durationObject = leg.getJSONObject("duration");
            duration = durationObject.getString("text");
            JSONArray steps = leg.getJSONArray("steps");
            for (int i=0; i<steps.length(); ++i)
            {
                JSONObject step = steps.getJSONObject(i);
                String stepDuration;
                String stepDistance;
                String instructions;
                if (step.getString("travel_mode") == "WALKING")
                {
                    ArrayList<WalkingStep> subSteps = new ArrayList<>();
                }
                else if(step.getString("travel_mode") == "TRANSIT")
                {

                }
            }
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
        } catch (JSONException e)
        {

        }
    }

    private static

}
