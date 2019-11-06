package com.pantone448c.ltccompanion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView routeText;
        public ImageView imageView;
        public Button buttonDir1;
        public Button buttonDir2;

        public ViewHolder(View itemView){
            super(itemView);
            routeText = itemView.findViewById(R.id.routeText);
            imageView = itemView.findViewById(R.id.imageView);
            buttonDir1 = itemView.findViewById(R.id.buttonDir1);
            buttonDir2 = itemView.findViewById(R.id.buttonDir2);
        }
    }

    //constructor
    public RouteListAdapter() {
    }

    @Override
    public RouteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View routeView = inflater.inflate(R.layout.route_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(routeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Route curRoute = Routes.getRoutes().get(position);

        holder.imageView.setColorFilter(Color.parseColor(curRoute.ROUTE_COLOUR));
        holder.routeText.setText(curRoute.ROUTE_NAME);
    }

    @Override
    public int getItemCount() {
        return Routes.getRoutes().size();
    }
}
