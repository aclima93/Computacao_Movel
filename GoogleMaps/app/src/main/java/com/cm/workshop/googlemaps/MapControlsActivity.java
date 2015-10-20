package com.cm.workshop.googlemaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import java.util.HashMap;

public class MapControlsActivity extends AppCompatActivity {

    private HashMap<String, Boolean> mapControlConfigurations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_controls);

        setupPreferences();

    }

    @Override
    public void finish() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(MapsActivity.MAP_CONTROL_CONFIGURATIONS_KEY, mapControlConfigurations);
        setResult(Activity.RESULT_OK, resultIntent);

        // this call must happen at the end, otherwise result will not be set appropriately!
        super.finish();
    }

    private void setupPreferences() {

        Intent intent = getIntent();
        mapControlConfigurations = (HashMap<String, Boolean>) intent.getSerializableExtra(MapsActivity.MAP_CONTROL_CONFIGURATIONS_KEY);

        CheckBox checkBox;

        // Compass
        String compassKey = getString(R.string.compass_key);
        if (mapControlConfigurations.containsKey(compassKey) ) {
            checkBox = (CheckBox) findViewById(R.id.compass_id);
            checkBox.setChecked(mapControlConfigurations.get(compassKey));
        }

        // Indoor Level Picker
        String indoorLevelPickerKey = getString(R.string.indoor_level_picker_key);
        if (mapControlConfigurations.containsKey(indoorLevelPickerKey) ) {
            checkBox = (CheckBox) findViewById(R.id.indoor_level_picker_id);
            checkBox.setChecked(mapControlConfigurations.get(indoorLevelPickerKey));
        }

        // Map Toolbar
        String mapToolbarKey = getString(R.string.map_toolbar_key);
        if (mapControlConfigurations.containsKey(mapToolbarKey) ) {
            checkBox = (CheckBox) findViewById(R.id.map_toolbar_id);
            checkBox.setChecked(mapControlConfigurations.get(mapToolbarKey));
        }

        // My Location Button
        String myLocationButtonKey = getString(R.string.my_location_button_key);
        if (mapControlConfigurations.containsKey(myLocationButtonKey) ) {
            checkBox = (CheckBox) findViewById(R.id.my_location_button_id);
            checkBox.setChecked(mapControlConfigurations.get(myLocationButtonKey));
        }

        // Rotate Gestures
        String rotateGesturesKey = getString(R.string.rotate_gestures_key);
        if (mapControlConfigurations.containsKey(rotateGesturesKey) ) {
            checkBox = (CheckBox) findViewById(R.id.rotate_gestures_id);
            checkBox.setChecked(mapControlConfigurations.get(rotateGesturesKey));
        }

        // Scroll Gestures
        String scrollGesturesKey = getString(R.string.scroll_gestures_key);
        if (mapControlConfigurations.containsKey(scrollGesturesKey) ) {
            checkBox = (CheckBox) findViewById(R.id.scroll_gestures_id);
            checkBox.setChecked(mapControlConfigurations.get(scrollGesturesKey));
        }

        // Tilt Gestures
        String tiltGesturesKey = getString(R.string.tilt_gestures_key);
        if (mapControlConfigurations.containsKey(tiltGesturesKey) ) {
            checkBox = (CheckBox) findViewById(R.id.tilt_gestures_id);
            checkBox.setChecked(mapControlConfigurations.get(tiltGesturesKey));
        }

        // Zoom Controls
        String zoomControlsKey = getString(R.string.zoom_controls_key);
        if (mapControlConfigurations.containsKey(zoomControlsKey) ) {
            checkBox = (CheckBox) findViewById(R.id.zoom_controls_id);
            checkBox.setChecked(mapControlConfigurations.get(zoomControlsKey));
        }

        // Zoom Gestures
        String zoomGesturesKey = getString(R.string.zoom_gestures_key);
        if (mapControlConfigurations.containsKey(zoomGesturesKey) ) {
            checkBox = (CheckBox) findViewById(R.id.zoom_gestures_id);
            checkBox.setChecked(mapControlConfigurations.get(zoomGesturesKey));
        }
    }

}
