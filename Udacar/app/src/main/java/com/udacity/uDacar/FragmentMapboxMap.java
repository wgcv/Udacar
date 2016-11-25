package com.udacity.uDacar;


import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyBearingTracking;
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
import com.mapbox.services.commons.turf.TurfMeasurement;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;
import com.mapbox.services.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.geocoding.v5.models.GeocodingResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMapboxMap extends Fragment implements  MapboxMap.OnMyLocationChangeListener,
        ISphinxListener {


    private static final String TAG = "FragmentMapboxMap";
    private static final int PERMISSIONS_REQUEST_LOCATION_AUDIO = 3;
    /*
    * Pocket Sphinx
    */
    protected static final int RESULT_SPEECH = 4;
    SphinxSpeechRecognition mRecognizer;

    private TextToSpeech mTextToSpeech;

    /*
    * Mapbox
    */
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Marker destinationMarker;
    private LocationServices locationServices;
    private MarkerView currentLocMarker;
    private Location mCurrentLocation;
    private LatLng mCurrentLatLng;
    private Icon mCarIcon;

    private GeocoderAutoCompleteView mAutoCompleteSearch;

    TextView tv;

    public FragmentMapboxMap() {
        // Required empty public constructor
    }

    public static FragmentMapboxMap newInstance() {
        FragmentMapboxMap fragment = new FragmentMapboxMap();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MapboxAccountManager.start(getActivity(), getString(R.string.mapbox_token));
        View view = inflater.inflate(R.layout.fragment_mapbox_map, container, false);

        mAutoCompleteSearch = (GeocoderAutoCompleteView) view.findViewById(R.id.search_box);
        locationServices = LocationServices.getLocationServices(getActivity());
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        tv = (TextView) view.findViewById(R.id.text_view);
        tv.setText("Preparing the recognizer");

        mAutoCompleteSearch.setAccessToken(MapboxAccountManager.getInstance().getAccessToken());
        mAutoCompleteSearch.setType(GeocodingCriteria.TYPE_POI);
        mAutoCompleteSearch.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
            @Override
            public void OnFeatureClick(CarmenFeature feature) {
                Position position = feature.asPosition();
                updateMap(position.getLatitude(), position.getLongitude());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer.destroy();

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(),
                            "This device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestLocationAndAudioPermissions();

        return view;
    }

    private void requestLocationAndAudioPermissions() {
        Log.d(TAG, "requestLocationAndAudioPermissions");
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.RECORD_AUDIO
            }, PERMISSIONS_REQUEST_LOCATION_AUDIO);
        } else {
            initializeView();
        }
    }

    public void initializeView() {
        Log.d(TAG, "initializing view");



        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap map) {
                mapboxMap = map;
                if (!locationServices.areLocationPermissionsGranted()) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION_AUDIO);
                } else {
                    enableMapboxMap();

                    mapboxMap.setOnMyLocationChangeListener(FragmentMapboxMap.this);
                    mapboxMap.setMyLocationEnabled(true);
                    mapboxMap.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
                    mapboxMap.getMyLocationViewSettings().setAccuracyTintColor(Color.parseColor("#02B3E4"));
                    mapboxMap.getMyLocationViewSettings().setEnabled(true);
                    mapboxMap.getTrackingSettings().setDismissBearingTrackingOnGesture(false);
                    mapboxMap.getUiSettings().setRotateGesturesEnabled(true);
                    mapboxMap.getTrackingSettings().setMyBearingTrackingMode(MyBearingTracking.GPS);
                    mapboxMap.getMarkerViewManager().scheduleViewMarkerInvalidation();

                    mapboxMap.getMyLocationViewSettings().setForegroundDrawable(
                            ContextCompat.getDrawable(getActivity(), R.drawable.car_marker_icon),
                            ContextCompat.getDrawable(getActivity(), R.drawable.car_marker_icon));


                }
            }
        });

        mRecognizer = new SphinxSpeechRecognition(getActivity(), this);
        mRecognizer.runRecognizer();

        mTextToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.UK);
                    mTextToSpeech.setPitch(0.6f);
                    mTextToSpeech.setSpeechRate(2);
                }
            }
        });



        // Create an Icon object for the marker to use
        IconFactory iconFactory = IconFactory.getInstance(getActivity());
        Drawable iconDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.car_marker_icon);
        mCarIcon = iconFactory.fromDrawable(iconDrawable);
    }

    private void enableMapboxMap() {
        enableLocation();
    }


    private void enableLocation() {
        // If we have the last location of the user, we can move the camera to that position.
        Location lastLocation = locationServices.getLastLocation();
        if (lastLocation != null && mapboxMap != null) {
            mCurrentLocation = lastLocation;


            /*//add marker
            currentLocMarker = mapboxMap.addMarker(new MarkerViewOptions()
                    .icon(mCarIcon)
                    .anchor(0.5f, 0.5f)
                    .flat(true)
                    .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));*/

            //build camera option to animate map camera
            CameraPosition currentPlace = new CameraPosition.Builder()
                    .target(new LatLng(mCurrentLocation))
                    .zoom(20f)
                    .build();

            //animate map camera
            mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));

        }
        locationServices.addLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(MainActivity.this, "onLocationChanged", Toast.LENGTH_SHORT).show();

                if(mCurrentLocation != null) {
                    Position pos1 = Position.fromCoordinates(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude());
                    Position pos2 = Position.fromCoordinates(location.getLongitude(), location.getLatitude());
                    double targetBearing = TurfMeasurement.bearing(pos1, pos2);

                    CameraPosition currentPlace = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(18f)
                            .bearing(targetBearing)
                            .build();

                    mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(currentPlace), 4000);
                }else{
                    CameraPosition currentPlace = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(18f)
                            .build();

                    mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(currentPlace), 4000);
                }
                mCurrentLocation = location;

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_LOCATION_AUDIO) {
            if (grantResults.length >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                initializeView();
            } else {
                getActivity().finish();
            }
        }
    }

    @Override
    public void onMyLocationChange(@Nullable Location location) {

    }

    @Override
    public void sphinxFailedRecognition(String msg) {
        tv.setText(msg);
    }

    @Override
    public void sphinxError(String msg) {
        tv.setText(msg);
    }

    @Override
    public void sphinxHintMsg(String msg) {
        tv.setText(msg);
    }

    @Override
    public void callGoogleSpeechRecognition() {
        String text1 = "Where do you need directions to ?";
        mTextToSpeech.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).run();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text1);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 50000);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        try {
            startActivityForResult(intent, RESULT_SPEECH);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(),
                    "This device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void runRecognizer(){
        if(mRecognizer != null){
            mRecognizer.runRecognizer();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        runRecognizer();
        //mRecognizer.startListeningWakeup();

        switch (requestCode) {
            case RESULT_SPEECH: {

                if (resultCode == Activity.RESULT_OK && null != data) {

                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (text.size() > 0) {
                        searchCoordinates(text);
                    } else {
                        Toast.makeText(getActivity(),
                                "We had a failure to communicate.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }

        }
    }

    public void searchCoordinates(ArrayList<String> text)  {
        resetMap();
        try {
            searchForEndpointCoordinates(text.get(0));
        } catch (ServicesException e) {
            Log.e(TAG, "Exception While Searching:" + e.getMessage());
            Toast.makeText(getActivity(), "Error While Searching For Endpoint", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchForEndpointCoordinates(final String endpoint) throws ServicesException {

        // Get Coordinates For Endpoint
        MapboxGeocoding client = new MapboxGeocoding.Builder()
                .setAccessToken(getString(R.string.mapbox_token))
                .setLocation(endpoint)
                .setProximity(Position.fromCoordinates(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude()))
                .build();

        client.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                if (response != null && response.body() != null && response.body().getFeatures() != null) {
                    List<CarmenFeature> features = response.body().getFeatures();
                    if (!features.isEmpty()) {
                        Log.i(TAG, "Coordinates (Lat / Lon) = " + features.get(0).asPosition().getLatitude() + ", " + features.get(0).asPosition().getLongitude());
                        try {
                            searchForDirections(features.get(0));
                        } catch (ServicesException e) {
                            Log.e(TAG, "Exception While Searching for Directions: " + e);
                            Toast.makeText(getActivity(), "Error While Searching For Directions", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),
                                "Couldn't find a coordinate for '" + endpoint + "'",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Toast.makeText(getActivity(),
                        "Failure occurred while trying to find a coordinate for '" + endpoint + "'",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    DirectionsRoute mCurRoute;
    private void searchForDirections(final CarmenFeature endpointFeature) throws ServicesException {

        final ArrayList<Position> points = new ArrayList<>();
        points.add(Position.fromCoordinates(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude()));
        points.add(endpointFeature.asPosition());

        MapboxDirections client = new MapboxDirections.Builder()
                .setAccessToken(getString(R.string.mapbox_token))
                .setCoordinates(points)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .setOverview(DirectionsCriteria.OVERVIEW_FULL)
                .setSteps(true)
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response != null && response.body() != null
                        && response.body().getRoutes() != null && response.body().getRoutes().size() > 0) {

                    DirectionsRoute route = response.body().getRoutes().get(0);
                    mCurRoute = route;
                    resetMap();

                    destinationMarker = mapboxMap.addMarker(new MarkerOptions().position(new LatLng(endpointFeature.asPosition().getLatitude(), endpointFeature.asPosition().getLongitude())).title(endpointFeature.getPlaceName()).snippet(endpointFeature.getText()));

                    // Center Route On Map
                   /* LatLng center = new LatLng(
                            (mCurrentLocation.getLatitude() + endpointFeature.asPosition().getLatitude()) / 2,
                            (mCurrentLocation.getLongitude() + endpointFeature.asPosition().getLongitude()) / 2);*/
                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                            .include(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                            .include(new LatLng(endpointFeature.asPosition().getLatitude(), endpointFeature.asPosition().getLongitude()))
                            .build();


                    CameraUpdate update = CameraUpdateFactory.newLatLngBounds(latLngBounds, 10);
                    mapboxMap.moveCamera(update);
                    //mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(center).build()));

                    // Draw the route on the map
                    drawRoute(route);
                } else {
                    Toast.makeText(getActivity(), "No routes could be found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(getActivity(),
                        "Failure occurred while trying to find directions for '" + endpointFeature.getPlaceName() + "'",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawRoute(DirectionsRoute route) {

        // show route
        List<PolylineOptions> polylineOptions = new ArrayList<>();
        PolylineOptions builder = new PolylineOptions();
        builder.color(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        builder.alpha(0.5f);

        LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordinates = lineString.getCoordinates();
        for (int lc = 0; lc < coordinates.size(); lc++) {
            Log.d(TAG, "directions: " + coordinates.get(lc).getLatitude() + ";" + coordinates.get(lc).getLongitude());
            builder.add(new LatLng(coordinates.get(lc).getLatitude(), coordinates.get(lc).getLongitude()));
        }

        builder.width(getResources().getDimension(R.dimen.line_width_route));
        polylineOptions.add(builder);
        mapboxMap.addPolylines(polylineOptions);

    }

    private void resetMap() {
        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        mapboxMap.removeAnnotations();
    }

    private void updateMap(double latitude, double longitude) {
        resetMap();

        double originLat = mCurrentLocation.getLatitude();
        double originLong = mCurrentLocation.getLongitude();

        Position originPosition = Position.fromCoordinates(originLong, originLat);
        Position destinationPosition = Position.fromCoordinates(longitude, latitude);

        mapboxMap.addMarker(new MarkerOptions()
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

    private void mapBoundZoom(Position[] positions) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Position p : positions) {
            builder.include(new LatLng(p.getLatitude(), p.getLongitude()));
        }
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20));
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
                DirectionsRoute currentRoute = response.body().getRoutes().get(0);
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

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

        if (mRecognizer != null) {
            mRecognizer.destroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * Dispatch onLowMemory() to all fragments.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();

        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }

        if (mRecognizer != null) {
            mRecognizer.destroy();
        }
    }
}
