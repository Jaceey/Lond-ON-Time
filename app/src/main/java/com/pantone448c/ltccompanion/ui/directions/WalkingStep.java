package com.pantone448c.ltccompanion.ui.directions;

public class WalkingStep extends Step {
    public final WalkingStep[] steps;

    public WalkingStep(final GooglePair distance, final GooglePair duration, final GoogleLocation end_location, final GoogleLocation start_location, final String travel_mode, final String html_instructions, final WalkingStep[] steps)
    {
        super(distance, duration, end_location, start_location, travel_mode, html_instructions);
        this.steps = steps;
    }
}
