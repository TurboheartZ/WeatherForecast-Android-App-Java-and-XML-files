package com.zhangxy.weatherforecast;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.loaders.ObservationsTask;
import com.hamweather.aeris.communication.loaders.ObservationsTaskCallback;
import com.hamweather.aeris.communication.parameter.PlaceParameter;
import com.hamweather.aeris.model.AerisError;

import java.util.List;


public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        String lat = intent.getStringExtra("latitude");
        String lng = intent.getStringExtra("longitude");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //add a fragment
        MapFragment myFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lat", lat);
        bundle.putString("lng", lng);
        myFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.MapFrame, myFragment);
        fragmentTransaction.commit();
    }
    public void onFragmentInteraction(Uri uri)
    {

    }

//
//        // setting up secret key and client id for oauth to aeris
//        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), this);
//
//        // instantiate the place we will use for requesting data from the task
//        PlaceParameter place = new PlaceParameter("seattle,wa");
//        //PlaceParameter place = new PlaceParameter(lat, lon);
//
//        //create the batch builder object
////        BatchBuilder builder = new BatchBuilder();
////
////        //set the place
////        builder.addGlobalParameter(place);
//
//        //API request and callback
//        ObservationsTask task = new ObservationsTask(MapActivity.this, new ObservationsTaskCallback() {
//            @Override public void onObservationsFailed(AerisError error) {
//            // handle fail here
//        }
//            @Override public void onObservationsLoaded(List responses) {
//            // handle successful loading here.
//        } });
//
//        //API Parameter
//        ParameterBuilder builder = new ParameterBuilder()
//                .withFields(ObservationFields.ICON)
//                .withFilter("day")
//                .withLimit(2)
//                .withRadius(5)
//                .withFrom("-24hours")
//                .withTo("now");
//        //task.requestClosest(place, builder.build());
//
//        //Make the request
//        task.requestClosest(place);


//****************************************************************************************

//        MapViewFragment weathermap = new MapViewFragment() {
//            @Nullable
//            @Override
//            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//                AerisEngine.initWithKeys(MapActivity.this.getString(R.string.aeris_client_id), MapActivity.this.getString(R.string.aeris_client_secret), MapActivity.this);
//                PlaceParameter place = new PlaceParameter("seattle,wa");
//                ObservationsTask task = new ObservationsTask(MapActivity.this, new ObservationsTaskCallback() {
//                    @Override public void onObservationsFailed(AerisError error) {
//                        // handle fail here
//                    }
//                    @Override public void onObservationsLoaded(List responses) {
//                        // handle successful loading here.
//                    } });
//                ParameterBuilder builder = new ParameterBuilder()
//                        .withFields(ObservationFields.ICON)
//                        .withFilter("day")
//                        .withLimit(2)
//                        .withRadius(5)
//                        .withFrom("-24hours")
//                        .withTo("now");
//                task.requestClosest(place);
//
//                View view = inflater.inflate(R.layout.content_map, container, false);
//                mapView = (AerisMapView)view.findViewById(R.id.aerisfragment_map);
//                mapView.init(savedInstanceState, AerisMapView.AerisMapType.GOOGLE);
//                OnAerisMapLongClickListener listener = new OnAerisMapLongClickListener() {
//                    @Override
//                    public void onMapLongClick(double v, double v1) {
//
//                    }
//                };
//                mapView.setOnAerisMapLongClickListener(listener);
//                return view;
//
//            }
//        };
//****************************************************************************************

}
