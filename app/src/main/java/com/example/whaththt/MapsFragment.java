package com.example.whaththt;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.whaththt.company_classes.CompanyRegisterFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 17f;
    private static final float DEFAULT_ZOOM2 = 12f;
    private LatLng markerLatLng;
    private Button saveLocation;
    DocumentReference docRef;
    String Userid;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor myEdit;

    FirebaseAuth mAuth;

    FirebaseFirestore CompanyUserDB = FirebaseFirestore.getInstance();

    Map<String, Object> updates = new HashMap<>();
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            if(mLocationPermissionGranted){
                getDeviceLocation();
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                moveCameraAnimated(new LatLng(31.065983, 34.877725),7f);
                mMap.setMyLocationEnabled(true);
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        markerLatLng=latLng;
                        myEdit.putString("latitude", String.valueOf(markerLatLng.latitude));
                        myEdit.putString("longitude", String.valueOf(markerLatLng.longitude));
                        myEdit.apply();
                        addCustomMarker(latLng);
                        moveCameraAnimated(latLng,DEFAULT_ZOOM2);

                    }
                });

            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        saveLocation = view.findViewById(R.id.btnSaveLocation);
        sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocationPermission();
        mAuth=FirebaseAuth.getInstance();
        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CompanyRegisterFragment());
            }
        });

        }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(requireContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(requireContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            }
        } else {
            requestPermissionLauncher.launch(permissions);
        }
    }
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    isGranted -> {
                        boolean allGranted = true;
                        for (Boolean granted : isGranted.values()) {
                            if (!granted) {
                                mLocationPermissionGranted = false;
                                allGranted = false;
                                break;
                            }
                        }
                        if (allGranted) {
                            // Permissions are granted, do something here
                            mLocationPermissionGranted = true;
                            initMap();
                        }
                    });

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationTask = mFusedLocationProviderClient.getLastLocation();
                locationTask.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = task.getResult();

                        } else {
                            // Handle unsuccessful task
                            Log.e(TAG, "getLastLocation: Exception: " + task.getException());
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }
    private void moveCamera(LatLng latLng, float zoom) {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }
    }
    private void moveCameraAnimated(LatLng latLng, float zoom) {
        if (mMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(zoom)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
    private void addCustomMarker(LatLng position) {
        // Create a marker options object
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position);

        // Load marker icon as a Bitmap
        Bitmap markerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.marker1);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerIcon));

        // Add the marker to the map
        mMap.addMarker(markerOptions);

    }

    private void replaceFragment(Fragment fragment) {


        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.auth_frame_layout, fragment)
                .commit();
    }
}

