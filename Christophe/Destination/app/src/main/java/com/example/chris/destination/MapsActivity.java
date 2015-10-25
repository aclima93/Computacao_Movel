package com.example.chris.destination;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.directions.route.Route;
import com.directions.route.Routing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    /*ArrayList to switch finish to start and make
    sure that only 2 markers are possible to deploy*/
    private ArrayList<MarkerOptions> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markers = new ArrayList<>();
        mMap = mapFragment.getMap();

        //Long and single click listeners
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Put camera in Coimbra region
        LatLng coimbra = new LatLng(40.2025, -8.4121);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coimbra));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    @Override
    public void onMapClick(LatLng point) {
        //Move camera to the point position by ne click
        mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
    }

    @Override
    public void onMapLongClick(LatLng point) {
        mMap.clear();

        //If we have already 2 markers we have to remove one of them
        if (markers.size() >= 2) {
            markers.remove(0);
        }

        //And add a new one by FIFO
        markers.add(new MarkerOptions().position(point));

        //Put Markers in the map
        for (int i = 0; i < markers.size(); i++) {
            if (i == 0)
                mMap.addMarker(markers.get(i).title("Start").draggable(true));
            else
                mMap.addMarker(markers.get(i).title("Finish").draggable(true));
        }

        //If we are in ready to calculate a route (2 markers)
        if(markers.size()>=2){
            route();
        }
    }

    public void route() {

        //Define de route that we want
        Routing routing = new Routing.Builder()
            .travelMode(Routing.TravelMode.DRIVING)
            .waypoints(markers.get(0).getPosition(), markers.get(1).getPosition())
            .build();

        //Calculation of the route
        routing.execute();

        //Get information about the nodes
        ArrayList<Route> route = null;
        try {
            route = routing.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Add route to the map
        if(route.size()>=0) {
            for (int i = 0; i < route.size(); i++) {
                PolylineOptions polyoptions = new PolylineOptions();
                polyoptions.color(Color.BLUE);
                polyoptions.width(10);
                polyoptions.addAll(route.get(i).getPoints());
                mMap.addPolyline(polyoptions);
            }
        }
    }
}
