package com.pantone448c.ltccompanion.ui.directions;

public class GoogleRoute {
    public final GoogleBounds bounds;
    public final Leg[] legs;

    public GoogleRoute(final GoogleBounds bounds, final Leg[] legs)
    {
        this.bounds = bounds;
        this.legs = legs;
    }
}
