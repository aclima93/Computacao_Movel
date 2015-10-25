package com.jtmnf.googlemaps;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        getTouchListener();
    }

    private void getTouchListener() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoderCoords = new Geocoder(getApplicationContext());
                List<Address> addressList;

                try {
                    addressList = geocoderCoords.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    for (Address address : addressList) {
                        /*System.out.println("getLocality: "+address.getLocality());
                        System.out.println("getAdminArea: "+address.getAdminArea());
                        System.out.println("getCountryCode: "+address.getCountryCode());
                        System.out.println("getCountryName: "+address.getCountryName());
                        System.out.println("getFeatureName: "+address.getFeatureName());
                        System.out.println("getPhone: "+address.getPhone());
                        System.out.println("getPostalCode: "+address.getPostalCode());
                        System.out.println("getPremises: "+address.getPremises());
                        System.out.println("getSubAdminArea: "+address.getSubAdminArea());
                        System.out.println("getSubLocality: "+address.getSubLocality());
                        System.out.println("getSubThoroughfare: "+address.getSubThoroughfare());
                        System.out.println("getThoroughfare: "+address.getThoroughfare());
                        System.out.println("getUrl: "+address.getUrl());
                        System.out.println("getLocale: "+address.getLocale().toString());

                        System.out.println("TOSTRING: "+address.toString());
                        */

                        String addressStr = "";
                        for(int i = 0; i < address.getMaxAddressLineIndex(); i++){
                            if (address.getAddressLine(i) != null)
                                addressStr += address.getAddressLine(i) + "\n";
                        }

                        Toast.makeText(getApplicationContext(), addressStr.substring(0, addressStr.length()-1), Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_activity_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map_settings:
                openMapSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openMapSettings() {
        startActivityForResult(new Intent(this, MapOptions.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                mMap.setMapType(data.getIntExtra("map", 0));
            }
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setBuildingsEnabled(true);
    }

    public GoogleMap getmMap() {
        return mMap;
    }
}
