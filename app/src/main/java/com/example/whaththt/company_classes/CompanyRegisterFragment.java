package com.example.whaththt.company_classes;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whaththt.MainActivity;
import com.example.whaththt.MapsFragment;
import com.example.whaththt.R;
import com.example.whaththt.normal_classes.NormalRegisterFragment;
import com.example.whaththt.UserCompany;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CompanyRegisterFragment extends Fragment {

    TextInputEditText etRegUsername, etRegPassword, etRegEmail, etRegPhone;
    Button btnSignUpSubmit, btnMap;
    FirebaseAuth mAuth;

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_RATING = "ratingBar";
    private static final String KEY_ADDRESS = "address";

    private String username, email, phone, password,imageURL;
    private CheckBox checkBox;
    private ImageView profileImageview;
    private Bitmap photo;
    private TextView wantUser;
    private GeoPoint address;
    private float ratingBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor myEdit;
    private String longitude, latitude;
    Uri imageUri;

    MapsFragment mapsFragment = new MapsFragment();
    FirebaseFirestore CompanyUserDB = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_register, container, false);

        etRegUsername = view.findViewById(R.id.etRegUsername);
        etRegPhone = view.findViewById(R.id.etRegPhone);
        etRegEmail = view.findViewById(R.id.etRegEmail);
        etRegPassword = view.findViewById(R.id.etRegPass);
        btnSignUpSubmit = view.findViewById(R.id.btnSignUpSubmit);
        profileImageview = view.findViewById(R.id.profile_imageview);
        checkBox = view.findViewById(R.id.check_box);
        wantUser = view.findViewById(R.id.tvWantUser);
        btnMap = view.findViewById(R.id.btnMap2);


        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        checkBox.setChecked(false);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToOtherRegisterFragment(new MapsFragment());
            }
        });

        wantUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToOtherRegisterFragment(new NormalRegisterFragment());
            }
        });

        profileImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnSignUpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });


    }
    private void createUser(){
        if (etRegUsername.getText() != null && etRegPhone.getText() != null) {
            username = etRegUsername.getText().toString();
            email = etRegEmail.getText().toString();
            phone = etRegPhone.getText().toString();
            password = etRegPassword.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String latitudeString = sharedPreferences.getString("latitude", "");
                                String longitudeString = sharedPreferences.getString("longitude", "");
                                double latitude = 0.0;
                                double longitude = 0.0;
                                if (!latitudeString.isEmpty() && !longitudeString.isEmpty()) {
                                    latitude = Double.parseDouble(latitudeString);
                                    longitude = Double.parseDouble(longitudeString);
                                }
                                address = new GeoPoint(latitude, longitude);

                               UserCompany user = new UserCompany(username, email, password,phone,address,photo, ratingBar);

                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put(KEY_USERNAME, username);
                                userMap.put(KEY_PHONE, phone);
                                userMap.put(KEY_EMAIL, email);
                                userMap.put(KEY_RATING,ratingBar);

                                userMap.put(KEY_ADDRESS, address);

                                String fileName= mAuth.getCurrentUser().getUid().toString()+".jpg";
                                StorageReference imageRef=storageRef.child("companyImages/"+fileName);
                                UploadTask uploadTask = imageRef.putFile(imageUri);

                                CompanyUserDB.collection("CompanyUserDB")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .set(userMap);
                                Intent intent = new Intent(requireContext(), MainActivity.class);
                               // intent.putExtra("user", user);
                                intent.putExtra("normalUser","2");
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(requireContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }

                        }

                    });
        }
    }
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    private void navigateToOtherRegisterFragment(Fragment fragment) {


        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.auth_frame_layout, fragment)
                .commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                // Load the selected image into the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                profileImageview.setImageBitmap(bitmap);
                checkBox.setChecked(true);
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}