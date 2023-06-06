package com.example.whaththt.company_classes;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.whaththt.R;
import com.example.whaththt.User;
import com.example.whaththt.UserCompany;
import com.example.whaththt.company_classes.CompanyProfileAdapter;
import com.example.whaththt.company_classes.ViewCompanyProfileFragment;
import com.example.whaththt.normal_classes.ViewNormalProfileFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CompanyHomeFragment extends Fragment {

    private ListView listViewProfiles;
    FirebaseFirestore UsersDB;
    CollectionReference userRef;
    FirebaseStorage firebaseStorage;
    StorageReference imageRef;
    GeoPoint geoPoint;
    LatLng location;
    com.example.whaththt.company_classes.NormalProfileAdapter normalProfileAdapter;
    List<User> profileItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_home, container, false);
        listViewProfiles = view.findViewById(R.id.listViewProfiles);
        firebaseStorage  = FirebaseStorage.getInstance();


        UsersDB = FirebaseFirestore.getInstance();
        userRef = UsersDB.collection("UsersDB");

        return view;
    }

    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        profileItems = new ArrayList<>();
        normalProfileAdapter = new com.example.whaththt.company_classes.NormalProfileAdapter(requireContext(), profileItems,this);
        listViewProfiles.setAdapter(normalProfileAdapter);
        AddItems();


    }


    private void AddItems()
    {
        userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Retrieve data from the document
                        String username = document.getString("username");
                        String documentId = document.getId();
                        imageRef = firebaseStorage.getReference().child("images/" + documentId + ".jpg");

                        geoPoint=document.getGeoPoint("address");
                        double latitude = geoPoint.getLatitude();
                        double longitude = geoPoint.getLongitude();
                        location = new LatLng(latitude, longitude);
                        // Create a temporary UserCompany object without the image
                        User user = new User(username,null,null,null);

                        // Add the temporary userCompany object to the list

                        profileItems.add(user);



                        // Retrieve the image from Firebase Storage
                        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                // Decode the image bytes into a Bitmap
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                // Set the image to the corresponding userCompany object
                                user.setImage(bitmap);

                                // Notify the adapter that the data has changed
                                normalProfileAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    // Create and set the adapter
                    normalProfileAdapter.notifyDataSetChanged();
                } else {
                    // Handle the error
                }
            }
        });
    }



    public void navigateToViewNormalProfileFragment() {
        ViewNormalProfileFragment viewNormalProfileFragment = new ViewNormalProfileFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(getId(), viewNormalProfileFragment)
                .commit();
    }
}
