package com.workshop.googlemaps;

import java.util.HashMap;

/**
 * Created by aclima on 11/10/15.
 */
public class SettingsKeeper {

    private HashMap<String, Object> controlConfigurations;
    private HashMap<String, Object> mapConfigurations;
    private HashMap<String, Object> markerConfigurations;

    public SettingsKeeper(){
        controlConfigurations = new HashMap<>();
        mapConfigurations = new HashMap<>();
        markerConfigurations = new HashMap<>();
    }


    public HashMap<String, Object> getControlConfigurations() {
        return controlConfigurations;
    }

    public void setControlConfigurations(HashMap<String, Object> controlConfigurations) {
        this.controlConfigurations = controlConfigurations;
    }


    public HashMap<String, Object> getMapConfigurations() {
        return mapConfigurations;
    }

    public void setMapConfigurations(HashMap<String, Object> mapConfigurations) {
        this.mapConfigurations = mapConfigurations;
    }

    public HashMap<String, Object> getMarkerConfigurations() {
        return markerConfigurations;
    }

    public void setMarkerConfigurations(HashMap<String, Object> markerConfigurations) {
        this.markerConfigurations = markerConfigurations;
    }
}
