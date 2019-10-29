package com.pantone448c.ltccompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;


import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class MapBoxActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {

    /** The following is used by the commented code at the bottom of the file
     * To populate the featureCollection with provided GeoJSON data for generating markers

    private GeoJsonSource source;

    private static final String GEOJSON_SOURCE_ID = "GEOJSON_SOURCE_ID";
    private static final String MARKER_IMAGE_ID = "MARKER_IMAGE_ID";
    private static final String MARKER_LAYER_ID = "MARKER_LAYER_ID";
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";
    private static final String PROPERTY_SELECTED = "selected";
    private static final String PROPERTY_NAME = "stop_name";

     */

    private MapView mapView;
    private MapboxMap mapboxMap;

    //Displaying markers?
    private FeatureCollection featureCollection;    /** A GeoJSON collection, used to store locations for markers in Mapbox */
    //private static final String SOURCE_ID = "SOURCE_ID";
    //private static final String ICON_ID = "ICON_ID";
    //private static final String LAYER_ID = "LAYER_ID";

    //Mapbox Permission Manager
    private PermissionsManager permissionsManager;

    //Handles device location
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private MapBoxActivityLocationCallback callback = new MapBoxActivityLocationCallback(this);

    //MapView Boundaries Declarations
    private static final LatLng LONDON_COORDS = new LatLng(42.983612, -81.249725);
    private static final LatLng BOUND_CORNER_NW = new LatLng(LONDON_COORDS.getLatitude() - 0.5,LONDON_COORDS.getLongitude() - 0.5);
    private static final LatLng BOUND_CORNER_SE = new LatLng(LONDON_COORDS.getLatitude() + 0.5,LONDON_COORDS.getLongitude() + 0.5);
    private static final LatLngBounds RESTRICTED_BOUNDS_AREA = new LatLngBounds.Builder()
            .include(BOUND_CORNER_NW)
            .include(BOUND_CORNER_SE)
            .build();

    private MarkerViewManager markerViewManager;
    private BuildingPlugin buildingPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**Mapbox access token configured here*/
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_key));
        setContentView(R.layout.activity_map_box);

        //Initialize Mapbox
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }   /**onCreate*/

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap){
        //TODO: Populate featureCollection with test markers
        long startTime = System.nanoTime();
        String resultingJson = "";
        try{
            //Load GeoJSON file from local assets
            InputStream is = this.getAssets().open("london_stops.geojson");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            resultingJson =  new String(buffer, Charset.forName("UTF-8"));
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }

        featureCollection = FeatureCollection.fromJson(resultingJson);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println(duration);
        //TODO: Initialize Map
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            //Set the map bounds
            mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA);
            //Launch Mapbox's location engine
            enableLocationComponent(style);

            //Configure building extrusion plugin
            buildingPlugin = new BuildingPlugin(mapView, mapboxMap, style);
            buildingPlugin.setMinZoomLevel(15f);
            buildingPlugin.setVisibility(true);

            //Configure camera position
            //TODO: Home in target on device location - currently does not grab device location in time
            mapboxMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                            /** .target(callback.lastDeviceLocation) --> Get the device location from the LocationEngine Callback */
                            .target(LONDON_COORDS)      //Camera location on launch
                            .zoom(12)                   //Camera zoom on launch (Building extrusions show <= 15)
                            .tilt(30)                   //Camera angle on launch (0-60)
                            .build()
                    ));

            //mapboxMap.addOnMapClickListener(MapBoxActivity.this);

            //Populate the FeatureCollection with the GeoJson
            //new LoadGeoJsonDataTask(MapBoxActivity.this).execute();
        });

        //TODO: Load the markers into the MarkerViewManager

        markerViewManager = new MarkerViewManager(mapView, mapboxMap);
        for(Feature feat : featureCollection.features()){
            Geometry geo = feat.geometry();

            LatLng coords = new LatLng();
            //markerViewManager.addMarker(new MarkerView(coords, customView))
        }

    }   /**onMapReady*/

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            //Acquire instance of component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            //Configure activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                    .useDefaultLocationEngine(false)
                    .build();
            //Activate and enable component
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);
            //Configure camera and render modes
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            //Initialize Location Engine
            initLocationEngine();
        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }   /**enableLocationComponent*/

    /** Configure the LocationEngine for device location queries */
    @SuppressWarnings({"MissingPermission"})
    private void initLocationEngine(){
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
        LocationEngineRequest request = new
                LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }   /**initLocationEngine*/

    /** Mapbox PermissionsManager override */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }   /**onRequestPermissionResult*/

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }    /**onExplanationNeeded*/

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            if(mapboxMap.getStyle() != null){
                enableLocationComponent(mapboxMap.getStyle());
            }
        }else{
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }   /**onPermissionResult*/

    /** Callback class responsible for processing device location updates */
    private static class MapBoxActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        protected LatLng lastDeviceLocation;
        private final WeakReference<MapBoxActivity> activityRef;    //Weak Reference required to prevent memory leaks

        MapBoxActivityLocationCallback(MapBoxActivity activity){
            this.activityRef = new WeakReference<>(activity);
        }   /**MapBoxActivityLocationCallback*/

        /** When the device location changes */
        @Override
        public void onSuccess(LocationEngineResult result){
            MapBoxActivity activity = activityRef.get();
            if(activity != null){
                Location location = result.getLastLocation();
                if(location == null)
                    return;
                lastDeviceLocation = new LatLng(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude());
                //Pass result to LocationComponent
                activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                //Print a Toast of the Coordinates
                Toast.makeText(activity, lastDeviceLocation.toString(), Toast.LENGTH_SHORT).show();
            }
        }   /**onSuccess*/

        /** When the device fails to capture location */
        @Override
        public void onFailure(@NonNull Exception exception){
            Log.d(activityRef.get().getResources().getString(R.string.debug_tag), exception.getLocalizedMessage());
            MapBoxActivity activity = activityRef.get();
            if(activity != null){
                Toast.makeText(activity, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }   /**MapBoxActivityLocationCallback*/

    @Override
    public void onStart(){
        super.onStart();
        mapView.onStart();
    }   /**onStart*/

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }   /**onResume*/

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }   /**onPause*/

    @Override
    public void onStop(){
        super.onStop();
        mapView.onStop();
    }   /**onStop*/

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }   /**onLowMemory*/

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates(callback);
        }
        if(mapboxMap != null){
            mapboxMap.removeOnMapClickListener(this);
        }
        markerViewManager.onDestroy();
        mapView.onDestroy();
    }   /**onDestroy*/

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }   /**onSaveInstanceState*/


    /**
     * -------
     * The following blocks are being configured by Matt, please ignore for now
     * -------
     */

    @Override
    public boolean onMapClick(@NonNull LatLng point){
        //return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
        return false;
    }   /**onMapClick*/

/** This code relates to the commented out code at the top of the file
 * Note that the below code is used for Marker generation, however the function has been deprecated
 * in Mapbox 7.0 in favour of the MarkerViewManager
 */

//    /*Processes and displays FeatureCollection onto Mapbox */
//    public void setUpData(final FeatureCollection collection){
//        featureCollection = collection;
//        if(mapboxMap != null){
//            mapboxMap.getStyle(style -> {
//                setUpSource(style);
//                setUpImage(style);
//                setUpMarkerLayer(style);
//                setUpInfoWindowLayer(style);
//            });
//        }
//    }   /*setUpData*/
//
//    /* Add GeoJSON source to map */
//    private void setUpSource(@NonNull Style loadedStyle){
//        source = new GeoJsonSource(GEOJSON_SOURCE_ID, featureCollection);
//        loadedStyle.addSource(source);
//    }   /*setUpSource*/
//
//    /* Add marker image to the map as SymbolLayer icon*/
//    private void setUpImage(@NonNull Style loadedStyle){
//        loadedStyle.addImage(MARKER_IMAGE_ID, BitmapFactory.decodeResource(this.getResources(), R.drawable.custom_marker));
//    }   /*setUpImage*/
//
//    /* Updates display of data on the map after FeatureCollection modification */
//    private void refreshSource(){
//        if(source != null && featureCollection != null){
//            source.setGeoJson(featureCollection);
//        }
//    }   /*refreshSource*/
//
//    /* Set up a layer with maki icons */
//    private void setUpMarkerLayer(@NonNull Style loadedStyle){
//        loadedStyle.addLayer(new SymbolLayer(MARKER_LAYER_ID, GEOJSON_SOURCE_ID)
//                .withProperties(
//                        iconImage(MARKER_IMAGE_ID),
//                        iconAllowOverlap(true),
//                        iconOffset(new Float[] {0f, -8f})
//                ));
//    }   /*setUpMarkerLayer*/
//
//    / Set up layer with Android SDK Callouts */
//    private void setUpInfoWindowLayer(@NonNull Style loadedStyle){
//        loadedStyle.addLayer(new SymbolLayer(CALLOUT_LAYER_ID, GEOJSON_SOURCE_ID)
//                .withProperties(
//                        iconImage("{name}"),
//                        iconAnchor(ICON_ANCHOR_BOTTOM),
//                        iconAllowOverlap(true),
//                        iconOffset(new Float[] {-2f, -28f})
//                )
//                .withFilter(eq((get(PROPERTY_SELECTED)), literal(true))));
//    }   /*setUpInfoWindowLayer*/
//
//    /* Handle click events for SymbolLayer symbols - When a SymbolLayer is clicked, that feature is moved to selected state */
//    private boolean handleClickIcon(PointF screenPoint){
//        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, MARKER_LAYER_ID);
//        if(!features.isEmpty()){
//            String name = features.get(0).getStringProperty(PROPERTY_NAME);
//            List<Feature> featureList = featureCollection.features();
//            if(featureList != null){
//                for(int i = 0; i < featureList.size(); ++i){
//                    if(featureList.get(i).getStringProperty(PROPERTY_NAME).equals(name)){
//                        if(featureSelectStatus(i)){
//                            setFeatureSelectState(featureList.get(i), false);
//                        }else{
//                            setSelected(i);
//                        }
//                    }
//                }
//            }
//            return true;
//        }else{
//            return false;
//        }
//    }   /*handleClickIcon*/
//
//    /* Set a feature selected state */
//    private void setSelected(int index){
//        if(featureCollection.features() != null){
//            Feature feature = featureCollection.features().get(index);
//            setFeatureSelectState(feature, true);
//            refreshSource();
//        }
//    }   /*setSelected*/
//
//    /* Selects the state of a feature */
//    private void setFeatureSelectState(@NonNull Feature feature, boolean selectedState){
//        if(feature.properties() != null){
//            feature.properties().addProperty(PROPERTY_SELECTED, selectedState);
//            refreshSource();
//        }
//    }   /*setFeatureSelectState*/
//
//    /* Checks whether a feature's selected property is true or false */
//    private boolean featureSelectStatus(int index){
//        if(featureCollection == null){
//            return false;
//        }
//        return featureCollection.features().get(index).getBooleanProperty(PROPERTY_SELECTED);
//    }
//
//    /* Invoked when bitmaps are generated from a view */
//    public void setImageGenResults(HashMap<String, Bitmap> imageMap){
//        if(mapboxMap != null){
//            mapboxMap.getStyle(style -> {
//                style.addImages(imageMap);
//            });
//        }
//    }   /*setImageGenResults*/
//
//    /* AsyncTask to load data from assets folder */
//    private static class LoadGeoJsonDataTask extends AsyncTask<Void, Void, FeatureCollection> {
//
//        private final WeakReference<MapBoxActivity> activityRef;
//
//        LoadGeoJsonDataTask(MapBoxActivity activity){
//            this.activityRef = new WeakReference<>(activity);
//        }/*LoadGeoJsonDataTask*/
//
//        @Override
//        protected FeatureCollection doInBackground(Void... params){
//            MapBoxActivity activity = activityRef.get();
//            if(activity == null){
//                return null;
//            }
//            String geoJson = loadGeoJsonFromAsset(activity, "london_stops.geojson");
//            return FeatureCollection.fromJson(geoJson);
//        }   /*doInBackground*/
//
//        @Override
//        protected void onPostExecute(FeatureCollection featureCollection){
//            super.onPostExecute(featureCollection);
//            MapBoxActivity activity = activityRef.get();
//            if(featureCollection == null || activity == null){
//                return;
//            }
//            //Add "selected" boolean property to featureCollection
//            for(Feature singleFeat : featureCollection.features()){
//                singleFeat.addBooleanProperty(PROPERTY_SELECTED, false);
//            }
//            activity.setUpData(featureCollection);
//            new GenerateViewIconTask(activity).execute(featureCollection);
//        }   /*onPostExecute*/
//
//        static String loadGeoJsonFromAsset(Context context, String filename){
//            try{
//                //Load GeoJSON file from local assets
//                InputStream is = context.getAssets().open(filename);
//                int size = is.available();
//                byte[] buffer = new byte[size];
//                is.read(buffer);
//                is.close();
//                return new String(buffer, Charset.forName("UTF-8"));
//            }catch(Exception ex){
//                throw new RuntimeException(ex);
//            }
//        }   /*loadGeoJsonFromAsset*/
//    }   /*LoadGeoJsonDataTask*/
//
//    /* AsyncTask to generate Bitmap from Views to be used as iconImage in a SymbolLayer */
//    private static class GenerateViewIconTask extends AsyncTask<FeatureCollection, Void, HashMap<String, Bitmap>>{
//        private final HashMap<String, View> viewMap = new HashMap();
//        private final WeakReference<MapBoxActivity> activityRef;
//        private final boolean refreshSource;
//
//        GenerateViewIconTask(MapBoxActivity activity, boolean refreshSource){
//            this.activityRef = new WeakReference<>(activity);
//            this.refreshSource = refreshSource;
//        }   /*GenerateViewIconTask(2)*/
//
//        GenerateViewIconTask(MapBoxActivity activity){
//            this(activity, false);
//        }   /*GenerateViewIconTask(1)*/
//
//        @SuppressWarnings("WrongThread")
//        @Override
//        protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params){
//            MapBoxActivity activity = activityRef.get();
//            if(activity != null){
//                HashMap<String, Bitmap> imagesMap = new HashMap<>();
//                LayoutInflater inflater = LayoutInflater.from(activity);
//
//                FeatureCollection featureCollection = params[0];
//
//                for(Feature feature : featureCollection.features()) {
//                    /* This shit is deprecated in the current Mapbox version - Use MarkerViewManager */
//
//                    /*BubbleLayout bubbleLayout = (BubbleLayout) inflater.inflate(R.layout.map_box_layout_callout, null);
//
//                     String name = feature.getStringProperty(PROPERTY_NAME);
//                     TextView titleTextView = bubbleLayout.findViewById(R.id.info_window_title);
//                     titleTextView.setText(name);
//
//                     String style = feature.getStringProperty(PROPERTY_CAPITAL);
//                     TextView descriptionTextView = bubbleLayout.findViewById(R.id.info_window_description);
//                     descriptionTextView.setText(
//                     String.format(activity.getString(R.string.capital), style));
//
//                     int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                     bubbleLayout.measure(measureSpec, measureSpec);
//
//                     float measuredWidth = bubbleLayout.getMeasuredWidth();
//
//                     bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);
//
//                     Bitmap bitmap = SymbolGenerator.generate(bubbleLayout);
//                     imagesMap.put(name, bitmap);
//                     viewMap.put(name, bubbleLayout);*/
//                }
//                return imagesMap;
//            }else{
//                return null;
//            }
//        }   /*doInBackground*/
//
//        @Override
//        protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap){
//            super.onPostExecute(bitmapHashMap);
//            MapBoxActivity activity = activityRef.get();
//            if(activity != null && bitmapHashMap != null){
//                activity.setImageGenResults(bitmapHashMap);
//                if(refreshSource){
//                    activity.refreshSource();
//                }
//            }
//            Toast.makeText(activity, R.string.tap_on_marker_instruction, Toast.LENGTH_SHORT).show();
//        }
//    }   /*GenerateViewIconTask*/
//
//    /* Utility class for Bitmap symbol generation */
//    private static class SymbolGenerator{
//        /* Generate a Bitmap from an Android SDK View */
//        static Bitmap generate(@NonNull View view){
//            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//            view.measure(measureSpec, measureSpec);
//
//            int measuredWidth = view.getMeasuredWidth();
//            int measuredHeight = view.getMeasuredHeight();
//
//            view.layout(0,0,measuredWidth, measuredHeight);
//            Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
//            bitmap.eraseColor(Color.TRANSPARENT);
//            Canvas canvas = new Canvas(bitmap);
//            view.draw(canvas);
//            return bitmap;
//        }   /*generate*/
//    }   /*SymbolGenerator*/

}   /**MapBoxActivity*/
