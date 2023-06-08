package com.example.whaththt.settings.account;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whaththt.R;
import com.example.whaththt.settings.SettingsFragment;
import com.example.whaththt.settings.SettingsItem;
import com.example.whaththt.settings.SettingsItemAdapter;

import java.util.List;

public class AccountItemAdapter extends ArrayAdapter<AccountItem> {
    public AccountItemAdapter(Context context, List<AccountItem> accountItems) {
        super(context, 0, accountItems);
    }
    SettingsAccountFragment settingsAccountFragment;
    private static class ViewHolder{
        TextView textItemDescription;
        TextView textViewTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AccountItem accountItem = getItem(position);

        AccountItemAdapter.ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new AccountItemAdapter.ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_settings_item, parent, false);
            viewHolder.textItemDescription = convertView.findViewById(R.id.iconImageView);
            viewHolder.textViewTitle = convertView.findViewById(R.id.TitleTextView);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (AccountItemAdapter.ViewHolder) convertView.getTag();

        viewHolder.textItemDescription.setText(accountItem.getItemTitle());
        viewHolder.textViewTitle.setText(accountItem.getItemTitle());

        return convertView;

    }
}
