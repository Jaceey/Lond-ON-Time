package com.pantone448c.ltccompanion.ui.directions;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RouteBuilder implements Runnable {

    private static final String GOOGLEMAPSAPIKEY = "AIzaSyBem8E_gphjPMqF65s_s46c3gxxd-IpcjA";
    private static final String GOOGLEMAPSDIRECTIONAPI = "https://maps.googleapis.com/maps/api/directions/";
    private static Retrofit client;
    private static Gson gson;
    private static GoogleDirectionsInterface directionsInterface;

    private String origin;
    private String destination;
    private Response<TransitTrip> response;


    private RouteBuilder(String origin, String destination)
    {
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public void run()
    {
        try
        {
            response = directionsInterface.getTrip(GOOGLEMAPSAPIKEY, "transit", origin, destination).execute();
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }

    }

    public static void initRetroFit()
    {
        gson = new GsonBuilder()
                .registerTypeAdapter(Step.class, new StepDeserializer())
                .create();
        client = new Retrofit.Builder().baseUrl(GOOGLEMAPSDIRECTIONAPI).addConverterFactory(GsonConverterFactory.create(gson)).build();
        directionsInterface = client.create(GoogleDirectionsInterface.class);
    }

    public final static TransitTrip getDirections(String origin, String destination)
    {
        origin = origin.replace(' ', '+');
        destination = destination.replace(' ', '+');
        RouteBuilder routeBuilder = new RouteBuilder(origin, destination);
        Thread directionsThread = new Thread(routeBuilder);
        directionsThread.start();
        try
        {
            directionsThread.join();
            return routeBuilder.response.body();
        }
        catch (InterruptedException ex)
        {

        }

        throw new NullPointerException();
    }
}
