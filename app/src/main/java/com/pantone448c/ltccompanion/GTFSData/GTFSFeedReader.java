package com.pantone448c.ltccompanion.GTFSData;

import android.util.Log;

import java.io.IOException;
import java.net.URL;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;

public class GTFSFeedReader implements Runnable {
    private final URL feedURL;
    private FeedMessage message;
    private FeedEntity[] entities;

    public FeedEntity[] getEntities()
    {
        return entities;
    }

    public GTFSFeedReader(URL feedURL)
    {
        this.feedURL = feedURL;
    }

    @Override
    public void run() {
        try
        {
            message = FeedMessage.parseFrom(feedURL.openStream());
            entities = new FeedEntity[message.getEntityCount()];
            entities = message.getEntityList().toArray(entities);
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
        }
    }
}
