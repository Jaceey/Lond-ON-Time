package com.pantone448c.ltccompanion;

public final class Route {
    public final int ROUTE_ID;
    public final int ROUTE_NAME;
    public final int ROUTE_COLOUR;
    public final int ROUTE_TEXT_COLOUR;

    public Route(final int ROUTE_ID, final int ROUTE_NAME, final int ROUTE_COLOUR, final int ROUTE_TEXT_COLOUR)
    {
        this.ROUTE_ID = ROUTE_ID;
        this.ROUTE_NAME = ROUTE_NAME;
        this.ROUTE_COLOUR = ROUTE_COLOUR;
        this.ROUTE_TEXT_COLOUR = ROUTE_TEXT_COLOUR;
    }
}
