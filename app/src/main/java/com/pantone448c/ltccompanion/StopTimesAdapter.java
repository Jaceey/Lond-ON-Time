package com.pantone448c.ltccompanion;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.javatuples.Triplet;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StopTimesAdapter extends RecyclerView.Adapter<StopTimesAdapter.ViewHolder> {

    private ArrayList<Triplet<Route, StopTime, Trip>> timesList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewRouteId, textViewTime;

        public ViewHolder (View itemView){
            super(itemView);
            textViewRouteId = itemView.findViewById(R.id.textViewRouteId);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }

    //Constructor
    public StopTimesAdapter(){}

    public void refreshTimes(Triplet<Route, StopTime, Trip>[] times) {
        timesList = new ArrayList<>();
        Collections.addAll(timesList, times);
        timesList.sort(new Comparator<Triplet<Route, StopTime, Trip>>() {
            @Override
            public int compare(Triplet<Route, StopTime, Trip> first, Triplet<Route, StopTime, Trip> second) {
                return first.getValue1().ARRIVAL_TIME.compareTo(second.getValue1().ARRIVAL_TIME);
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public StopTimesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View timesView = inflater.inflate(R.layout.stoptimes_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(timesView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StopTimesAdapter.ViewHolder viewHolder, int position) {
        Triplet<Route, StopTime, Trip> time = timesList.get(position);

        viewHolder.itemView.setBackgroundColor(Color.parseColor(time.getValue0().ROUTE_COLOUR));
        viewHolder.textViewRouteId.setText(time.getValue0().ROUTE_NAME);
        viewHolder.textViewTime.setText(time.getValue1().ARRIVAL_TIME);
    }

    @Override
    public int getItemCount() {
        return timesList.size();
    }
}
