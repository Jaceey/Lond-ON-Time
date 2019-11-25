package com.pantone448c.ltccompanion.ui.directions;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder> {

    private TransitTrip trip;

    public void setTrip(TransitTrip trip)
    {
        this.trip = trip;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView){
            super(itemView);
        }
    }
}
