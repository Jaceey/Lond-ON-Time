package com.pantone448c.ltccompanion.ui.stoptimes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.google.gson.JsonElement;
import com.mapbox.geojson.Feature;
import com.pantone448c.ltccompanion.GTFSData.GTFSStaticData;
import com.pantone448c.ltccompanion.R;
import com.pantone448c.ltccompanion.Stop;
import com.pantone448c.ltccompanion.StopTimesAdapter;
import com.pantone448c.ltccompanion.ui.savedstops.SavedStopsFragment;

public class StopTimesActivity extends AppCompatActivity {

    StopTimesAdapter adapter;
    private int stopId;
    private TextView textViewStopId, textViewCountdown;
    private RecyclerView recyclerView;

    private CheckBox favouriteBox;

    //Setup countdown timer for refreshing
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long l) {
            textViewCountdown.setText("" + (l / 1000));
        }

        @Override
        public void onFinish() {
            refreshTimes();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_times);

        textViewStopId = findViewById(R.id.textViewStopId);
        textViewCountdown = findViewById(R.id.textViewCountdown);
        recyclerView = findViewById(R.id.timesRecycler);
        favouriteBox = findViewById(R.id.checkboxFav);
        stopId = getIntent().getExtras().getInt("stopid");

        Stop stop = null;
        try {
            stop = SavedStopsFragment.stopViewModel.getStopById(stopId);
        } catch (Exception ex)
        {

        }

        if (stop != null)
        {
            favouriteBox.setChecked(true);
        }

        favouriteBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getBaseContext(), "Stop " + stopId + " favourited!", Toast.LENGTH_SHORT).show();
                    SavedStopsFragment.stopViewModel.insertStop(GTFSStaticData.getStop(stopId));
                }
                else
                {

                    Toast.makeText(getBaseContext(), "Stop " + stopId + " unfavourited!", Toast.LENGTH_SHORT).show();
                    SavedStopsFragment.stopViewModel.deleteStop(GTFSStaticData.getStop(stopId));
                }
            }
        });
        textViewStopId.setText("#" + stopId);

        //Create Adapter
        adapter = new StopTimesAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set everything else up
        refreshTimes();
    }

    public void refreshTimes() {
        adapter.refreshTimes(GTFSStaticData.getBusesForStop(stopId, 50));
        timer.start();
    }

}
