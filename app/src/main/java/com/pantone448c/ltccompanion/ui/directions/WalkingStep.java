package com.pantone448c.ltccompanion.ui.directions;

public class WalkingStep extends Step {
    public final String instructions;
    public final WalkingStep[] steps;

    public WalkingStep(final String distance, final String duration, final String instructions, WalkingStep[] steps)
    {
        super(distance, duration, TravelMode.WALKING);
        this.instructions = instructions;
        this.steps = steps;
    }
}
