package com.pantone448c.ltccompanion.ui.directions;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pantone448c.ltccompanion.R;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder> {

    final static String TRAVELMODEWALK = "WALKING";
    final static String TRAVELMODETRANSIT = "TRANSIT";

    private Step[] steps = null;
    private Context context;

    public abstract class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView distance;
        public TextView duration;
        public TextView instructions;
        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }

    public class WalkingViewHolder extends DirectionsAdapter.ViewHolder
    {
        public RecyclerView subSteps;
        public DirectionsAdapter subStepAdapter;
        public WalkingViewHolder(View itemView)
        {
            super(itemView);
            distance = itemView.findViewById(R.id.distanceValW);
            duration = itemView.findViewById(R.id.durationValW);
            instructions = itemView.findViewById(R.id.directionsW);
            subSteps = itemView.findViewById(R.id.subSteps);
            subStepAdapter = new DirectionsAdapter();
            subSteps.setAdapter(subStepAdapter);
            subSteps.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    public class TransitViewHolder extends DirectionsAdapter.ViewHolder
    {
        TextView initialStop;
        TextView finalStop;
        TextView arrivalTime;
        TextView departureTime;
        TextView numStops;
        public TransitViewHolder(View itemView)
        {
            super(itemView);
            distance = itemView.findViewById(R.id.distanceValT);
            duration = itemView.findViewById(R.id.durationValT);
            instructions = itemView.findViewById(R.id.directionsT);
            initialStop = itemView.findViewById(R.id.initialStop);
            finalStop = itemView.findViewById(R.id.finalStop);
            arrivalTime = itemView.findViewById(R.id.arrivalTimeVal);
            departureTime = itemView.findViewById(R.id.departureTimeVal);
            numStops = itemView.findViewById(R.id.numStops);
        }
    }

    public void setSteps(Step[] steps)
    {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (steps[position].travel_mode.equals(TRAVELMODEWALK))
        {
            return 0;
        }
        else if (steps[position].travel_mode.equals(TRAVELMODETRANSIT))
        {
            return 1;
        }
        return 0; //defaults to walking data, don't want to deal with exception handling right now will update later if I have time should never reach this point and it's only there to make java shutup
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType)
        {
            case 0:
                return new WalkingViewHolder(inflater.inflate(R.layout.walkingstep_item, parent, false));
            case 1:
                return new TransitViewHolder(inflater.inflate(R.layout.transitstep_item, parent, false));
        }
        throw new NullPointerException();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.distance.setText(steps[position].distance.text);
        holder.duration.setText(steps[position].duration.text);
        holder.instructions.setText(steps[position].html_instructions);
        if (holder.getClass().equals(WalkingViewHolder.class))
        {
            WalkingViewHolder wHolder = (WalkingViewHolder)holder;
            WalkingStep step = (WalkingStep)steps[position];
            if (step.steps != null)
            {
                wHolder.subStepAdapter.setSteps(step.steps);
            }

        }
        else if(holder.getClass().equals(TransitViewHolder.class))
        {
            TransitViewHolder tHolder = (TransitViewHolder)holder;
            TransitStep step = (TransitStep)steps[position];
            tHolder.numStops.setText(String.valueOf(step.transit_details.num_stops));
            tHolder.departureTime.setText(step.transit_details.departure_time.text);
            tHolder.arrivalTime.setText(step.transit_details.arrival_time.text);
            tHolder.initialStop.setText(step.transit_details.arrival_stop.name);
            tHolder.finalStop.setText(step.transit_details.departure_stop.name);
        }
    }

    @Override
    public int getItemCount() {
        if (steps != null)
        {
            return steps.length;
        }
        return 0;
    }
}
