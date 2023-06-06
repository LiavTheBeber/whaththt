package com.example.whaththt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.whaththt.AuthenticationActivity;
import com.example.whaththt.R;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {

    private Button logoutButton;
    FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        logoutButton = view.findViewById(R.id.logout_button);
        mAuth = FirebaseAuth.getInstance();


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        return view;
    }
    private void logout() {
        // Perform any necessary logout tasks here, such as clearing user data or session

        mAuth.signOut();

        // Start the authentication activity
        Intent intent = new Intent(requireContext(), AuthenticationActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}