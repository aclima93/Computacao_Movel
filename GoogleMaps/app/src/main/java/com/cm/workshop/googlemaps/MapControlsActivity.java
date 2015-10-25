package com.cm.workshop.googlemaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

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

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.map_controls_id);

        CheckBox checkBox;

        for( final Map.Entry<String, Boolean> mapControl : mapControlConfigurations.entrySet() ){

            checkBox = new CheckBox(this);
            checkBox.setText(mapControl.getKey());
            checkBox.setChecked(mapControl.getValue());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mapControlConfigurations.put(mapControl.getKey(), ((CheckBox) v).isChecked());
                }
            });

            linearLayout.addView(checkBox);

        }

    }

}
