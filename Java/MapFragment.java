package com.zhangxy.weatherforecast;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.hamweather.aeris.communication.AerisCallback;
import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.EndpointType;
import com.hamweather.aeris.communication.loaders.ObservationsTask;
import com.hamweather.aeris.communication.loaders.ObservationsTaskCallback;
import com.hamweather.aeris.communication.parameter.PlaceParameter;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.maps.interfaces.OnAerisMapLongClickListener;
import com.hamweather.aeris.model.AerisError;
import com.hamweather.aeris.model.AerisResponse;
import com.hamweather.aeris.tiles.AerisTile;

import java.util.List;

/**
 * Created by Zhangxy on 12/9/2015.
 */
public class MapFragment extends MapViewFragment implements OnAerisMapLongClickListener, AerisCallback {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), "com.zhangxy.weatherforecast");

        Bundle bundle = getArguments();
        String lat = bundle.getString("lat");
        String lng = bundle.getString("lng");

        Location location = new Location("");
        location.setLatitude(Double.valueOf(lat));
        location.setLongitude(Double.valueOf(lng));

        View view = inflater.inflate(R.layout.fragment_interactive_maps, container, false);
        mapView = (AerisMapView)view.findViewById(R.id.aerisfragment_map);
        mapView.init(savedInstanceState, AerisMapView.AerisMapType.GOOGLE);
        mapView.moveToLocation(location,9);
        mapView.addLayer(AerisTile.RADSAT);
        mapView.setOnAerisMapLongClickListener(this);
        return view;
    }
    private void initMap() {
        mapView.moveToLocation(new LatLng(34.7, -86.7), 9);
        mapView.setOnAerisMapLongClickListener(this);

    }

    @Override
    public void onMapLongClick(double lat, double longitude) {
        // code to handle map long press. i.e. Fetch current conditions?
        // see demo app MapFragment.java
    }

    @Override
    public void onResult(EndpointType endpointType, AerisResponse aerisResponse) {

    }
}
