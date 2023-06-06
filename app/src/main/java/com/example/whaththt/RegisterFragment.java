package com.example.whaththt;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.whaththt.CompanyRegisterFragment;
import com.example.whaththt.MainActivity;
import com.example.whaththt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterFragment extends Fragment {

    TextInputEditText etRegUsername;
    TextInputEditText etRegPhone;
    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    Button btnSignUpSubmit;
    FirebaseAuth mAuth;

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BIO = "bio";
    private CheckBox checkBox;

    private ImageView profileImageview;
    private Bitmap photo;
    private Uri imageUri;
    private byte[] imageData;
    private TextView wantCompany;

    FirebaseFirestore UserDB = FirebaseFirestore.getInstance();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etRegUsername = view.findViewById(R.id.etRegUsername);
        etRegPhone = view.findViewById(R.id.etRegPhone);
        etRegEmail = view.findViewById(R.id.etRegEmail);
        etRegPassword = view.findViewById(R.id.etRegPass);
        btnSignUpSubmit = view.findViewById(R.id.btnSignUpSubmit);
        profileImageview = view.findViewById(R.id.profile_imageview);
        checkBox = view.findViewById(R.id.check_box);
        wantCompany = view.findViewById(R.id.tvWantCompany);


        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        checkBox.setChecked(false);

        wantCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToOtherRegisterFragment(new CompanyRegisterFragment());
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
                mAuth.createUserWithEmailAndPassword(etRegEmail.getText().toString(), etRegPassword.getText().toString())
                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (etRegUsername.getText() != null && etRegPhone.getText() != null) {

                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        User user = new User(etRegUsername.getText().toString(),etRegEmail.getText().toString(),etRegPhone.getText().toString(),photo);

                                        // Save the user or perform any other operations

                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put(KEY_USERNAME, user.getUsername());
                                        userMap.put(KEY_PHONE, user.getPhone());
                                        userMap.put(KEY_EMAIL, user.getEmail());
                                        userMap.put(KEY_BIO, "");

                                        // Generate a unique filename for the image
                                        String filename = mAuth.getCurrentUser().getUid().toString() + ".jpg";

                                        // Create a reference to the desired storage location
                                        StorageReference imageRef = storageRef.child("images/" + filename);

                                        // Upload the image file to Firebase Storage
                                        UploadTask uploadTask = imageRef.putFile(imageUri);

                                        UserDB.collection("UsersDB").document(mAuth.getCurrentUser().getUid()).set(userMap);

                                        Intent intent = new Intent(requireContext(), MainActivity.class);
                                        intent.putExtra("normalUser","1");
                                        intent.putExtra("user", user);
                                        startActivity(intent);
                                        getActivity().finish();

                                    }
                                    else
                                    {
                                        Toast.makeText(requireContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });


    }

    private void navigateToOtherRegisterFragment(Fragment fragment) {


        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.auth_frame_layout, fragment)
                .commit();
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