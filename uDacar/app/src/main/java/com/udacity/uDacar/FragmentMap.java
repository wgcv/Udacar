package com.udacity.uDacar;


import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.android.geocoder.ui.GeocoderAutoCompleteView;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;
import com.mapbox.services.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.geocoding.v5.models.CarmenFeature;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentMap extends Fragment {

    private static final String TAG = FragmentMap.class.getSimpleName();
    private MapView mMapView;
    private MapboxMap mMap;
    private DirectionsRoute currentRoute;
    private GeocoderAutoCompleteView mAutoCompleteSearch;
    private LocationServices locationServices;
    private Location mCurrentLocation;
    private Position originPosition, destinationPosition;


    public FragmentMap() {
    }


    public static FragmentMap newInstance() {
        FragmentMap fragment = new FragmentMap();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MapboxAccountManager.start(getActivity(), getString(com.udacity.uDacar.R.string.mapbox_token));

        locationServices = LocationServices.getLocationServices(getActivity());

        View view = inflater.inflate(com.udacity.uDacar.R.layout.fragment_map, container, false);

        mMapView = (MapView) view.findViewById(com.udacity.uDacar.R.id.mapview);
        mAutoCompleteSearch = (GeocoderAutoCompleteView) view.findViewById(com.udacity.uDacar.R.id.search);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMap = mapboxMap;
                if (!locationServices.areLocationPermissionsGranted()) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    enableLocation();

                    // Enable user tracking to show the padding affect.
                    mMap.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
                    //mMap.getTrackingSettings().setDismissAllTrackingOnGesture(false);

                    // Customize the user location icon using the getMyLocationViewSettings object.
                    //mMap.getMyLocationViewSettings().setPadding(0, 500, 0, 0);
                   // mMap.getMyLocationViewSettings().setForegroundTintColor(Color.parseColor("#56B881"));
                    mMap.getMyLocationViewSettings().setAccuracyTintColor(Color.parseColor("#02B3E4"));
                    mMap.getMyLocationViewSettings().setBackgroundDrawable(
                            ContextCompat.getDrawable(getActivity(),
                                    com.udacity.uDacar.R.drawable.car_little), new int[]{0,0,0,0});

                }
            }
        });

        mAutoCompleteSearch.setAccessToken(MapboxAccountManager.getInstance().getAccessToken());
        mAutoCompleteSearch.setType(GeocodingCriteria.TYPE_POI);
        mAutoCompleteSearch.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
            @Override
            public void OnFeatureClick(CarmenFeature feature) {
                Position position = feature.asPosition();
                updateMap(position.getLatitude(), position.getLongitude());
            }
        });


        return view;
    }

    private void updateMap(double latitude, double longitude) {
        double originLat = getCurrentLocation().getLatitude();
        double originLong = getCurrentLocation().getLongitude();

        originPosition = Position.fromCoordinates(originLong, originLat);
        destinationPosition = Position.fromCoordinates(longitude, latitude);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .snippet("Destination"));

        // Get route from API
        try {
            getRoute(originPosition, destinationPosition);
            mapBoundZoom(new Position[]{originPosition, destinationPosition});
        } catch (ServicesException servicesException) {
            servicesException.printStackTrace();
        }
    }

    private void getRoute(Position origin, Position destination) throws ServicesException {

        MapboxDirections client = new MapboxDirections.Builder()
                .setOrigin(origin)
                .setDestination(destination)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .setAccessToken(MapboxAccountManager.getInstance().getAccessToken())
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get the generic HTTP info about the response
                Log.d(TAG, "Response code: " + response.code());
                if (response.body() == null) {
                    Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().getRoutes().size() < 1) {
                    Log.e(TAG, "No routes found");
                    return;
                }

                Log.d(TAG, "Response code: " + response.raw().toString());

                // Print some info about the route
                currentRoute = response.body().getRoutes().get(0);
                Log.d(TAG, "Distance: " + currentRoute.getDistance());
                Toast.makeText(
                        getActivity(),
                        "Route is " + currentRoute.getDistance() + " meters long.",
                        Toast.LENGTH_SHORT).show();

                // Draw the route on the map
                drawRoute(currentRoute);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e(TAG, "Error: " + throwable.getMessage());
                Toast.makeText(getActivity(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawRoute(DirectionsRoute route) {
        // Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).getLatitude(),
                    coordinates.get(i).getLongitude());
        }

        // Draw Points on MapView
        mMap.addPolyline(new PolylineOptions()
                .add(points)
                .color(Color.parseColor("#02B3E4"))
                .width(5));
    }

    private void enableLocation() {
        // If we have the last location of the user, we can move the camera to that position.
        Location lastLocation = locationServices.getLastLocation();
        if (lastLocation != null) {
            mCurrentLocation = lastLocation;
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastLocation)));
        }

        locationServices.addLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {

                    mCurrentLocation = location;
                    originPosition = Position.fromCoordinates(location.getLongitude(), location.getLatitude());


                    if (destinationPosition != null) {
                        mapBoundZoom(new Position[]{originPosition, destinationPosition});
                    } else {
                        CameraPosition currentPlace = new CameraPosition.Builder()
                                .target(new LatLng(location))
                                .tilt(65.5f).zoom(15f).build();
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));

                    }

                    locationServices.removeLocationListener(this);
                }
            }
        });

        mMap.setMyLocationEnabled(false);
    }

    private Location getCurrentLocation() {
        return mCurrentLocation;
    }

    private void mapBoundZoom(Position[] positions) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Position p : positions) {
            builder.include(new LatLng(p.getLatitude(), p.getLongitude()));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
