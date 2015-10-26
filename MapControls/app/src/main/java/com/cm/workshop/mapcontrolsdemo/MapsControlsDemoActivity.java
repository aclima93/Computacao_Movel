package com.cm.workshop.mapcontrolsdemo;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapsControlsDemoActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback {

    // Map
    private GoogleMap mGoogleMap; // Might be null if Google Play services APK is not available.

    // GPS Location
    private GoogleApiClient googleApiClient; //used for accessing locations with GPS and WIFI
    private Location lastGPSLocation; // used for storing the last know location of the device
    private LocationRequest gpsLocationRequest; // used for defining our Location Requests

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_controls_demo);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // make use of the Google API
        buildGoogleApiClient();

        // create our Location Requester Rules
        createLocationRequest();

    }

    @Override
    public void onConnected(Bundle connectionHint) {

        // start the location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, gpsLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastGPSLocation = location;
            }
        });

        // using the API,get our last know location, just because we can
        lastGPSLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        /*
         * All requests have been canceled and no outstanding listeners will be executed.
         * GoogleApiClient will automatically attempt to restore the connection.
         */
        Toast.makeText(getApplicationContext(), "Location connection suspended.", Toast.LENGTH_LONG).show();
    }

    /**
     * In order to use Google's API we must configure the connection.
     */
    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    /**
     * A LocationRequest determines the frequency, precision, priority, etc. of location updates.
     */
    protected void createLocationRequest(){

        gpsLocationRequest = new LocationRequest();
        gpsLocationRequest.setInterval(1000); // update interval in milliseconds
        gpsLocationRequest.setFastestInterval(250); // fastest update interval in milliseconds
        gpsLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //
        // Useful Gestures
        //

        // Rotate Gestures
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);

        // Scroll Gestures
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);

        // Tilt Gestures
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);

        // Zoom Gestures
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

        //
        // "My Location" button
        //

        // enable the map's "My Location" logic (mainly interfaces)
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                lastGPSLocation = location;
            }
        });

        // enable the UI's "My Location" button
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                if (lastGPSLocation != null) {

                    // zoom in on our last know location
                    CameraPosition cameraPosition = new CameraPosition.Builder().
                            target(new LatLng(lastGPSLocation.getLatitude(), lastGPSLocation.getLongitude()))
                            .zoom(mGoogleMap.getMaxZoomLevel() - 2)
                            .build();

                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    return true;
                }
                return false;
            }
        });

    }

}
