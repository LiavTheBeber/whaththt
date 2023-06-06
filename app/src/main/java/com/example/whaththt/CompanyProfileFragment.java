package com.example.whaththt;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CompanyProfileFragment extends Fragment implements OnMapReadyCallback {

    private EditText etBio;
    private ImageView profileImage;
    private TextView tvComapnyUserName, tvBio;
    private Button btnSave;
    private String userId,imageUrl;

    private MapView mapView;


    RatingBar ratingBar;
    FirebaseAuth mAuth;
    FirebaseFirestore CompanyUserDB;
    DocumentReference docRef;
    FirebaseStorage storage;
    StorageReference imageRef;
    Map<String, Object> updates;

    LatLng location;
    GoogleMap mMap;
    GeoPoint geoPoint;
    double latitude,longitude;


    Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_company_profile, container, false);
        etBio = view.findViewById(R.id.edit_text);
        profileImage = view.findViewById(R.id.profile_image_view);
        tvComapnyUserName =view.findViewById(R.id.username_text_view);
        tvBio = view.findViewById(R.id.bio_text_view);
        btnSave = view.findViewById(R.id.save_button);
        ratingBar = view.findViewById(R.id.ratingBar);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);



        storage  = FirebaseStorage.getInstance();


        mAuth = FirebaseAuth.getInstance();
        CompanyUserDB = FirebaseFirestore.getInstance();
        updates = new HashMap<>();



        ratingBar.setIsIndicator(true);

        return view;
    }
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.getMapAsync(this);
        //god code onResume
        mapView.onResume();
        loadUserData();



        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageUrl = uri.toString();

                Picasso.get().load(imageUrl).into(profileImage);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed To Load Image", Toast.LENGTH_SHORT).show();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bioString = etBio.getText().toString();
                if(!bioString.equals("")){
                    tvBio.setText(etBio.getText());
                    updates.put("bio", bioString);
                    docRef.update(updates);
                }
                else
                    Toast.makeText(requireContext(), "Please Insert Bio", Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the result is from the image gallery
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);

                imageRef = storage.getReference().child("companyImages/" + userId + ".jpg");
                UploadTask uploadTask = imageRef.putFile(imageUri);

            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                geoPoint=documentSnapshot.getGeoPoint("address");
                if (geoPoint != null) {
                    double latitude = geoPoint.getLatitude();
                    double longitude = geoPoint.getLongitude();
                    location = new LatLng(latitude, longitude);
                    mMap = googleMap;
                    // Add any map customization or marker placement here

                    mMap.addMarker(new MarkerOptions().position(location));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));


                }
            }
        });


    }

    private void loadUserData() {
        userId = mAuth.getCurrentUser().getUid();
        docRef = CompanyUserDB.collection("CompanyUserDB").document(userId);

        imageRef = storage.getReference().child("companyImages/" + userId + ".jpg");

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tvComapnyUserName.setText(documentSnapshot.getString("username"));
                tvBio.setText(documentSnapshot.getString("bio"));
            }
        });
    }
}
