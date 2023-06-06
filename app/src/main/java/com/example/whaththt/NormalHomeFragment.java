package com.example.whaththt;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.grpc.Context;

public class NormalHomeFragment extends Fragment {

    private ListView listViewProfiles;
    //private CompanyProfileAdapter profileAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    FirebaseFirestore companyUsersDB;
    CollectionReference companyRef;
    FirebaseStorage firebaseStorage;
    StorageReference imageRef;
    String username;
    float ratingBar;
    GeoPoint geoPoint;
    LatLng location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listViewProfiles = view.findViewById(R.id.listViewProfiles);
        firebaseStorage  = FirebaseStorage.getInstance();


         companyUsersDB = FirebaseFirestore.getInstance();
         companyRef = companyUsersDB.collection("CompanyUserDB");


        sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        return view;
    }

    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        companyRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<UserCompany> profileItems = new ArrayList<>();
                    CompanyProfileAdapter companyProfileAdapter = new CompanyProfileAdapter(requireContext(), profileItems);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Retrieve data from the document
                        String username = document.getString("username");
                        float ratingBar = document.getDouble("ratingBar").floatValue();
                        String documentId = document.getId();
                        imageRef = firebaseStorage.getReference().child("companyImages/" + documentId + ".jpg");

                        geoPoint=document.getGeoPoint("address");
                        double latitude = geoPoint.getLatitude();
                        double longitude = geoPoint.getLongitude();
                        location = new LatLng(latitude, longitude);
                        // Create a temporary UserCompany object without the image
                        UserCompany userCompany = new UserCompany(username, "", "", "", new GeoPoint(latitude,longitude), null, ratingBar);

                        // Add the temporary userCompany object to the list
                        profileItems.add(userCompany);




                        // Retrieve the image from Firebase Storage
                        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                // Decode the image bytes into a Bitmap
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                // Set the image to the corresponding userCompany object
                                userCompany.setImage(bitmap);

                                // Notify the adapter that the data has changed
                                companyProfileAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    // Create and set the adapter
                    companyProfileAdapter.notifyDataSetChanged();
                    listViewProfiles.setAdapter(companyProfileAdapter);
                } else {
                    // Handle the error
                }
            }
        });
        listViewProfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserCompany selectedItem = (UserCompany) adapterView.getItemAtPosition(i);
                navigateToViewCompanyProfileFragment();
            }
        });

    }

    private void navigateToViewCompanyProfileFragment() {
       ViewCompanyProfileFragment viewCompanyProfileFragment = new ViewCompanyProfileFragment();



        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.auth_frame_layout, viewCompanyProfileFragment)
                .commit();
    }
}
