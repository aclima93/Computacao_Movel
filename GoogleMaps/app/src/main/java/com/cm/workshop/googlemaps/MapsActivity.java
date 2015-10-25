package com.cm.workshop.googlemaps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Map
    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private LinearLayout coordinatesLayout;

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

    // Internet Location
    private Marker networkMarker;
    private String lastNetworkUpdateTime;
    private LinearLayout networkCoordinatesLayout;
    private TextView networkLatitudeTextView;
    private TextView networkLongitudeTextView;
    private TextView lastNetworkUpdateTimeTextView;
    private static final int NETWORK_UPDATE_INTERVAL = 1000;
    private static final int NETWORK_UPDATE_DISTANCE = 0;

    // HashMaps for storing our configurations
    private HashMap<String, Boolean> mapControlConfigurations;
    private HashMap<String, Object> mapTypeConfigurations;
    private HashMap<String, Object> markerConfigurations;

    // Keys for Intent Objects
    public static final String MAP_CONTROL_CONFIGURATIONS_KEY = "mapControlConfigurations";
    public static final String MAP_TYPE_CONFIGURATIONS_KEY = "mapTypeConfigurations";
    public static final String MARKER_CONFIGURATIONS_KEY = "markerConfigurations";

    // (Random) Request Codes. Puns intended
    private static final int MAP_CONTROL_CONFIGURATIONS_RC = 666; // hail science!
    private static final int MAP_TYPE_CONFIGURATIONS_RC = 1337; // illuminati elite
    private static final int MARKER_CONFIGURATIONS_RC = 31415; // useful for pi charts

    // Logging Strings
    private static final String EVENT = "Event";
    private static final String FUNCTION = "Function";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.v(EVENT, "onCreate");

        coordinatesLayout = (LinearLayout) findViewById(R.id.coordinates_layout);

        gpsLatitudeTextView = (TextView) findViewById(R.id.gps_latitude_tv);
        gpsLongitudeTextView = (TextView) findViewById(R.id.gps_longitude_tv);
        lastGPSUpdateTimeTextView = (TextView) findViewById(R.id.last_gps_update_time_tv);

        networkLatitudeTextView = (TextView) findViewById(R.id.network_latitude_tv);
        networkLongitudeTextView = (TextView) findViewById(R.id.network_longitude_tv);
        lastNetworkUpdateTimeTextView = (TextView) findViewById(R.id.last_network_update_time_tv);

        initMapControlConfigurations();
        initMapTypeConfigurations();
        initMarkerConfigurations();

        buildGoogleApiClient();
        createLocationRequest();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.v(EVENT, "onCreateOptionsMenu");

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.v(EVENT, "onOptionsItemSelected");

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_map_controls:
                openMapControlsSettings();
                return true;
            case R.id.action_map_types:
                openMapTypesSettings();
                return true;
            case R.id.action_markers:
                openMarkersSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openMarkersSettings() {
        // TODO:
    }

    private void openMapTypesSettings() {
        // TODO:
    }

    private void openMapControlsSettings() {
        Intent intent = new Intent(getApplicationContext(), MapControlsActivity.class);
        intent.putExtra(MAP_CONTROL_CONFIGURATIONS_KEY, mapControlConfigurations);
        startActivityForResult(intent, MAP_CONTROL_CONFIGURATIONS_RC);
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


    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {

        Log.v(FUNCTION, "onActivityResult - Assessing the result passed from an activity.");

        super.onActivityResult(requestCode, resultCode, resultIntent);

        if (resultCode == RESULT_OK) {
            if (requestCode == MAP_CONTROL_CONFIGURATIONS_RC) {
                mapControlConfigurations = (HashMap<String, Boolean>) resultIntent.getSerializableExtra(MAP_CONTROL_CONFIGURATIONS_KEY);
            } else if (requestCode == MAP_TYPE_CONFIGURATIONS_RC) {
                mapTypeConfigurations = (HashMap<String, Object>) resultIntent.getSerializableExtra(MAP_TYPE_CONFIGURATIONS_KEY);
            } else if (requestCode == MARKER_CONFIGURATIONS_RC) {
                markerConfigurations = (HashMap<String, Object>) resultIntent.getSerializableExtra(MARKER_CONFIGURATIONS_KEY);
            }
        }

        reSetupMap();

    }

    /**
     * Initialize map controls settings defaults
     */
    private void initMapControlConfigurations() {

        mapControlConfigurations = new HashMap<>();

        String[] mapControlsKeys = getResources().getStringArray(R.array.map_controls_titles);
        String[] mapControlsValues = getResources().getStringArray(R.array.map_controls_values);

        for (int i = 0; i < mapControlsKeys.length; i++)
            mapControlConfigurations.put(mapControlsKeys[i], Boolean.valueOf(mapControlsValues[i]));
    }

    /**
     * Initialize map type settings defaults
     */
    private void initMapTypeConfigurations() {

        mapTypeConfigurations = new HashMap<>();
    }

    /**
     * Initialize marker settings defaults
     */
    private void initMarkerConfigurations() {

        markerConfigurations = new HashMap<>();
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

        //
        // GPS Location
        //

        Log.v(FUNCTION, "startLocationUpdates - Starting the location updates.");
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, gpsLocationRequest, this);

        //
        // Network Location
        //

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        android.location.LocationListener locationListener = new android.location.LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                updateNetworkLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // checking for permissions at runtime is troublesome for the programmer... users should just read the fine print
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, NETWORK_UPDATE_INTERVAL, NETWORK_UPDATE_DISTANCE, locationListener);
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
     * Force the map to update
     */
    private void reSetupMap(){

        Log.v(FUNCTION, "reSetupMap - Re-setting up map.");

        googleMap = null;
        setUpMapIfNeeded();
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #googleMap} is not null.
     */
    private void setUpMap() {

        Log.v(FUNCTION, "setUpMap - Setting up map.");

        applyMapSettings();

    }

    /**
     * Based on the user configurations, set the map UI and listeners.
     */
    private void applyMapSettings() {

        Log.v(FUNCTION, "applyMapSettings - Applying map settings.");

        // Compass
        String compassKey = getString(R.string.compass_title);
        if (mapControlConfigurations.containsKey(compassKey) ) {
            googleMap.getUiSettings().setCompassEnabled(mapControlConfigurations.get(compassKey));
        }

        // Indoor Level Picker
        String indoorLevelPickerKey = getString(R.string.indoor_level_picker_title);
        if (mapControlConfigurations.containsKey(indoorLevelPickerKey) ) {
            googleMap.setIndoorEnabled(mapControlConfigurations.get(indoorLevelPickerKey));
            googleMap.getUiSettings().setIndoorLevelPickerEnabled(mapControlConfigurations.get(indoorLevelPickerKey));
        }

        // Map Toolbar
        String mapToolbarKey = getString(R.string.map_toolbar_title);
        if (mapControlConfigurations.containsKey(mapToolbarKey) ) {
            googleMap.getUiSettings().setMapToolbarEnabled(mapControlConfigurations.get(mapToolbarKey));
        }

        // My Location Button
        String myLocationButtonKey = getString(R.string.my_location_button_title);
        if (mapControlConfigurations.containsKey(myLocationButtonKey) ) {

            boolean value = mapControlConfigurations.get(myLocationButtonKey);

            if(value){
                coordinatesLayout.setVisibility(View.VISIBLE);
            }
            else{
                coordinatesLayout.setVisibility(View.GONE);
            }

            googleMap.setMyLocationEnabled(value);
            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    Log.v(EVENT, "onMyLocationChange");
                    updateGPSLocation(location);
                }
            });
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Log.v(EVENT, "onMyLocationButtonClick");

                    if(lastGPSLocation != null) {

                        Toast.makeText(getApplicationContext(), "Teleporting to your location...", Toast.LENGTH_SHORT).show();
                        CameraPosition cameraPosition = new CameraPosition.Builder().
                                target(new LatLng(lastGPSLocation.getLatitude(), lastGPSLocation.getLongitude()))
                                .tilt(0)
                                .zoom(googleMap.getMaxZoomLevel()-2) // come closer my little friend
                                .bearing(0)
                                .build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        return true;
                    }

                    Toast.makeText(getApplicationContext(), "Location unavailable...", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            googleMap.getUiSettings().setMyLocationButtonEnabled(value);

            if(googleApiClient.isConnected() && value) {
                startLocationUpdates();
            }

        }

        // Rotate Gestures
        String rotateGesturesKey = getString(R.string.rotate_gestures_title);
        if (mapControlConfigurations.containsKey(rotateGesturesKey) ) {
            googleMap.getUiSettings().setMapToolbarEnabled(mapControlConfigurations.get(rotateGesturesKey));
        }

        // Scroll Gestures
        String scrollGesturesKey = getString(R.string.scroll_gestures_title);
        if (mapControlConfigurations.containsKey(scrollGesturesKey) ) {
            googleMap.getUiSettings().setScrollGesturesEnabled(mapControlConfigurations.get(scrollGesturesKey));
        }

        // Tilt Gestures
        String tiltGesturesKey = getString(R.string.tilt_gestures_title);
        if (mapControlConfigurations.containsKey(tiltGesturesKey) ) {
            googleMap.getUiSettings().setTiltGesturesEnabled(mapControlConfigurations.get(tiltGesturesKey));
        }

        // Zoom Controls
        String zoomControlsKey = getString(R.string.zoom_controls_title);
        if (mapControlConfigurations.containsKey(zoomControlsKey) ) {
            googleMap.getUiSettings().setZoomControlsEnabled(mapControlConfigurations.get(zoomControlsKey));
        }

        // Zoom Gestures
        String zoomGesturesKey = getString(R.string.zoom_gestures_title);
        if (mapControlConfigurations.containsKey(zoomGesturesKey) ) {
            googleMap.getUiSettings().setZoomGesturesEnabled(mapControlConfigurations.get(zoomGesturesKey));
        }

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

    public void updateNetworkLocation(Location location){

        Log.v(FUNCTION, "updateNetworkLocation - Updating smartphone location using the Internet.");

        if(networkMarker == null) {
            networkMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("Network Estimate"));
        }
        else {
            networkMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        lastNetworkUpdateTime = DateFormat.getTimeInstance().format(new Date());

        networkLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        networkLongitudeTextView.setText(String.valueOf(location.getLongitude()));
        lastNetworkUpdateTimeTextView.setText(lastGPSUpdateTime);

    }
}
