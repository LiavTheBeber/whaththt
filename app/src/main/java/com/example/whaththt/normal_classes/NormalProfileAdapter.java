
package com.example.whaththt.company_classes;


import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whaththt.side_classes.User;
import com.example.whaththt.R;

import java.util.List;

public class NormalProfileAdapter extends ArrayAdapter<User> {


    private static class ViewHolder {
        ImageView imageProfile;
        TextView textName;

    }
    CompanyHomeFragment companyHomeFragment;
    public NormalProfileAdapter(Context context, List<User> profileItems,CompanyHomeFragment companyHomeFragment) {
        super(context, 0, profileItems);
        this.companyHomeFragment = companyHomeFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User profileItem = getItem(position);


        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.normal_profile_item, parent, false);
            viewHolder.imageProfile = convertView.findViewById(R.id.imageProfile);
            viewHolder.textName = convertView.findViewById(R.id.textName);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set the data for each view in the item layout
        Bitmap imageBitmap = profileItem.getImage();
        viewHolder.imageProfile.setImageBitmap(imageBitmap);
        viewHolder.textName.setText(profileItem.getUsername());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                companyHomeFragment.navigateToViewNormalProfileFragment();
            }
        });


        return convertView;
    }


}


