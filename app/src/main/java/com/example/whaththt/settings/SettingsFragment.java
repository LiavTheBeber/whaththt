package com.example.whaththt.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.whaththt.AuthenticationActivity;
import com.example.whaththt.R;
import com.example.whaththt.side_classes.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {

    private ListView listViewItems;
    private List<SettingItem> settingItems;
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
                SettingItem settingItem = (SettingItem) parent.getItemAtPosition(position);

                Toast.makeText(requireContext(), settingItem.getItemTitle() , Toast.LENGTH_SHORT).show();

            }
        });






    }

    private void AddItems(){
        settingItems.add(new SettingItem(R.drawable.baseline_person_24,"Account"));
        settingItems.add(new SettingItem(R.drawable.baseline_notifications_24,"Notifications"));
        settingItems.add(new SettingItem(R.drawable.baseline_remove_red_eye_24,"Appearance"));
        settingItems.add(new SettingItem(R.drawable.baseline_security_24,"Security"));
        settingItems.add(new SettingItem(R.drawable.baseline_support_agent_24,"Support"));
        settingItems.add(new SettingItem(R.drawable.baseline_help_outline_24,"About"));
        settingsItemAdapter.notifyDataSetChanged();
    }


}