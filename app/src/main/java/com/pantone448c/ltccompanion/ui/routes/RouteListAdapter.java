package com.pantone448c.ltccompanion.ui.routes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.pantone448c.ltccompanion.R;
import com.pantone448c.ltccompanion.Route;
import com.pantone448c.ltccompanion.Routes;
import com.pantone448c.ltccompanion.ui.mapbox.MapBoxFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {
    public ArrayList<String> NSRoutes = new ArrayList<>(Arrays.asList("Route 1","Route 4","Route 6","Route 10","Route 12","Route 13","Route 15","Route 16","Route 19","Route 25","Route 35","Route 37","Route 90","Route 92","Route 93","Route 102","Route 104","Route 106"));
    public ArrayList<String> EWRoutes = new ArrayList<>(Arrays.asList("Route 2","Route 3","Route 5","Route 7","Route 9","Route 17","Route 20","Route 24","Route 27","Route 28","Route 30","Route 31","Route 33","Route 36","Route 91","Route 94"));

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

        Log.i("ROUTE NAME:", curRoute.ROUTE_NAME);

        if (NSRoutes.contains(curRoute.ROUTE_NAME))
        {
            holder.buttonDir1.setText("North");
            holder.buttonDir2.setText("South");
        }
        else if (EWRoutes.contains(curRoute.ROUTE_NAME))
        {
            holder.buttonDir1.setText("East");
            holder.buttonDir2.setText("West");
        }
        else
        {
            holder.buttonDir1.setText("Forward");
            holder.buttonDir2.setText("Reverse");
        }

        holder.buttonDir1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)v.getContext();

                Bundle bundle = new Bundle();
                bundle.putInt("routeid", Routes.getRoutes().get(position).ROUTE_GID);
                bundle.putString("direction", holder.buttonDir1.getText().toString());

                Fragment frag = new MapBoxFragment();
                frag.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.routeFragment, frag).addToBackStack(null).commit();


            }
        });

        holder.buttonDir2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();

                Bundle bundle = new Bundle();
                bundle.putInt("routeid", Routes.getRoutes().get(position).ROUTE_GID);
                bundle.putString("direction", holder.buttonDir2.getText().toString());

                Fragment frag = new MapBoxFragment();
                frag.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.routeFragment, frag).addToBackStack(null).commit();

            }
        });


      //  holder.imageView.setColorFilter(Color.parseColor(curRoute.ROUTE_COLOUR));
      //  holder.imageView.setBackgroundColor(Color.parseColor(curRoute.ROUTE_COLOUR));
        holder.itemView.setBackgroundColor(Color.parseColor(curRoute.ROUTE_COLOUR));
        holder.routeText.setText(curRoute.ROUTE_NAME);
        holder.routeText.setTextColor(Color.WHITE);
        holder.routeText.setShadowLayer(10.0f, 4,4, Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return Routes.getRoutes().size();
    }
}
