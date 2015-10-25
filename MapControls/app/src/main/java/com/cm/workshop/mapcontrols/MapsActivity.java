package com.cm.workshop.mapcontrols;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

public class MapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Map
    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.

    // GPS Location
    private GoogleApiClient googleApiClient; //used for accessing locations with GPS and WIFI
    private Location lastGPSLocation; // used for storing the last know location of the device
    private String lastGPSUpdateTime;
    private LocationRequest gpsLocationRequest;
    private TextView gpsLatitudeTextView;
    private TextView gpsLongitudeTextView;
    private TextView lastGPSUpdateTimeTextView;
    private static final int GPS_UPDATE_INTERVAL = 1000;
    private static final int GPS_UPDATE_MAX_INTERVAL = 250;

    // Logging Strings
    private static final String EVENT = "Event";
    private static final String FUNCTION = "Function";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.v(EVENT, "onCreate");

        gpsLatitudeTextView = (TextView) findViewById(R.id.gps_latitude_tv);
        gpsLongitudeTextView = (TextView) findViewById(R.id.gps_longitude_tv);
        lastGPSUpdateTimeTextView = (TextView) findViewById(R.id.last_gps_update_time_tv);

        buildGoogleApiClient();
        createLocationRequest();
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(EVENT, "onResume");
        // an update might be in order
        buildGoogleApiClient();
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(EVENT, "onPause");
        // stop updates
        stopLocationUpdates();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        updateGPSLocation(LocationServices.FusedLocationApi.getLastLocation(googleApiClient));

        Log.v(EVENT, "onConnected - Location connection successful.");
        Toast.makeText(getApplicationContext(), "Location connection successful.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(EVENT, "onConnectionSuspended - Location connection suspended.");
        Toast.makeText(getApplicationContext(), "Location connection suspended.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(EVENT, "onConnectionFailed - Location connection failed.");
        Toast.makeText(getApplicationContext(), "Location connection failed.", Toast.LENGTH_LONG).show();
    }

    protected void startLocationUpdates() {

        Log.v(FUNCTION, "startLocationUpdates - Starting the location updates.");
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, gpsLocationRequest, this);

    }

    protected void stopLocationUpdates() {
        Log.v(FUNCTION, "stopLocationUpdates - Stopping the location updates.");

        if(googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(EVENT, "onLocationChanged");
        updateGPSLocation(location);
    }

    /**
     * In order to use Google's API we must configure the connection.
     */
    protected synchronized void buildGoogleApiClient() {

        Log.v(FUNCTION, "buildGoogleApiClient - Configuring the Google API access client.");

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    /**
     * A LocationRequest is what determines the frequency, precision, priority, etc. of location updates.
     */
    protected void createLocationRequest(){

        Log.v(FUNCTION, "createLocationRequest - Creating the location request.");

        gpsLocationRequest = new LocationRequest();
        gpsLocationRequest.setInterval(GPS_UPDATE_INTERVAL);
        gpsLocationRequest.setFastestInterval(GPS_UPDATE_MAX_INTERVAL);
        gpsLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #googleMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {

        Log.v(FUNCTION, "setUpMapIfNeeded - Set up map if it is necessary.");

        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {

            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #googleMap} is not null.
     */
    private void setUpMap() {

        Log.v(FUNCTION, "setUpMap - Setting up map.");

        //
        // "My Location" button
        //

        // enable the map's "My Location" logic (mainly interfaces)
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Log.v(EVENT, "onMyLocationChange");
                updateGPSLocation(location);
            }
        });

        // enable the UI's "My Location" button
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Log.v(EVENT, "onMyLocationButtonClick");

                if (lastGPSLocation != null) {

                    Toast.makeText(getApplicationContext(), "Teleporting to your location...", Toast.LENGTH_SHORT).show();

                    // zoom in on our last know location
                    CameraPosition cameraPosition = new CameraPosition.Builder().
                            target(new LatLng(lastGPSLocation.getLatitude(), lastGPSLocation.getLongitude()))
                            .tilt(0)
                            .zoom(googleMap.getMaxZoomLevel() - 2) // come closer my little friend
                            .bearing(0)
                            .build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    return true;
                }

                Toast.makeText(getApplicationContext(), "Location unavailable...", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        if(googleApiClient.isConnected()) {
            startLocationUpdates();
        }

        //
        // Useful Gestures
        //

        // Rotate Gestures
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        // Scroll Gestures
        googleMap.getUiSettings().setScrollGesturesEnabled(true);

        // Tilt Gestures
        googleMap.getUiSettings().setTiltGesturesEnabled(true);

        // Zoom Gestures
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

    }

    public void updateGPSLocation(Location location){

        Log.v(FUNCTION, "updateGPSLocation - Updating smartphone location using GPS.");

        lastGPSLocation = location;

        if (lastGPSLocation != null) {

            lastGPSUpdateTime = DateFormat.getTimeInstance().format(new Date());

            gpsLatitudeTextView.setText(String.valueOf(lastGPSLocation.getLatitude()));
            gpsLongitudeTextView.setText(String.valueOf(lastGPSLocation.getLongitude()));
            lastGPSUpdateTimeTextView.setText(lastGPSUpdateTime);

        }
    }
}
