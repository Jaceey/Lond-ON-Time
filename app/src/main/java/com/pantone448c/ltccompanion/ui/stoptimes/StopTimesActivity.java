package com.pantone448c.ltccompanion.ui.stoptimes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.pantone448c.ltccompanion.GTFSData.GTFSStaticData;
import com.pantone448c.ltccompanion.R;
import com.pantone448c.ltccompanion.StopTimesAdapter;

public class StopTimesActivity extends AppCompatActivity {

    StopTimesAdapter adapter;
    private int stopId;
    private TextView textViewStopId, textViewCountdown;
    private RecyclerView recyclerView;

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

        stopId = getIntent().getExtras().getInt("stopid");
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
