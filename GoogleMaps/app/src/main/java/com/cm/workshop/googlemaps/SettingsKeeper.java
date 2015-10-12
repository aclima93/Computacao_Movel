package com.cm.workshop.googlemaps;

import java.util.HashMap;

/**
 * Created by aclima on 11/10/15.
 */
public class SettingsKeeper {

    private HashMap<String, Object> mapControlConfigurations;
    private HashMap<String, Object> mapTypeConfigurations;
    private HashMap<String, Object> markerConfigurations;

    public SettingsKeeper(){
        mapControlConfigurations = new HashMap<>();
        mapTypeConfigurations = new HashMap<>();
        markerConfigurations = new HashMap<>();
    }


    public HashMap<String, Object> getMapControlConfigurations() {
        return mapControlConfigurations;
    }

    public void setMapControlConfigurations(HashMap<String, Object> mapControlConfigurations) {
        this.mapControlConfigurations = mapControlConfigurations;
    }


    public HashMap<String, Object> getMapTypeConfigurations() {
        return mapTypeConfigurations;
    }

    public void setMapTypeConfigurations(HashMap<String, Object> mapTypeConfigurations) {
        this.mapTypeConfigurations = mapTypeConfigurations;
    }

    public HashMap<String, Object> getMarkerConfigurations() {
        return markerConfigurations;
    }

    public void setMarkerConfigurations(HashMap<String, Object> markerConfigurations) {
        this.markerConfigurations = markerConfigurations;
    }
}
