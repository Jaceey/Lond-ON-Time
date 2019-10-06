package com.pantone448c.ltccompanion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private GoogleApiClient googleApiClient;
    private LatLng loc;
    // Fanshawe  43.012440, -81.200180
    LatLng bullseye = new LatLng(42.98, -81.23);
    LatLng start = new LatLng(42.982600, -81.250000);
    LatLng budGardens = new LatLng(42.98237,-81.25255);
    private FusedLocationProviderClient fusedLocationClient;
    private final String TAG ="MyMaps";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Checks if ACCESS_FINE_LOCATION permission is granted
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(MapsActivity.class.getSimpleName(),"Connection suspended to Google Play Services!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(MapsActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng london = new LatLng(42.9849, -81.2453);
        mMap.addMarker(new MarkerOptions().position(london).title("Marker in London, Ontario"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(london));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to Google Play Services!");
        // get the current location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location lastLocation) {
            // Got last known location. In some rare situations this can be null.
            if (lastLocation != null) {
                // Logic to handle location object
                double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
                loc = new LatLng(lat, lon);
                Log.i(TAG, loc.toString());
                // Add a BLUE marger to current location and zoom
                // use reverse geocoding to get the current address at your location
                mMap.addMarker(new MarkerOptions().position(loc)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                       // .title(getCurrAddress()).snippet("Your location Lat:"+loc.latitude + ", Lng:" + loc.longitude));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                // animate camera allows zoom
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,14));
            }
            }
        });
    }
}
