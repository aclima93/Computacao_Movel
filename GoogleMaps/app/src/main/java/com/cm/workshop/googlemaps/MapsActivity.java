package com.cm.workshop.googlemaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private SettingsKeeper settingsKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        settingsKeeper = new SettingsKeeper();

        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_map_settings:
                openMapSettings();
                return true;
            case R.id.action_marker_settings:
                openMarkerSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openMarkerSettings() {
        startActivity(new Intent(getApplicationContext(), MarkerSettingsActivity.class));
    }

    private void openMapSettings() {
        startActivity(new Intent(getApplicationContext(), MapSettingsActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
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
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Polo 2.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        applyMapSettings();

        mMap.addMarker(new MarkerOptions().position(new LatLng(40.214266, -8.407384)).title("Pólo 2"));

        // adding a new marker everytime the user performs a longpress on a location
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title("A simple marker!"));

                // show a toast indicating coordinates of the marker
                Toast.makeText(getApplicationContext(), "Pinned marker at " + marker.getPosition().latitude + "\n" + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyMapSettings() {

        UiSettings mapSettings = mMap.getUiSettings();
        HashMap<String, Object> mapControlConfigurations = settingsKeeper.getMapControlConfigurations();


        // Compass
        String compassKey = getString(R.string.compass_key);
        if (mapControlConfigurations.containsKey(compassKey) ) {
            mapSettings.setCompassEnabled((Boolean) mapControlConfigurations.get(compassKey));
        }

        // Indoor Level Picker
        String indoorLevelPickerKey = getString(R.string.indoor_level_picker_key);
        if (mapControlConfigurations.containsKey(indoorLevelPickerKey) ) {
            mapSettings.setIndoorLevelPickerEnabled((Boolean) mapControlConfigurations.get(indoorLevelPickerKey));
        }

        // Map Toolbar
        String mapToolbarKey = getString(R.string.map_toolbar_key);
        if (mapControlConfigurations.containsKey(mapToolbarKey) ) {
            mapSettings.setMapToolbarEnabled((Boolean) mapControlConfigurations.get(mapToolbarKey));
        }

        // My Location Button
        String myLocationButtonKey = getString(R.string.my_location_button_key);
        if (mapControlConfigurations.containsKey(myLocationButtonKey) ) {
            mapSettings.setMapToolbarEnabled((Boolean) mapControlConfigurations.get(myLocationButtonKey));
        }

        // Rotate Gestures
        String rotateGesturesKey = getString(R.string.rotate_gestures_key);
        if (mapControlConfigurations.containsKey(rotateGesturesKey) ) {
            mapSettings.setMapToolbarEnabled((Boolean) mapControlConfigurations.get(rotateGesturesKey));
        }

        // Scroll Gestures
        String scrollGesturesKey = getString(R.string.scroll_gestures_key);
        if (mapControlConfigurations.containsKey(scrollGesturesKey) ) {
            mapSettings.setMapToolbarEnabled((Boolean) mapControlConfigurations.get(scrollGesturesKey));
        }

        // Tilt Gestures
        String tiltGesturesKey = getString(R.string.tilt_gestures_key);
        if (mapControlConfigurations.containsKey(tiltGesturesKey) ) {
            mapSettings.setMapToolbarEnabled((Boolean) mapControlConfigurations.get(tiltGesturesKey));
        }

        // Zoom Controls
        String zoomControlsKey = getString(R.string.zoom_controls_key);
        if (mapControlConfigurations.containsKey(zoomControlsKey) ) {
            mapSettings.setZoomControlsEnabled((Boolean) mapControlConfigurations.get(zoomControlsKey));
        }

        // Zoom Gestures
        String zoomGesturesKey = getString(R.string.zoom_gestures_key);
        if (mapControlConfigurations.containsKey(zoomGesturesKey) ) {
            mapSettings.setZoomControlsEnabled((Boolean) mapControlConfigurations.get(zoomGesturesKey));
        }

    }
}
