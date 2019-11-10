package com.pantone448c.ltccompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pantone448c.ltccompanion.ui.mapbox.MapBoxFragment;
import com.pantone448c.ltccompanion.ui.routes.RoutesFragment;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GTFSStaticData.initContext(this.getApplication());
    }


    public void exampleCSVReader()
    {
        try
        {
            InputStream in = getResources().openRawResource(R.raw.routes);
            Reader read = new InputStreamReader(in);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(read);
            int count = 0;
            for (CSVRecord record : records)
            {
                if (count > 0) //skip the first row because it will have the headers and I'm not sure how to get it to skip headers automatically
                {
                    record.get(0);//reads the first column and returns a string
                    record.get(1);//reads the second column and returns a string
                    record.get(2);//reads the third column and returns a string
                    record.get(3);//reads the fourth column and returns a string
                }
                ++count;
            }
        }
        catch (Exception ex)//generic exception to save time
        {
            Toast myToast2 = Toast.makeText(this, "uh oh... something went wrong", Toast.LENGTH_LONG);
            myToast2.show();
        }

    }

    public void download(View view) {
        //GtfsRealtime.TripUpdate[] test = LTCLiveFeed.Instance().getTripUpdates(3963);
        //GtfsRealtime.VehiclePosition[] test2 = LTCLiveFeed.Instance().getVehiclePositions(3963);
        //GtfsRealtime.Alert[] test3 = LTCLiveFeed.Instance().getAlerts();

        Stop[] stops = GTFSStaticData.getStops(3980, 0);
        Toast myToast = Toast.makeText(this, "Well it didn't crash so that's a start!", Toast.LENGTH_LONG);
        myToast.show();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    URL url = new URL("http://gtfs.ltconline.ca/Vehicle/VehiclePositions.pb");
                    URL url2 = new URL("http://gtfs.ltconline.ca/Alert/Alerts.pb");
                    URL url3 = new URL("http://gtfs.ltconline.ca/TripUpdate/TripUpdates.pb");
                    GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream());
                    GtfsRealtime.FeedMessage feed2 = GtfsRealtime.FeedMessage.parseFrom(url2.openStream());
                    GtfsRealtime.FeedMessage feed3 = GtfsRealtime.FeedMessage.parseFrom(url3.openStream());
                    GtfsRealtime.FeedEntity test;

                }
                catch (Exception ex)
                {

                }
            }
        }).start();*/
    }

    public void parseCSV(View view) {
        exampleCSVReader();
    }

    public void onViewMapClick(View view) {
        Intent intent = new Intent(this, MapBoxFragment.class);
        startActivity(intent);
    }

    public void onRoutesClick(View view) {
        Intent intent = new Intent(this, RoutesFragment.class);
        startActivity(intent);
    }

    public void onNavigationClick(View view) {
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        startActivity(intent);
    }
}
