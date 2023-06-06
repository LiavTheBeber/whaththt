package com.example.whaththt.company_classes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.whaththt.normal_classes.NormalHomeFragment;
import com.example.whaththt.R;


public class ViewCompanyProfileFragment extends Fragment {

    Button backBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_company_profile, container, false);

        backBtn=view.findViewById(R.id.back_button);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigateToNormalHomeFragment();
            }
        });
    }

    private void NavigateToNormalHomeFragment() {
        NormalHomeFragment normalHomeFragment = new NormalHomeFragment();


        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(getId(), normalHomeFragment)
                .commit();
    }
}