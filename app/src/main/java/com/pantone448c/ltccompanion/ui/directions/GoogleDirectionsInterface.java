package com.pantone448c.ltccompanion.ui.directions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleDirectionsInterface {
    @GET("json")
    Call<TransitTrip> getTrip(@Query("key") String apikey, @Query("mode") String mode, @Query("origin") String origin, @Query("destination") String destination);
}
