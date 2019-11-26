package com.pantone448c.ltccompanion.ui.directions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


import org.json.JSONObject;

import java.lang.reflect.Type;

public class StepDeserializer implements JsonDeserializer {
    private static final String JSONTAGTRAVELMODE = "travel_mode";
    private static final String JSONTRAVELMODERESULTWALKING = "WALKING";
    private static final String JSONTRAVELMODERESULTTRANSIT = "TRANSIT";

    @Override
    public Step deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new GsonBuilder().create();
        String travelmode = json.getAsJsonObject().get(JSONTAGTRAVELMODE).getAsString();
        if (travelmode.equals(JSONTRAVELMODERESULTWALKING))
        {
            return gson.fromJson(json, WalkingStep.class);
        }
        else if (travelmode.equals(JSONTRAVELMODERESULTTRANSIT))
        {
            return gson.fromJson(json, TransitStep.class);
        }
        return null;
    }
}
