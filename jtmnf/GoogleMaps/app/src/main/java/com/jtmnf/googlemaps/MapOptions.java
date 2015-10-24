package com.jtmnf.googlemaps;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class MapOptions extends AppCompatActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_options);

        if(getParent() instanceof MapsActivity){
            MapsActivity activity = (MapsActivity) getParent();

            mMap = activity.getmMap();
            Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_SHORT).show();
        }
}

    //NONE
    //TERRAIN
    //SATELLITE
    //NORMAL
    //HYBRID
    public void changeMapTypeHybrid(View view) {
        Intent intent = new Intent();
        intent.putExtra("map", GoogleMap.MAP_TYPE_HYBRID);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void changeMapTypeNormal(View view) {
        Intent intent = new Intent();
        intent.putExtra("map", GoogleMap.MAP_TYPE_NORMAL);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void changeMapTypeSatellite(View view) {
        Intent intent = new Intent();
        intent.putExtra("map", GoogleMap.MAP_TYPE_SATELLITE);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void changeMapTypeTerrain(View view) {
        Intent intent = new Intent();
        intent.putExtra("map", GoogleMap.MAP_TYPE_TERRAIN);

        setResult(RESULT_OK, intent);
        finish();
    }
}
