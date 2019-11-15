package com.pantone448c.ltccompanion.ui.directions;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RouteBuilder implements Runnable
{
    private static final String GOOGLEMAPSDIRECTIONAPI = "http://maps.googleapis.com/maps/api/directions/json?key=AIzaSyBem8E_gphjPMqF65s_s46c3gxxd-IpcjA&mode=transit";

    //private static RequestQueue queue;

    private static Context mycontext;

    private static final String JSONTAGDURATION = "duration";
    private static final String JSONTAGDISTANCE = "distance";
    private static final String JSONTAGLEGS = "legs";
    private static final String JSONTAGROUTES = "routes";
    private static final String JSONTAGARRIVALTIME = "arrival_time";
    private static final String JSONTAGDEPARTURETIME = "departure_time";
    private static final String JSONTAGTEXT = "text";
    private static final String JSONTAGSTEPS = "steps";
    private static final String JSONTAGTRAVELMODE = "travel_mode";
    private static final String JSONTRAVELMODERESULTWALKING = "WALKING";
    private static final String JSONTRAVELMODERESULTTRANSIT = "TRANSIT";
    private static final String JSONTAGINSTRUCTIONS = "html_instructions";
    private static final String JSONTAGTRANSITDETAILS = "transit_details";
    private static final String JSONTAGARRIVALSTOP = "arrival_stop";
    private static final String JSONTAGDEPARTURESTOP = "departure_stop";
    private static final String JSONTAGHEADSIGN = "headsign";
    private static final String JSONTAGNAME = "name";
    private static final String JSONTAGLINE = "line";
    private static final String JSONTAGNUMSTOPS = "num_stops";

    private String startAddress;
    private String destinationAddress;
    private TransitTrip transitTrip;

    private RouteBuilder(String startAddress, String destinationAddress)
    {
        this.startAddress = startAddress;
        this.destinationAddress = destinationAddress;
    }

    public static void InitRouteBuilder(Context context)
    {
        if (mycontext == null)
        {
            //queue = Volley.newRequestQueue(context);
            mycontext = context;
        }
        else
        {
            Log.i("AlreadyInitialized", "Request Queue is already initialized");
        }
    }

    public static final TransitTrip buildTransitTrip(final String startAddress, final String destinationAddress)
    {
        RouteBuilder builder = new RouteBuilder(startAddress, destinationAddress);
        Thread t = new Thread(builder);
        try {
            t.start();
            t.join();
            return builder.transitTrip;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    @Override
    public void run() {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        String arrivalTime;
        String departureTime;
        String distance;
        String duration;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GOOGLEMAPSDIRECTIONAPI);
        stringBuilder.append("&origin=");
        stringBuilder.append(startAddress);
        stringBuilder.append("&destination=");
        stringBuilder.append(destinationAddress);

        ArrayList<Step> steps = new ArrayList<>();

        RequestQueue myqueue = Volley.newRequestQueue(mycontext);
        String URL = stringBuilder.toString();

        JsonObjectRequest request = new JsonObjectRequest(URL, null, future, future);


        try {
            myqueue.add(request);
            JSONObject response = future.get(); // this will block
            JSONObject route = response.getJSONArray(JSONTAGROUTES).getJSONObject(0);
            JSONObject leg = route.getJSONArray(JSONTAGLEGS).getJSONObject(0);
            JSONObject arrivalTimeObject = leg.getJSONObject(JSONTAGARRIVALTIME);
            arrivalTime = arrivalTimeObject.getString(JSONTAGTEXT);
            JSONObject departureTimeObject = leg.getJSONObject(JSONTAGDEPARTURETIME);
            departureTime = departureTimeObject.getString(JSONTAGTEXT);
            JSONObject distanceObject = leg.getJSONObject(JSONTAGDISTANCE);
            distance = distanceObject.getString(JSONTAGTEXT);
            JSONObject durationObject = leg.getJSONObject(JSONTAGDURATION);
            duration = durationObject.getString(JSONTAGTEXT);
            JSONArray stepsJSON = leg.getJSONArray(JSONTAGSTEPS);
            for (int i=0; i<stepsJSON.length(); ++i)
            {
                JSONObject step = stepsJSON.getJSONObject(i);
                String stepDuration;
                String stepDistance;
                String instructions;
                if (step.getString(JSONTAGTRAVELMODE) == JSONTRAVELMODERESULTWALKING)
                {
                    ArrayList<WalkingStep> subSteps = new ArrayList<>();
                    stepDuration = step.getJSONObject(JSONTAGDURATION).getString(JSONTAGTEXT);
                    stepDistance = step.getJSONObject(JSONTAGDURATION).getString(JSONTAGTEXT);
                    instructions = step.getString(JSONTAGINSTRUCTIONS);
                    JSONArray subStepsJSON = step.getJSONArray(JSONTAGSTEPS);
                    for (int n=0; n<subStepsJSON.length(); ++n)
                    {
                        JSONObject subStep = subStepsJSON.getJSONObject(n);
                        String subStepDistance = subStep.getString(JSONTAGDISTANCE);
                        String subStepDuration = subStep.getString(JSONTAGDURATION);
                        String subStepInstruction = subStep.getString(JSONTAGINSTRUCTIONS);
                        subSteps.add(new WalkingStep(subStepDistance, subStepDuration, subStepInstruction, null));
                    }
                    WalkingStep[] subStepsArray = new WalkingStep[subSteps.size()];
                    subStepsArray = subSteps.toArray(subStepsArray);
                    steps.add(new WalkingStep(stepDistance, stepDuration, instructions, subStepsArray));

                }
                else if(step.getString(JSONTAGTRAVELMODE) == JSONTRAVELMODERESULTTRANSIT)
                {
                    String stepArrivalTime;
                    String stepDepartureTime;
                    String stepArrivalStop;
                    String stepDepartureStop;
                    String stepHeadSign;
                    String routeName;
                    int numStops;
                    stepDuration = step.getJSONObject(JSONTAGDURATION).getString(JSONTAGTEXT);
                    stepDistance = step.getJSONObject(JSONTAGDURATION).getString(JSONTAGTEXT);
                    instructions = step.getString(JSONTAGINSTRUCTIONS);
                    JSONObject transitDetails = step.getJSONObject(JSONTAGTRANSITDETAILS);
                    stepArrivalStop = transitDetails.getJSONObject(JSONTAGARRIVALSTOP).getString(JSONTAGNAME);
                    stepDepartureStop = transitDetails.getJSONObject(JSONTAGDEPARTURESTOP).getString(JSONTAGNAME);
                    stepArrivalTime = transitDetails.getJSONObject(JSONTAGARRIVALTIME).getString(JSONTAGTEXT);
                    stepDepartureTime = transitDetails.getJSONObject(JSONTAGDEPARTURETIME).getString(JSONTAGTEXT);
                    stepHeadSign = transitDetails.getString(JSONTAGHEADSIGN);
                    routeName = transitDetails.getJSONObject(JSONTAGLINE).getString(JSONTAGNAME);
                    numStops = transitDetails.getInt(JSONTAGNUMSTOPS);
                    steps.add(new TransitStep(stepDistance, stepDuration, stepArrivalStop, stepArrivalTime, stepDepartureStop, stepDepartureTime, routeName, stepHeadSign, numStops));
                }
            }
            Step[] stepsArray = new Step[steps.size()];
            stepsArray = steps.toArray(stepsArray);
            transitTrip = new TransitTrip(arrivalTime, departureTime, distance, duration, startAddress, destinationAddress, stepsArray);
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
        } catch (JSONException e) {

        }
    }
}
