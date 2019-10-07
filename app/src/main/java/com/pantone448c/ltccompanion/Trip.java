package com.pantone448c.ltccompanion;

public class Trip {

    public final int ROUTE_ID;
    public final int SERVICE_ID;
    public final int TRIP_ID;
    public final String TRIP_HEADSIGN;
    public final Direction DIRECTION_ID;
    public final Wheelchair_Accessible WHEELCHAIR_ACCESSIBLE;
    public final Bikes_Allowed BIKES_ALLOWED;

    public Trip(final int ROUTE_ID, final int SERVICE_ID, final int TRIP_ID, final String TRIP_HEADSIGN, final Direction DIRECTION_ID, final Wheelchair_Accessible WHEELCHAIR_ACCESSIBLE, final Bikes_Allowed BIKES_ALLOWED)
    {
        this.ROUTE_ID = ROUTE_ID;
        this.SERVICE_ID = SERVICE_ID;
        this.TRIP_ID = TRIP_ID;
        this.TRIP_HEADSIGN = TRIP_HEADSIGN;
        this.DIRECTION_ID = DIRECTION_ID;
        this.WHEELCHAIR_ACCESSIBLE = WHEELCHAIR_ACCESSIBLE;
        this.BIKES_ALLOWED = BIKES_ALLOWED;
    }
}
