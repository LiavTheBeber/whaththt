package com.example.whaththt.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.whaththt.R;
import com.example.whaththt.company_classes.ViewCompanyProfileFragment;
import com.example.whaththt.settings.account.SettingsAccountFragment;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {

    private ListView listViewItems;
    private List<SettingsItem> settingItems;
    private SettingsItemAdapter settingsItemAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        listViewItems = view.findViewById(R.id.list_view_items);
        return view;
    }


    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        settingItems = new ArrayList<>();
        settingsItemAdapter  = new SettingsItemAdapter(requireContext(),settingItems);
        listViewItems.setAdapter(settingsItemAdapter);

        AddItems();

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                SettingsItem selectedItem = (SettingsItem) parent.getItemAtPosition(position);

                if (selectedItem.getItemTitle().toString().equals("Account")){
                    SettingsAccountFragment settingsAccountFragment = new SettingsAccountFragment();
                    replaceFragment(settingsAccountFragment);
                }

            }
        });



    }

    private void AddItems(){
        settingItems.add(new SettingsItem(R.drawable.baseline_person_24,"Account"));
        settingItems.add(new SettingsItem(R.drawable.baseline_notifications_24,"Notifications"));
        settingItems.add(new SettingsItem(R.drawable.baseline_remove_red_eye_24,"Appearance"));
        settingItems.add(new SettingsItem(R.drawable.baseline_security_24,"Security"));
        settingItems.add(new SettingsItem(R.drawable.baseline_support_agent_24,"Support"));
        settingItems.add(new SettingsItem(R.drawable.baseline_help_outline_24,"About"));
        settingsItemAdapter.notifyDataSetChanged();
    }





    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(getId(), fragment)
                .commit();
    }


}