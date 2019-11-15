package com.pantone448c.ltccompanion.ui.mapbox;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import com.mapbox.geojson.Point;
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
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolLongClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.pantone448c.ltccompanion.GTFSStaticData;
import com.pantone448c.ltccompanion.R;
import com.pantone448c.ltccompanion.ui.savedstops.SavedStopsFragment;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.os.Looper.getMainLooper;
import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class MapBoxFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {

    private MapView mapView;
    private static MapboxMap mapboxMap;
    private Context context;
    private View mapBoxFragmentView;
    
    //Mapbox Permission Manager
    private PermissionsManager permissionsManager;

    //MapView Boundaries Declarations
    public static final LatLng LONDON_COORDS = new LatLng(42.983612, -81.249725);
    private static final LatLng BOUND_CORNER_NW = new LatLng(LONDON_COORDS.getLatitude() - 0.25,LONDON_COORDS.getLongitude() - 0.25);
    private static final LatLng BOUND_CORNER_SE = new LatLng(LONDON_COORDS.getLatitude() + 0.25,LONDON_COORDS.getLongitude() + 0.25);
    private static final LatLngBounds RESTRICTED_BOUNDS_AREA = new LatLngBounds.Builder()
            .include(BOUND_CORNER_NW)
            .include(BOUND_CORNER_SE)
            .build();

    //Handles device location
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 3;
    private MapBoxActivityLocationCallback callback = new MapBoxActivityLocationCallback(this);
    public static LatLng lastDeviceLocation = LONDON_COORDS;

    //Mapbox Symbol/Marker Generation
    private FeatureCollection featureCollection;    /* A GeoJSON collection, used to store locations for markers in Mapbox */
    private SymbolManager symbolManager;
    private List<SymbolOptions> symbolOptions;

    private BuildingPlugin buildingPlugin;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }   /*onCreate*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState == null){
            mapBoxFragmentView = inflater.inflate(R.layout.fragment_map_box, container, false);
        }
        return mapBoxFragmentView;
    }   /*onCreateView*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //setContentView(R.layout.fragment_map_box);

        /*Mapbox access token configured here*/
        Mapbox.getInstance(context, getResources().getString(R.string.mapbox_key));
        mapView = new MapView(context);
        ConstraintLayout parent = mapBoxFragmentView.findViewById(R.id.mapFragmentParent);
        parent.addView(mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        featureCollection = FeatureCollection.fromFeatures(GTFSStaticData.getStopsAsFeatures());
        symbolOptions = new ArrayList<>();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap){
        //TODO: Initialize Map
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(getString(R.string.mapbox_cust_style), new Style.OnStyleLoaded(){  //References unique map style on account
            @Override
            public void onStyleLoaded(@NonNull Style style){
                //Launch Mapbox's location engine
                enableLocationComponent(style);

                //Set the map bounds
                mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA);
                mapboxMap.setMinZoomPreference(10f);

                //Configure building extrusion plugin
                buildingPlugin = new BuildingPlugin(mapView, mapboxMap, style);
                buildingPlugin.setMinZoomLevel(12f);
                buildingPlugin.setVisibility(true);

                symbolManager = new SymbolManager(mapView, mapboxMap, style);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setTextAllowOverlap(true);

                //Set up the symbols from the featurecollection
                Gson gson = new Gson();
                for(Feature feat : featureCollection.features()){
                    try {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("stop_id", feat.getNumberProperty("stop_id"));
                        jsonObj.put("stop_code", feat.getNumberProperty("stop_code"));
                        jsonObj.put("stop_name", feat.getStringProperty("stop_name"));

                        symbolOptions.add(
                                new SymbolOptions()
                                        .withGeometry((Point)feat.geometry())
                                        .withIconImage("bus-11")
                                        .withData(gson.fromJson(jsonObj.toString(), JsonElement.class))
                                        .withTextField(feat.getStringProperty("stop_name"))
                                        .withTextOpacity(0.0f)  //Hides the stop name from the map
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(getString(R.string.debug_tag), e.getMessage());
                    }
                }
                Log.d(getString(R.string.debug_tag), symbolOptions.size() + " stops loaded");
                symbolManager.create(symbolOptions);

                symbolManager.addClickListener(new OnSymbolClickListener(){
                    @Override
                    public void onAnnotationClick(Symbol symbol){
                        Toast.makeText(getContext(), symbol.getTextField(), Toast.LENGTH_SHORT).show();
                    }
                });

                symbolManager.addLongClickListener(new OnSymbolLongClickListener() {
                    @Override
                    public void onAnnotationLongClick(Symbol symbol) {
                        JsonElement jsonElement = symbol.getData();
                        String jsonResult = jsonElement.getAsJsonObject().get("stop_id").toString();
                        Toast.makeText(getContext(), "Favouriting stop " + jsonResult, Toast.LENGTH_SHORT).show();
                        SavedStopsFragment.stopViewModel.insertStop(GTFSStaticData.getStop(Integer.parseInt(jsonResult)));
                    }
                });
            }
        });

        //Configure initial camera position
        ResetCameraPosition(lastDeviceLocation, false);

    }   /*onMapReady*/

    public static void ResetCameraPosition(LatLng newCameraPos, boolean smoothPan){
        if(smoothPan){
            mapboxMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(newCameraPos)     //Camera location on launch
                                    .zoom(16f)                      //Camera zoom on launch (Building extrusions show <= 15)
                                    .tilt(30.0)                     //Camera angle on launch (0-60)
                                    .bearing(0.0)
                                    .build()
                    ), 2500);
        }else{
            mapboxMap.setCameraPosition(
                    new CameraPosition.Builder()
                            .target(newCameraPos)     //Camera location on launch
                            .zoom(16f)                      //Camera zoom on launch (Building extrusions show <= 15)
                            .tilt(30.0)                     //Camera angle on launch (0-60)
                            .bearing(0.0)
                            .build()
            );
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle){
        while(PermissionsManager.areLocationPermissionsGranted(context) == false){
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
        //Acquire instance of component
        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        //Configure activation options
        LocationComponentActivationOptions locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(context, loadedMapStyle)
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
    }   /*enableLocationComponent*/

    /* Configure the LocationEngine for device location queries */
    @SuppressWarnings({"MissingPermission"})
    private void initLocationEngine(){
        locationEngine = LocationEngineProvider.getBestLocationEngine(context);
        LocationEngineRequest request = new
                LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }   /*initLocationEngine*/

    /* Mapbox PermissionsManager override */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }   /*onRequestPermissionResult*/

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(context, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }    /*onExplanationNeeded*/

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            if(mapboxMap.getStyle() != null){
                enableLocationComponent(mapboxMap.getStyle());
            }
        }else{
            Toast.makeText(context, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
        }
    }   /*onPermissionResult*/

    /* Callback class responsible for processing device location updates */
    private static class MapBoxActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MapBoxFragment> activityRef;    //Weak Reference required to prevent memory leaks

        MapBoxActivityLocationCallback(MapBoxFragment activity){
            this.activityRef = new WeakReference<>(activity);
        }   /*MapBoxActivityLocationCallback*/

        /* When the device location changes */
        @Override
        public void onSuccess(LocationEngineResult result){
            MapBoxFragment activity = activityRef.get();
            if(activity != null){
                Location location = result.getLastLocation();
                if(location == null)
                    return;
                lastDeviceLocation = new LatLng(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude());
                //Pass result to LocationComponent
                activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                //Print a Toast of the Coordinates
                //Toast.makeText(activity.getContext(), lastDeviceLocation.toString(), Toast.LENGTH_SHORT).show();
            }
        }   /*onSuccess*/

        /* When the device fails to capture location */
        @Override
        public void onFailure(@NonNull Exception exception){
            Log.d(activityRef.get().getResources().getString(R.string.debug_tag), exception.getLocalizedMessage());
            MapBoxFragment activity = activityRef.get();
            if(activity != null){
                Toast.makeText(activity.getContext(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }   /*MapBoxActivityLocationCallback*/

    @Override
    public void onStart(){
        super.onStart();
        mapView.onStart();
    }   /*onStart*/

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }   /*onResume*/

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }   /*onPause*/

    @Override
    public void onStop(){
        super.onStop();
        mapView.onStop();
    }   /*onStop*/

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }   /*onLowMemory*/

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates(callback);
        }
        symbolManager.onDestroy();
        mapView.onDestroy();
    }   /*onDestroy*/

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }   /*onSaveInstanceState*/


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

    }

}   /*MapBoxFragment*/
