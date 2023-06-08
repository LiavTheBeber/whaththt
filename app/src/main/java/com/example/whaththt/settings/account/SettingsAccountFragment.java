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
    private List<UserCompany> cUserItem;
    private List<User> userItem;
    private AccountItemAdapter accountItemAdapter;
    private String userType;
    GeoPoint geoPoint;
    LatLng location;
    CollectionReference userCollectionRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            userType = args.getString("normalUser");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_account, container, false);


        listViewItems = view.findViewById(R.id.list_view_items);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accountItems =  new ArrayList<>();
        accountItemAdapter  = new AccountItemAdapter(requireContext(),accountItems);
        listViewItems.setAdapter(accountItemAdapter);
        AddItems();


        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                SettingsItem settingItem = (SettingsItem) parent.getItemAtPosition(position);

                Toast.makeText(requireContext(), settingItem.getItemTitle() , Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void AddItems() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if (userType.equals("1")) {
            userCollectionRef = FirebaseFirestore.getInstance().collection("UsersDB");
        } else if (userType.equals("2")) {
            userCollectionRef = FirebaseFirestore.getInstance().collection("CompanyUserDB");
        } else {
            // Handle the case when the user type is unknown or not provided
            return;
        }

        userCollectionRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String username = document.getString("username");
                        String email = document.getString("email");
                        String phone = document.getString("phone");
                        if(userType.equals("2")){
                            geoPoint=document.getGeoPoint("address");
                            double latitude = geoPoint.getLatitude();
                            double longitude = geoPoint.getLongitude();
                            location = new LatLng(latitude, longitude);
                            UserCompany userCompany = new UserCompany(username,email,"",phone,new GeoPoint(latitude,longitude),null,1);
                            cUserItem.add(userCompany);
                        }
                        else {
                            User user = new User(username,email,phone,null);
                            userItem.add(user);
                        }
                    }
                } else {
                    // Handle the error
                }
            }
        });
    }

}