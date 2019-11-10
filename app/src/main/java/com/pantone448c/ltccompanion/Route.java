package com.pantone448c.ltccompanion;

public final class Route {
    public final int ROUTE_ID;
    public final int ROUTE_GID;
    public final String ROUTE_NAME;
    public final String ROUTE_COLOUR;

    public Route(final int ROUTE_ID, final int ROUTE_GID, final String ROUTE_NAME, final String ROUTE_COLOUR)
    {
        this.ROUTE_ID = ROUTE_ID;
        this.ROUTE_GID = ROUTE_GID;
        this.ROUTE_NAME = ROUTE_NAME;
        this.ROUTE_COLOUR = ROUTE_COLOUR;
    }
}
