package com.pantone448c.ltccompanion;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.pantone448c.ltccompanion.ui.mapbox.MapBoxFragment;
import com.pantone448c.ltccompanion.ui.savedstops.SavedStopsFragment;

import java.util.ArrayList;
import java.util.List;

public class SavedStopsAdapter extends RecyclerView.Adapter<SavedStopsAdapter.ViewHolder> {

    private List<Stop> savedStops;
    private LatLng selectedStopLocation = MapBoxFragment.LONDON_COORDS;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LatLng coordinates;
        public TextView stopName;
        public TextView stopId;
        public ImageView imageView;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            stopName = itemView.findViewById(R.id.stopName);
            stopId = itemView.findViewById(R.id.stopId);
        }
    }

    //constructor
    public SavedStopsAdapter() {
    }

    public void setStops(List<Stop> stops){
        savedStops = stops;
        notifyDataSetChanged();
    }

    @Override
    public SavedStopsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View stopView = inflater.inflate(R.layout.savedstop_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(stopView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Stop stop = savedStops.get(position);

        holder.stopId.setText(stop.STOP_ID + "");
        holder.stopName.setText(stop.STOP_NAME + "");
        holder.coordinates = new LatLng(stop.STOP_LAT, stop.STOP_LON);
    }

    @Override
    public int getItemCount() {
        if (savedStops != null)
            return savedStops.size();
        else return 0;
    }
}
