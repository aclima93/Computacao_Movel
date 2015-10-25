package com.cm.workshop.googlemaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;

public class MapTypesActivity extends AppCompatActivity {

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_types);

        if(getParent() instanceof MapsActivity){
            MapsActivity activity = (MapsActivity) getParent();

            googleMap = activity.getGoogleMap();
        }
    }

    //NONE
    //TERRAIN
    //SATELLITE
    //NORMAL
    //HYBRID
    public void changeMapTypeHybrid(View view) {
        Intent intent = new Intent();
        intent.putExtra(MapsActivity.MAP_TYPE_CONFIGURATIONS_KEY, GoogleMap.MAP_TYPE_HYBRID);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void changeMapTypeNormal(View view) {
        Intent intent = new Intent();
        intent.putExtra(MapsActivity.MAP_TYPE_CONFIGURATIONS_KEY, GoogleMap.MAP_TYPE_NORMAL);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void changeMapTypeSatellite(View view) {
        Intent intent = new Intent();
        intent.putExtra(MapsActivity.MAP_TYPE_CONFIGURATIONS_KEY, GoogleMap.MAP_TYPE_SATELLITE);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void changeMapTypeTerrain(View view) {
        Intent intent = new Intent();
        intent.putExtra(MapsActivity.MAP_TYPE_CONFIGURATIONS_KEY, GoogleMap.MAP_TYPE_TERRAIN);

        setResult(RESULT_OK, intent);
        finish();
    }
}
