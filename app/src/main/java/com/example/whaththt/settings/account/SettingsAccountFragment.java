package com.example.whaththt.settings.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.whaththt.R;
import com.example.whaththt.settings.SettingsItem;
import com.example.whaththt.settings.SettingsItemAdapter;
import com.example.whaththt.side_classes.User;
import com.example.whaththt.side_classes.UserCompany;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;


public class SettingsAccountFragment extends Fragment {

    private ListView listViewItems;
    private List<AccountItem> accountItems;
    private AccountItemAdapter accountItemAdapter;

    private String KEY_USERNAME = "Username";
    private String KEY_EMAIL = "Email";
    private String KEY_PHONE = "Phone";

    private String userID,username,email,phone;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private DocumentReference docRef;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_account, container, false);
        listViewItems = view.findViewById(R.id.list_view_items);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountItems =  new ArrayList<>();
        accountItemAdapter  = new AccountItemAdapter(requireContext(),accountItems);
        listViewItems.setAdapter(accountItemAdapter);

        userID = mAuth.getCurrentUser().getUid();
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                username = documentSnapshot.getString("username");
                email = documentSnapshot.getString("email");
                phone = documentSnapshot.getString("phone");

                accountItems.add(new AccountItem(KEY_USERNAME,username));
                accountItems.add(new AccountItem(KEY_EMAIL,email));
                accountItems.add(new AccountItem(KEY_PHONE,phone));

            }
        });

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                SettingsItem settingItem = (SettingsItem) parent.getItemAtPosition(position);

                Toast.makeText(requireContext(), settingItem.getItemTitle() , Toast.LENGTH_SHORT).show();

            }
        });


    }

}