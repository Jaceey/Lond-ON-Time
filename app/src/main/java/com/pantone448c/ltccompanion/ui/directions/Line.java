package com.pantone448c.ltccompanion.ui.directions;

public class Line {
    public final String color;
    public final String name;
    public final String short_name;
    public final String text_color;

    public Line(final String color, final String name, final String short_name, final String text_color)
    {
        this.color = color;
        this.name = name;
        this.short_name = short_name;
        this.text_color = text_color;
    }
}
