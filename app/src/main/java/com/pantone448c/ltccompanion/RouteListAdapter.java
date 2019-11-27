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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {

    Context context;
    public RouteListAdapterListener onClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView routeText;
        public ImageButton imageButton;
        public Button buttonDirectionMain;
        public Button buttonDirectionOther;
        public ConstraintLayout constraintLayout;

        public ViewHolder(View itemView){
            super(itemView);
            routeText = itemView.findViewById(R.id.routeText);
            imageButton = itemView.findViewById(R.id.imageView);
            buttonDirectionMain = itemView.findViewById(R.id.buttonDir1);
            buttonDirectionOther = itemView.findViewById(R.id.buttonDir2);
            //constraintLayout = itemView.findViewById(R.id.);

        }
    }

    //constructor
    public RouteListAdapter(RouteListAdapterListener listener) {
        this.onClickListener = listener;
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
        holder.itemView.setBackgroundColor(Color.parseColor(curRoute.ROUTE_COLOUR));
        holder.routeText.setText(curRoute.ROUTE_NAME);
        holder.routeText.setTextColor(Color.WHITE);
        holder.routeText.setShadowLayer(10.0f, 4,4, Color.BLACK);


        holder.buttonDirectionMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.ButtonDirectionMain(view, position);
            }
        });

        holder.buttonDirectionOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.ButtonDirectionOther(view,position);
            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.ImageBtnOnClick(view,position);
            }
        });
    }

    public interface RouteListAdapterListener {
        void ImageBtnOnClick(View v, int position);
        void ButtonDirectionMain(View v, int position);
        void ButtonDirectionOther(View v, int position);
    }

    @Override
    public int getItemCount() {
        return Routes.getRoutes().size();
    }
}