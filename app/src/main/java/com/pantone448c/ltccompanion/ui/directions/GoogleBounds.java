package com.pantone448c.ltccompanion.ui.directions;

public class GoogleBounds {
    public final GoogleLocation northeast;
    public final GoogleLocation southwest;

    public GoogleBounds(final GoogleLocation northeast, final GoogleLocation southwest)
    {
        this.northeast = northeast;
        this.southwest = southwest;
    }
}
