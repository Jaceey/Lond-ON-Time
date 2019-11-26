package com.pantone448c.ltccompanion.ui.directions;

public class GoogleTime {
    public final String text;
    public final String time_zone;
    public final int value;

    public GoogleTime(final String text, final String time_zone, final int value)
    {
        this.text = text;
        this.time_zone = time_zone;
        this.value = value;
    }
}
