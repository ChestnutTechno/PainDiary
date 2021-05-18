package com.example.personalisedmobilepaindiary.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;


import com.example.personalisedmobilepaindiary.databinding.MapFragmentBinding;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

public class MapFragment extends Fragment {
    private MapFragmentBinding binding;
    private MapboxMap mapboxMap;
    private Style style;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MapFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initialiseMap(savedInstanceState);

        binding.goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mapboxMap.getMarkers().isEmpty()){
                    mapboxMap.clear();
                }
                Geocoder gc = new Geocoder(getContext());
                List<Address> adds;
                LatLng ptr = null;
                String addressStr = binding.address.getText().toString();
                if (addressStr == null || addressStr.length() <= 3) {
                    Toast.makeText(getContext(), "Invalid address", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        adds = gc.getFromLocationName(addressStr, 5);
                        if (adds == null) {
                            Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                        Address location = adds.get(0);
                        location.getLatitude();
                        location.getLongitude();

                        ptr = new LatLng(location.getLatitude(), location.getLongitude());

                        Log.d("Location", ptr.toString());

                    } catch (Exception ex) {
                        Log.e(ex.getMessage(), ex.getLocalizedMessage());
                    }
                }
                if (ptr != null){
                    CameraPosition newPosition = new CameraPosition
                            .Builder()
                            .target(ptr)
                            .zoom(13)
                            .build();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.address.getWindowToken(), 0);
                    mapboxMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(newPosition), 7000);
                    mapboxMap.addMarker(new MarkerOptions().position(ptr));

                } else {
                    Toast.makeText(getContext(), "Invalid address", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    public void initialiseMap(Bundle savedInstanceState) {

        binding.mapView.onCreate(savedInstanceState);
        final LatLng latLng = new LatLng(HomeFragment.getLocation().getLatitude(), HomeFragment.getLocation().getLongitude());
        Log.d("Initialising map", latLng.toString());
        binding.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MapFragment.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        MapFragment.this.style = style;
                        CameraPosition position = new CameraPosition.Builder()
                                .target(latLng)
                                .zoom(13)
                                .build();
                        mapboxMap.setCameraPosition(position);
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }
    @Override
    public void onStop() {
        super.onStop();
        binding.mapView.onStop();
}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }

}