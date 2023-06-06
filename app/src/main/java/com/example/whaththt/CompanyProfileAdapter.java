
package com.example.whaththt;


import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.protobuf.ByteOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class CompanyProfileAdapter extends ArrayAdapter<UserCompany> {

    private static class ViewHolder {
        ImageView imageProfile;
        TextView textName;
        RatingBar ratingBar;
        MapView mapView;
        NormalHomeFragment normalHomeFragment;

    }

    public CompanyProfileAdapter(Context context, List<UserCompany> profileItems) {
        super(context, 0, profileItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserCompany profileItem = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_profile_item, parent, false);
            viewHolder.imageProfile = convertView.findViewById(R.id.imageProfile);
            viewHolder.textName = convertView.findViewById(R.id.textName);
            viewHolder.ratingBar = convertView.findViewById(R.id.ratingBar);
            viewHolder.mapView = convertView.findViewById(R.id.mapView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set the data for each view in the item layout
        Bitmap imageBitmap = profileItem.getImage();
        viewHolder.imageProfile.setImageBitmap(imageBitmap);
        viewHolder.textName.setText(profileItem.getUsername());
        viewHolder.ratingBar.setRating(profileItem.getRatingBar());
        viewHolder.ratingBar.setIsIndicator(true);
        viewHolder.mapView.onCreate(null);
        viewHolder.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Set up the Google Maps view with your desired configuration
                // You can customize the map behavior and markers here
                viewHolder.mapView.onResume();
                googleMap.addMarker(new MarkerOptions().position(new LatLng(profileItem.getAddress().getLatitude(), profileItem.getAddress().getLongitude())));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(profileItem.getAddress().getLatitude(), profileItem.getAddress().getLongitude())), 10));
            }
        });



        return convertView;
    }


}


