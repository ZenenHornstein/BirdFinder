package com.bcit.birdfinder;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    SeekBar seeker_range_input;
    int range;
    Button button_mode_toggle;
    Button button_search;

    Marker bird_marker;
    Circle circleOverlay;

    double longitude = -123.0;
    double latitude = 49.0;

    public static final String EXTRA_DATA_LAT = "BIRD_FINDER.MAIN.DATA_LAT";
    public static final String EXTRA_DATA_LONG = "BIRD_FINDER.MAIN.DATA_LONG";
    public static final String EXTRA_DATA_RANGE = "BIRD_FINDER.MAIN.DATA_RANGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        range_input = findViewById(R.id.main_editText_range);
        button_mode_toggle = (Button) findViewById(R.id.main_button_mode);
        button_search = (Button) findViewById(R.id.main_button_search);
        seeker_range_input = (SeekBar) findViewById(R.id.main_seekBar_range);

        range = seeker_range_input.getProgress();

        seeker_range_input.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                range = seekBar.getProgress();
                circleOverlay.setRadius(range*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                circleOverlay.setStrokeColor(Color.RED);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                range = seekBar.getProgress();
                circleOverlay.setRadius(range*1000);
                circleOverlay.setStrokeColor(Color.GREEN);
            }
        });
    }


    public void toggleMapMode(View view) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void GoToSelectActivity(View view) {
        Intent intent = new Intent(this, SelectActivity.class);
        intent.putExtra(EXTRA_DATA_LAT, latitude);
        intent.putExtra(EXTRA_DATA_LONG, longitude);
        intent.putExtra(EXTRA_DATA_RANGE, range);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng origin = new LatLng(latitude, longitude);
        bird_marker = mMap.addMarker(new MarkerOptions()
                .position(origin)
                .title("Bird Marker")
                .draggable(true));

        circleOverlay = mMap.addCircle(new CircleOptions()
                .center(bird_marker.getPosition())
                .radius(seeker_range_input.getProgress()*1000)
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(30, 0,150,0))
                .visible(false));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        circleOverlay.setVisible(true);

        mMap.setOnMarkerDragListener(markerDragListener);
        mMap.setOnMapLongClickListener(mapLongClickListener);
    }

    private final GoogleMap.OnMapLongClickListener mapLongClickListener = new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            bird_marker.setPosition(latLng);
            circleOverlay.setCenter(latLng);

            latitude = latLng.latitude;
            longitude = latLng.longitude;
        }
    };

    private final GoogleMap.OnMarkerDragListener markerDragListener = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {
        }

        @Override
        public void onMarkerDrag(Marker marker) {
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            LatLng coordinates = marker.getPosition();
            circleOverlay.setCenter(coordinates);

            latitude = coordinates.latitude;
            longitude = coordinates.longitude;
        }
    };

    public void zoomIn(View view) {
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    public void zoomOut(View view) {
        mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

}